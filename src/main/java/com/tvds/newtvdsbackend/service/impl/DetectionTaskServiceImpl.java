package com.tvds.newtvdsbackend.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tvds.newtvdsbackend.configuration.MinioConfig;
import com.tvds.newtvdsbackend.domain.dto.DetectionTaskPageDTO;
import com.tvds.newtvdsbackend.domain.entity.Component;
import com.tvds.newtvdsbackend.domain.entity.DetectionTask;
import com.tvds.newtvdsbackend.domain.entity.RailwayVehicle;
import com.tvds.newtvdsbackend.domain.vo.DetectionTaskVO;
import com.tvds.newtvdsbackend.domain.vo.PageVO;
import com.tvds.newtvdsbackend.domain.vo.VisualPromptVO;
import com.tvds.newtvdsbackend.exception.ServiceException;
import com.tvds.newtvdsbackend.mapper.ComponentMapper;
import com.tvds.newtvdsbackend.mapper.RailwayVehicleMapper;
import com.tvds.newtvdsbackend.service.ComponentService;
import com.tvds.newtvdsbackend.service.DetectionTaskService;
import com.tvds.newtvdsbackend.mapper.DetectionTaskMapper;
import com.tvds.newtvdsbackend.service.RailwayVehicleService;
import com.tvds.newtvdsbackend.utils.CommonUtil;
import com.tvds.newtvdsbackend.utils.RabbitMqUtil;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessagePostProcessor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service
@RequiredArgsConstructor
public class DetectionTaskServiceImpl extends ServiceImpl<DetectionTaskMapper, DetectionTask>
        implements DetectionTaskService {

    private final RailwayVehicleMapper railwayVehicleMapper;
    private final ComponentMapper componentMapper;
    private final RabbitTemplate rabbitTemplate;
    private final MinioConfig minioConfig;

    @Override
    public boolean createDetectionTaskV1(String vehicleId) {
        // 1. 查询车辆是否存在
        RailwayVehicle railwayVehicle = railwayVehicleMapper.selectById(vehicleId);
        if (railwayVehicle == null) {
            throw new ServiceException(Map.of(
                    "1", "车辆不存在"
            ));
        }
        // 2. 车辆存在就创建检测任务
        DetectionTask detectionTask = new DetectionTask();
        detectionTask.setVehicleId(vehicleId);
        // 3. 查询到视觉提示模板信息
        List<VisualPromptVO> allLabelBox = componentMapper.findAllLabelBox();
        Map<String, Map<String, Object>> result = CommonUtil.formatVisualPrompt(allLabelBox, minioConfig.getTemplateImageBucket());
        // 4.将任务写入到数据库
        boolean f = this.save(detectionTask);
        if (!f) {
            throw new ServiceException(Map.of(
                    "1", "创建检测任务失败"
            ));
        }
        // 5. 将所有待检测模板放到任务队列中
        rabbitTemplate.convertAndSend(
                RabbitMqUtil.COMPONENT_LOCATION_EXCHANGE_NAME,
                RabbitMqUtil.PRODUCER_COMPONENT_LOCATION_ROUTING_KEY,
                Map.of(
                        "taskId", detectionTask.getId(),
                        "detectionResultBucket", minioConfig.getDetectResultBucket(),
                        "vehicleImagePaths", List.of(
                                railwayVehicle.getImagePathA(),
                                railwayVehicle.getImagePathB(),
                                railwayVehicle.getImagePathC(),
                                railwayVehicle.getImagePathD(),
                                railwayVehicle.getImagePathE()
                        ),
                        "railwayVehicleBucket", minioConfig.getRailwayVehicleBucket(),
                        "templateImageList", result
                ),
                message -> {
                    // 设置消息的过期时间为 5 分钟
                    // 这里可以对消息进行一些处理
                    return message;
                }
        );
        return true;
    }

    @Override
    public boolean createDetectionTaskV2(String vehicleId) {
        // 1. 查询车辆是否存在
        RailwayVehicle railwayVehicle = railwayVehicleMapper.selectById(vehicleId);
        if (railwayVehicle == null) {
            throw new ServiceException(Map.of(
                    "1", "车辆不存在"
            ));
        }
        // 2. 车辆存在就创建检测任务
        DetectionTask detectionTask = new DetectionTask();
        detectionTask.setVehicleId(vehicleId);
        // 3. 查询模型的类别信息
        LambdaQueryWrapper<Component> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.orderByAsc(Component::getCreatedAt);
        List<Component> componentList = componentMapper.selectList(queryWrapper);
        if (componentList.isEmpty()) {
            throw new ServiceException(Map.of(
                    "1", "未配置检测模型"
            ));
        }
        // 4.将任务写入到数据库
        boolean f = this.save(detectionTask);
        if (!f) {
            throw new ServiceException(Map.of(
                    "1", "创建检测任务失败"
            ));
        }
        // 5. 将所有待检测模板放到任务队列中
        rabbitTemplate.convertAndSend(
                RabbitMqUtil.COMPONENT_LOCATION_EXCHANGE_NAME,
                RabbitMqUtil.PRODUCER_COMPONENT_LOCATION_ROUTING_KEY,
                Map.of(
                        "taskId", detectionTask.getId(),
                        "detectionResultBucket", minioConfig.getDetectResultBucket(),
                        "vehicleImagePaths", List.of(
                                railwayVehicle.getImagePathA(),
                                railwayVehicle.getImagePathB(),
                                railwayVehicle.getImagePathC(),
                                railwayVehicle.getImagePathD(),
                                railwayVehicle.getImagePathE()
                        ),
                        "railwayVehicleBucket", minioConfig.getRailwayVehicleBucket(),
                        "componentList", componentList
                ),
                message -> {
                    // 设置消息的过期时间为 5 分钟
                    // 这里可以对消息进行一些处理
                    return message;
                }
        );
        // 6. 返回结果
        return true;
    }

    @Override
    public PageVO<DetectionTaskVO> getDetectionTaskPage(DetectionTaskPageDTO detectionTaskPageDTO) {
        IPage<DetectionTaskVO> page = new Page<>(detectionTaskPageDTO.getCurrentPage(), detectionTaskPageDTO.getPageSize());
        this.baseMapper.getDetectionTaskPage(page, detectionTaskPageDTO);
        return new PageVO<>(
                page.getTotal(),
                page.getCurrent(),
                page.getSize(),
                page.getRecords()
        );
    }
}




