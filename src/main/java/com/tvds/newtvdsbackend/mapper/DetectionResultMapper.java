package com.tvds.newtvdsbackend.mapper;

import com.tvds.newtvdsbackend.domain.entity.DetectionResult;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.tvds.newtvdsbackend.domain.vo.DetectionComponentTypeVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;


public interface DetectionResultMapper extends BaseMapper<DetectionResult> {
    List<DetectionComponentTypeVO> selectComponentCountByTaskId(@Param("taskId") String taskId);
}




