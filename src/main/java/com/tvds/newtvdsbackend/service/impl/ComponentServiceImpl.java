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
import com.tvds.newtvdsbackend.utils.CommonUtil;
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

        if (labelBoxByComponentId == null || labelBoxByComponentId.isEmpty()) {
            throw new ServiceException(Map.of("1", "没有找到该组件被标记的任何模板"));
        }
        Map<String, Map<String, Object>> result = CommonUtil.formatVisualPrompt(labelBoxByComponentId, minioConfig.getTemplateImageBucket());
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




