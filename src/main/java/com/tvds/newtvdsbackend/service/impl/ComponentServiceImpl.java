package com.tvds.newtvdsbackend.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tvds.newtvdsbackend.configuration.MinioConfig;
import com.tvds.newtvdsbackend.domain.dto.ComponentDTO;
import com.tvds.newtvdsbackend.domain.dto.ComponentPageDTO;
import com.tvds.newtvdsbackend.domain.entity.Component;
import com.tvds.newtvdsbackend.domain.vo.ComponentVO;
import com.tvds.newtvdsbackend.domain.vo.PageVO;
import com.tvds.newtvdsbackend.domain.vo.VisualPromptVO;
import com.tvds.newtvdsbackend.exception.ServiceException;
import com.tvds.newtvdsbackend.service.ComponentService;
import com.tvds.newtvdsbackend.mapper.ComponentMapper;
import com.tvds.newtvdsbackend.utils.HttpUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ComponentServiceImpl extends ServiceImpl<ComponentMapper, Component>
        implements ComponentService {
    private final RestTemplate restTemplate;
    private final MinioConfig minioConfig;

    @Override
    public boolean addNewComponent(ComponentDTO componentDTO) {
        Component component = new Component();
        BeanUtil.copyProperties(componentDTO, component);
        // 这里可以继续后续的保存操作
        return this.save(component);
    }

    @Override
    public boolean updateComponent(String id, ComponentDTO componentDTO) {
        Component component = new Component();
        BeanUtil.copyProperties(componentDTO, component);
        component.setId(id);
        return this.updateById(component);
    }

    @Override
    public boolean deleteComponent(String id) {
        return this.removeById(id);
    }

    @Override
    public PageVO<ComponentVO> pageComponent(ComponentPageDTO componentPageDTO) {
        System.out.println(componentPageDTO);
        if (componentPageDTO.getComponentName() == null) {
            componentPageDTO.setComponentName("");
        }
        if (componentPageDTO.getComponentType() == null) {
            // 这个是精确查询
            componentPageDTO.setComponentType("");
        }
        Page<Component> page = new Page<>(componentPageDTO.getCurrentPage(), componentPageDTO.getPageSize());
        LambdaQueryWrapper<Component> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(Component::getComponentName, componentPageDTO.getComponentName());
        if (!Objects.equals(componentPageDTO.getComponentType(), "")) {
            // 这个是精确查询
            queryWrapper.eq(Component::getComponentType, componentPageDTO.getComponentType());
        }
        queryWrapper.orderByDesc(Component::getCreatedAt);
        Page<Component> componentPage = this.page(page, queryWrapper);
        PageVO<ComponentVO> pageVO = new PageVO<>();
        pageVO.setCurrentPage(componentPage.getCurrent());
        pageVO.setPageSize(componentPage.getSize());
        pageVO.setTotal(componentPage.getTotal());
        pageVO.setRecords(componentPage.getRecords().stream()
                .map(component -> {
                    ComponentVO componentVO = new ComponentVO();
                    BeanUtil.copyProperties(component, componentVO);
                    return componentVO;
                }).collect(Collectors.toList())
        );
        return pageVO;
    }

    @Override
    public byte[] getComponentVisualPrompt(String componentId) {
        // 这里需要调用Python的FastAPI服务进行生成

        // 1. 查询该componentId对应的TemplateImage和TemplateImage对应的Box信息
        List<VisualPromptVO> labelBoxByComponentId = this.baseMapper.findLabelBoxByComponentId(componentId);
        // 其中VisualPromptVO(componentId=1915454965191065601, componentName=123, componentType=默认, imagePath=1915454965191065601/1915788577791279104.jpg, x1=184.0, y1=183.0, x2=275.0, y2=238.0)
        // 以componentId为key
        // 信息包括componentName, componentType
        // 列表 imagePath x1, y1, x2, y2
        // {
        //     "1915454965191065601": {
        //         "componentName": "123",
        //         "componentType": "默认",
        //         "templateImage": [
        //             {
        //                 "imagePath": "1915454965191065601/1915788577791279104.jpg",
        //                 'boxes': [[x1, y1, x2, y2], [x1, y1, x2, y2]]
        //             }
        //         ]
        //     }
        // }
        // 其实预览接口的result只有一个key
        // 这里为了统一处理，还是用Map<String, Map<String, Object>>来存储
        if (labelBoxByComponentId == null || labelBoxByComponentId.isEmpty()) {
            throw new ServiceException(Map.of("1", "没有找到该组件被标记的任何模板"));
        }
        Map<String, Map<String, Object>> result = labelBoxByComponentId.stream().collect(
                Collectors.groupingBy(
                        VisualPromptVO::getComponentId, // 按 componentId 分组
                        Collectors.collectingAndThen(   // 对每个 componentId 分组的 List<VisualPromptVO> 进行后续处理
                                Collectors.toList(),        // 先将分组结果收集成 List
                                list -> {                   // 处理这个 List
                                    if (list.isEmpty()) {
                                        return new HashMap<String, Object>(); // 处理空列表情况
                                    }
                                    // 从列表的第一个元素获取 componentName 和 componentType
                                    VisualPromptVO firstVO = list.get(0);
                                    Map<String, Object> componentInfo = new HashMap<>();
                                    componentInfo.put("componentName", firstVO.getComponentName());
                                    componentInfo.put("componentType", firstVO.getComponentType());
                                    componentInfo.put("bucketName", minioConfig.getTemplateImageBucket());

                                    // 按 imagePath 对当前 componentId 的 VisualPromptVO 列表进行分组
                                    Map<String, List<VisualPromptVO>> imagesGroupedByPath = list.stream()
                                            .collect(Collectors.groupingBy(VisualPromptVO::getImagePath));

                                    // 构建 templateImage 列表
                                    List<Map<String, Object>> templateImageList = imagesGroupedByPath.entrySet().stream()
                                            .map(entry -> {
                                                String imagePath = entry.getKey();
                                                List<VisualPromptVO> vosForPath = entry.getValue();

                                                // 提取当前 imagePath 对应的所有 boxes
                                                List<List<Float>> boxes = vosForPath.stream()
                                                        .map(vo -> Arrays.asList(vo.getX1(), vo.getY1(), vo.getX2(), vo.getY2()))
                                                        .collect(Collectors.toList());

                                                Map<String, Object> imageDetails = new HashMap<>();
                                                imageDetails.put("imagePath", imagePath);
                                                imageDetails.put("boxes", boxes);
                                                return imageDetails;
                                            })
                                            .collect(Collectors.toList());

                                    componentInfo.put("templateImage", templateImageList);
                                    return componentInfo; // 返回构建好的内部 Map
                                }
                        )
                ));
        // 2. 调用Python的FastAPI服务进行生成
        String path = "/component-template-image/visual-prompt/preview";
        String url = HttpUtil.staticFastapiUrl + path;
        // 3. 发送POST请求返回图片流
        try {
            return restTemplate.postForObject(url, result, byte[].class);
        } catch (Exception e) {
            System.out.println("Python端处理错误");
            return null;
        }
    }
}




