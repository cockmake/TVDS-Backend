package com.tvds.newtvdsbackend.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tvds.newtvdsbackend.domain.dto.BoxDTO;
import com.tvds.newtvdsbackend.domain.dto.ComponentTemplateImageBoxDTO;
import com.tvds.newtvdsbackend.domain.entity.ComponentTemplateImageBox;
import com.tvds.newtvdsbackend.domain.vo.ComponentTemplateImageBoxVO;
import com.tvds.newtvdsbackend.exception.ServiceException;
import com.tvds.newtvdsbackend.service.ComponentTemplateImageBoxService;
import com.tvds.newtvdsbackend.mapper.ComponentTemplateImageBoxMapper;
import com.tvds.newtvdsbackend.service.ComponentTemplateImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class ComponentTemplateImageBoxServiceImpl extends ServiceImpl<ComponentTemplateImageBoxMapper, ComponentTemplateImageBox>
        implements ComponentTemplateImageBoxService {
    private final ComponentTemplateImageService componentTemplateImageService;

    @Override
    // 开启事物
    @Transactional
    public boolean removeAndSave(String templateImageId, ComponentTemplateImageBoxDTO componentTemplateImageBoxDTO) {
        // 查询templateImageId是否存在
        if (componentTemplateImageService.getById(templateImageId) == null) {
            Map<String, String> errors = Map.of("templateImageId", "模板图片不存在");
            throw new ServiceException(errors);
        }
        LambdaQueryWrapper<ComponentTemplateImageBox> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ComponentTemplateImageBox::getTemplateImageId, templateImageId);
        // 删除原有数据
        this.remove(wrapper);
        // 插入新数据
        for (BoxDTO boxDTO : componentTemplateImageBoxDTO.getCoors()) {
            ComponentTemplateImageBox componentTemplateImageBox = new ComponentTemplateImageBox();
            componentTemplateImageBox.setTemplateImageId(templateImageId);
            componentTemplateImageBox.setX1(boxDTO.getCoor().get(0));
            componentTemplateImageBox.setY1(boxDTO.getCoor().get(1));
            componentTemplateImageBox.setX2(boxDTO.getCoor().get(2));
            componentTemplateImageBox.setY2(boxDTO.getCoor().get(3));
            this.save(componentTemplateImageBox);
        }
        return true;
    }

    @Override
    public ComponentTemplateImageBoxVO getComponentTemplateImageBox(String templateImageId) {
        LambdaQueryWrapper<ComponentTemplateImageBox> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ComponentTemplateImageBox::getTemplateImageId, templateImageId);
        List<ComponentTemplateImageBox> componentTemplateImageBoxes = this.list(wrapper);

        // 转换为VO
        List<List<Float>> coors = componentTemplateImageBoxes.stream()
                .map(box -> List.of(box.getX1(), box.getY1(), box.getX2(), box.getY2()))
                .collect(Collectors.toList());
        ComponentTemplateImageBoxVO componentTemplateImageBoxVO = new ComponentTemplateImageBoxVO();
        componentTemplateImageBoxVO.setCoors(coors);
        return componentTemplateImageBoxVO;

    }
}




