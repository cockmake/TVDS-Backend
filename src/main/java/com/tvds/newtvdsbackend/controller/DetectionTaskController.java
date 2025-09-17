package com.tvds.newtvdsbackend.controller;

import com.tvds.newtvdsbackend.domain.dto.DetectionTaskPageDTO;
import com.tvds.newtvdsbackend.domain.vo.BaseResponseVO;
import com.tvds.newtvdsbackend.service.DetectionTaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/detection-task")
@RequiredArgsConstructor
public class DetectionTaskController {
    private final DetectionTaskService detectionTaskService;

    @PostMapping("/exec/{vehicleId}")
    public BaseResponseVO createDetectionTask(
            @PathVariable String vehicleId
    ) {
        boolean f = detectionTaskService.createDetectionTaskV2(vehicleId);
        return BaseResponseVO.success(f);
    }

    @PostMapping("/page")
    public BaseResponseVO getDetectionTaskPage(
            @RequestBody @Validated DetectionTaskPageDTO detectionTaskPageDTO
    ) {
        return BaseResponseVO.success(
                detectionTaskService.getDetectionTaskPage(detectionTaskPageDTO)
        );
    }

    @DeleteMapping("/{taskId}")
    public BaseResponseVO deleteDetectionTask(
            @PathVariable String taskId
    ) {
        boolean f = detectionTaskService.removeById(taskId);
        return BaseResponseVO.success(f);
    }
}
