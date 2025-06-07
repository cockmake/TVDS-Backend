package com.tvds.newtvdsbackend.domain.vo;

import lombok.Data;

import java.util.Date;

@Data
public class ComponentVO {
    private String id;
    private String componentName;
    private String componentType;
    private String componentDesc;
    private Double detectionIou;
    private Double detectionConf;
    private String abnormalityDesc;
    private Date createdAt;
    private Integer totalCount;
}
