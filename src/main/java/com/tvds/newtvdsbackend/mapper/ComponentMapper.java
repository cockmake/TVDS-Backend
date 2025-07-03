package com.tvds.newtvdsbackend.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.tvds.newtvdsbackend.domain.entity.Component;
import com.tvds.newtvdsbackend.domain.vo.VisualPromptVO;

import java.util.List;
import java.util.Map;

/**
* @author make
* @description 针对表【component】的数据库操作Mapper
* @createDate 2025-04-24 08:42:04
* @Entity com.tvds.newtvdsbackend.domain.entity.Component
*/

public interface ComponentMapper extends BaseMapper<Component> {
     List<VisualPromptVO> findLabelBoxByComponentId(String componentId);
     List<VisualPromptVO> findAllLabelBox();
}




