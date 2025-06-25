package com.tvds.newtvdsbackend.controller;

import com.tvds.newtvdsbackend.domain.vo.BaseResponseVO;
import com.tvds.newtvdsbackend.domain.vo.DetectionComponentPartVO;
import com.tvds.newtvdsbackend.domain.vo.DetectionComponentTypeVO;
import com.tvds.newtvdsbackend.domain.vo.PageVO;
import com.tvds.newtvdsbackend.service.DetectionResultService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.InputStream;
import java.util.List;

@RestController
@RequestMapping("/detection-result")
@RequiredArgsConstructor
public class DetectionResultController {
    private final DetectionResultService detectionResultService;

    @GetMapping("/{taskId}/{direction}")
    public BaseResponseVO getDetectionComponentType(
            @PathVariable @NotNull String taskId,
            @PathVariable @NotNull Integer direction
    ) {
        List<DetectionComponentTypeVO> vos = detectionResultService.getDetectionComponentTypeByTaskId(taskId, direction);
        return BaseResponseVO.success(vos);
    }

    @GetMapping("/{taskId}/{direction}/{componentId}")
    public BaseResponseVO getDetectionComponentPart(
            @PathVariable @NotEmpty String taskId,
            @PathVariable @NotEmpty Integer direction,
            @PathVariable @NotEmpty String componentId
    ) {
        PageVO<DetectionComponentPartVO> pageVO = detectionResultService.getDetectionComponentPartByComponentId(taskId, direction, componentId);
        return BaseResponseVO.success(pageVO);
    }

    @GetMapping("/{resultId}/preview")
    public ResponseEntity<InputStreamResource> getDetectionResultPreview(
            @PathVariable String resultId
    ) {
        InputStream inputStream = detectionResultService.getDetectionResultPreview(resultId);
        InputStreamResource resource = new InputStreamResource(inputStream);
        return ResponseEntity.ok()
                .header("Content-Disposition", "attachment; filename=\"preview.jpg\"")
                .header("Content-Type", "image/jpeg")
                .header("Cache-Control", "max-age=2592000")
                .body(resource);
    }
}
