package com.tvds.newtvdsbackend.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tvds.newtvdsbackend.domain.entity.DetectionResult;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.tvds.newtvdsbackend.domain.vo.DetectionComponentPartVO;
import com.tvds.newtvdsbackend.domain.vo.DetectionComponentTypeVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;


public interface DetectionResultMapper extends BaseMapper<DetectionResult> {
    List<DetectionComponentTypeVO> selectComponentCountByTaskId(@Param("taskId") String taskId);

    Page<DetectionComponentPartVO> getDetectionComponentDetailInfoPage(
            IPage<DetectionComponentPartVO> page,
            @Param("taskId") String taskId,
            @Param("componentId") String ComponentId
    );
}




