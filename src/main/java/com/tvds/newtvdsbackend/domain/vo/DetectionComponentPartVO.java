package com.tvds.newtvdsbackend.domain.vo;

import lombok.Data;

@Data
public class DetectionComponentPartVO {
    private String resultId;
    private Double detectionConf;
    private Boolean isAbnormal;
    private Double x1;
    private Double y1;
    private Double x2;
    private Double y2;
}
