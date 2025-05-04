package com.tvds.newtvdsbackend.domain.vo;

import lombok.Data;

import java.util.Date;

@Data
public class DetectionTaskVO {
    String id;
    Integer taskStatus;
    String vehicleId;  // 在 railway_vehicle 表中对应的 id
    String vehicleInfo;
    String vehicleDesc;
    Date createdAt;
    Date updatedAt;
}
