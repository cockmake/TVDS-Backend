package com.tvds.newtvdsbackend.domain.vo;

import lombok.Data;

import java.util.Date;

@Data
public class VehicleExtraTaskItem {
    private String taskId; // 任务ID
    private Integer taskStatus;
    Date createdAt;
    Date updatedAt;
}
