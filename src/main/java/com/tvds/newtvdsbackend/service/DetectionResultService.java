package com.tvds.newtvdsbackend.service;

import com.tvds.newtvdsbackend.domain.entity.DetectionResult;
import com.baomidou.mybatisplus.extension.service.IService;
import com.tvds.newtvdsbackend.domain.vo.DetectionComponentPartVO;
import com.tvds.newtvdsbackend.domain.vo.DetectionComponentTypeVO;
import com.tvds.newtvdsbackend.domain.vo.PageVO;
import org.apache.ibatis.annotations.Param;

import java.io.InputStream;
import java.util.List;


public interface DetectionResultService extends IService<DetectionResult> {
    List<DetectionComponentTypeVO> getDetectionComponentTypeByTaskId(@Param("taskId") String taskId, @Param("direction") Integer direction);
    PageVO<DetectionComponentPartVO> getDetectionComponentPartByComponentId(String taskId, Integer direction, String componentId);
    InputStream getDetectionResultPreview(String resultId);
}
