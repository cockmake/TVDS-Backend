package com.tvds.newtvdsbackend.domain.vo;

import lombok.Data;

import java.util.Date;

@Data
public class DetectionComponentPartVO {
    private String resultId;
    private Double detectionConf;
    private Boolean isAbnormal;
    private Double x1;
    private Double y1;
    private Double x2;
    private Double y2;

    // 检测任务
    private Date taskCreatedAt;
    private Date taskUpdatedAt;

    // 从 RailwayVehicleVO 平铺过来的字段
    private String vehicleId;          // 对应 RailwayVehicleVO.id
    private Integer direction;
    private String vehicleInfo;
    private String vehicleDesc;
    private String recordStation;
    private String vehicleIdentity;
    private String travelDirection;
    private String bureau;
    private String section;
    private Integer vehicleSeq;
    private Integer totalSequence;
    private Date vehicleCreatedAt;     // 对应 RailwayVehicleVO.createdAt

}
