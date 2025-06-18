package com.tvds.newtvdsbackend.domain.vo;

import lombok.Data;

@Data
public class DetectionComponentTypeVO {
    private String componentId;
    private String componentType;
    private String componentName;
    private Long count;
}