package com.tvds.newtvdsbackend.service;

import com.tvds.newtvdsbackend.domain.dto.ComponentTemplateImageBoxDTO;
import com.tvds.newtvdsbackend.domain.entity.ComponentTemplateImageBox;
import com.baomidou.mybatisplus.extension.service.IService;
import com.tvds.newtvdsbackend.domain.vo.ComponentTemplateImageBoxVO;



public interface ComponentTemplateImageBoxService extends IService<ComponentTemplateImageBox> {
    boolean removeAndSave(String templateImageId, ComponentTemplateImageBoxDTO componentTemplateImageBoxDTO);
    ComponentTemplateImageBoxVO getComponentTemplateImageBox(String templateImageId);
}
