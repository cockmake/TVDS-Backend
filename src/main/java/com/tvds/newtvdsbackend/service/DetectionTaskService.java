package com.tvds.newtvdsbackend.service;

import com.tvds.newtvdsbackend.domain.dto.DetectionTaskPageDTO;
import com.tvds.newtvdsbackend.domain.entity.DetectionTask;
import com.baomidou.mybatisplus.extension.service.IService;
import com.tvds.newtvdsbackend.domain.vo.DetectionTaskVO;
import com.tvds.newtvdsbackend.domain.vo.PageVO;

public interface DetectionTaskService extends IService<DetectionTask> {
    boolean createDetectionTaskV1(String vehicleId);
    boolean createDetectionTaskV2(String vehicleId);
    PageVO<DetectionTaskVO> getDetectionTaskPage(DetectionTaskPageDTO detectionTaskPageDTO);
}