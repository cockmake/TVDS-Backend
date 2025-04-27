package com.tvds.newtvdsbackend.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.tvds.newtvdsbackend.domain.dto.ComponentDTO;
import com.tvds.newtvdsbackend.domain.dto.ComponentPageDTO;
import com.tvds.newtvdsbackend.domain.entity.Component;
import com.tvds.newtvdsbackend.domain.vo.ComponentVO;
import com.tvds.newtvdsbackend.domain.vo.PageVO;

import java.io.InputStream;
import java.util.List;


public interface ComponentService extends IService<Component> {
    boolean addNewComponent(ComponentDTO componentDTO);

    boolean updateComponent(String id, ComponentDTO componentDTO);

    boolean deleteComponent(String id);

    PageVO<ComponentVO> pageComponent(ComponentPageDTO componentPageDTO);

    byte[] getComponentVisualPrompt(String componentId);
}
