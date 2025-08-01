package com.tvds.newtvdsbackend.domain.vo;

import lombok.Data;

import java.util.Date;

@Data
public class VehicleExtraTaskItem {
    private String taskId; // 任务ID
    private Integer taskStatus;
    Date createdAt;
    Date updatedAt;
    private Integer abnormalCount = 0; // 异常数量，默认为 0
}
