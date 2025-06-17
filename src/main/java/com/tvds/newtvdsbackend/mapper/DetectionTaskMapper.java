package com.tvds.newtvdsbackend.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tvds.newtvdsbackend.domain.dto.DetectionTaskPageDTO;
import com.tvds.newtvdsbackend.domain.entity.DetectionTask;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.tvds.newtvdsbackend.domain.vo.DetectionComponentPartVO;
import com.tvds.newtvdsbackend.domain.vo.DetectionTaskVO;
import org.apache.ibatis.annotations.Param;

/**
 * @author make
 * @description 针对表【detection_task】的数据库操作Mapper
 * @createDate 2025-04-29 16:38:18
 * @Entity com.tvds.newtvdsbackend.domain.entity.DetectionTask
 */
public interface DetectionTaskMapper extends BaseMapper<DetectionTask> {
    Page<DetectionTaskVO> getDetectionTaskPage(IPage<DetectionTaskVO> page, @Param("dto") DetectionTaskPageDTO detectionTaskPageDTO);
}




