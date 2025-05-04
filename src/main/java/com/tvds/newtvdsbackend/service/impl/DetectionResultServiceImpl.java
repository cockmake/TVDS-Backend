package com.tvds.newtvdsbackend.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tvds.newtvdsbackend.configuration.MinioConfig;
import com.tvds.newtvdsbackend.domain.entity.DetectionResult;
import com.tvds.newtvdsbackend.domain.vo.DetectionComponentPartVO;
import com.tvds.newtvdsbackend.domain.vo.DetectionComponentTypeVO;
import com.tvds.newtvdsbackend.exception.ServiceException;
import com.tvds.newtvdsbackend.service.DetectionResultService;
import com.tvds.newtvdsbackend.mapper.DetectionResultMapper;
import io.minio.GetObjectArgs;
import io.minio.MinioClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class DetectionResultServiceImpl extends ServiceImpl<DetectionResultMapper, DetectionResult>
        implements DetectionResultService {
    private final MinioConfig minioConfig;
    private final MinioClient minioClient;

    @Override
    public List<DetectionComponentTypeVO> getDetectionComponentTypeByTaskId(String taskId) {
        return this.baseMapper.selectComponentCountByTaskId(taskId);
    }

    @Override
    public List<DetectionComponentPartVO> getDetectionComponentPartByComponentId(String taskId, String componentId) {
        LambdaQueryWrapper<DetectionResult> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(DetectionResult::getTaskId, taskId)
                .eq(DetectionResult::getComponentId, componentId)
                .orderByAsc(DetectionResult::getX1);
        List<DetectionResult> detectionResults = this.list(wrapper);
        return detectionResults.stream()
                .map(result -> {
                    DetectionComponentPartVO vo = new DetectionComponentPartVO();
                    vo.setResultId(result.getId());
                    vo.setDetectionConf(result.getDetectionConf());
                    vo.setIsAbnormal(result.getIsAbnormal() == 1);
                    vo.setX1(result.getX1());
                    vo.setY1(result.getY1());
                    vo.setX2(result.getX2());
                    vo.setY2(result.getY2());
                    return vo;
                }).collect(Collectors.toList());
    }

    @Override
    public InputStream getDetectionResultPreview(String resultId) {
        LambdaQueryWrapper<DetectionResult> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(DetectionResult::getId, resultId);
        DetectionResult detectionResult = this.getOne(wrapper);
        String bucketName = minioConfig.getDetectResultBucket();
        String objectName = detectionResult.getComponentImagePath();
        try {
            return minioClient.getObject(
                    GetObjectArgs.builder()
                            .bucket(bucketName)
                            .object(objectName)
                            .build()
            );
        } catch (Exception e) {
            throw new ServiceException(
                    Map.of("error", "获取检测结果预览失败")
            );
        }
    }
}




