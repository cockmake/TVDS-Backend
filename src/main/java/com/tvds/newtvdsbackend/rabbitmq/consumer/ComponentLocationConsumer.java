package com.tvds.newtvdsbackend.rabbitmq.consumer;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.tvds.newtvdsbackend.domain.entity.DetectionResult;
import com.tvds.newtvdsbackend.domain.entity.DetectionTask;
import com.tvds.newtvdsbackend.domain.mq.ComponentLocationResponse;
import com.tvds.newtvdsbackend.domain.mq.ComponentLocationResult;
import com.tvds.newtvdsbackend.service.DetectionResultService;
import com.tvds.newtvdsbackend.service.DetectionTaskService;
import com.tvds.newtvdsbackend.utils.SnowflakeUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Component
@RequiredArgsConstructor
public class ComponentLocationConsumer {

    private final DetectionResultService detectionResultService;
    private final DetectionTaskService detectionTaskService;

    @RabbitListener(
            bindings = @QueueBinding(
                    value = @Queue(value = "consumer.component.location.queue", durable = "true"),
                    exchange = @Exchange(value = "component.location.exchange", type = "direct", durable = "true"),
                    key = "consumer.component.location.key"
            )
    )
    public void receiveMessage(ComponentLocationResponse response) {
        System.out.println(response);
        String taskId = response.getTaskId();
        List<Map<String, ComponentLocationResult>> componentLocationResults = response.getComponentLocationResults();
        try {
            List<DetectionResult> detectionResults = new ArrayList<>();
            for (int direction = 0; direction < componentLocationResults.size(); direction++) {
                Map<String, ComponentLocationResult> results = componentLocationResults.get(direction);
                int finalDirection = direction;
                results.forEach((componentId, result) -> {
                    List<List<Integer>> boxes = result.getBoxes();
                    List<Double> confidences = result.getConfidences();
                    List<String> abnormalityResults = result.getAbnormalityResults();
                    List<Boolean> isAbnormalList = result.getIsAbnormal();
                    List<String> imagePaths = result.getImagePaths();
                    System.out.println(imagePaths);
                    for (int i = 0; i < boxes.size(); i++) {
                        List<Integer> box = boxes.get(i);
                        DetectionResult dr = new DetectionResult();
                        dr.setTaskId(taskId);
                        dr.setDetectionConf(confidences.get(i));
                        dr.setAbnormalityDesc(abnormalityResults.get(i));
                        dr.setIsAbnormal(isAbnormalList.get(i) ? 1 : 0);
                        dr.setComponentId(componentId);
                        dr.setComponentImagePath(imagePaths.get(i));
                        dr.setX1(box.get(0).doubleValue());
                        dr.setY1(box.get(1).doubleValue());
                        dr.setX2(box.get(2).doubleValue());
                        dr.setY2(box.get(3).doubleValue());
                        dr.setDirection(finalDirection);
                        detectionResults.add(dr);
                    }
                });
            }
            detectionResultService.saveBatch(detectionResults);
            // 将任务标记为完成
            updateTaskStatus(taskId, 2);
        } catch (Exception e) {
            e.printStackTrace();
            updateTaskStatus(taskId, 3);
        }
    }

    private void updateTaskStatus(String taskId, int status) {
        detectionTaskService.update(
                new com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper<DetectionTask>()
                        .eq(DetectionTask::getId, taskId)
                        .set(DetectionTask::getTaskStatus, status)
        );
    }
}