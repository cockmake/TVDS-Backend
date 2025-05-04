package com.tvds.newtvdsbackend.domain.mq;

import lombok.Data;

import java.util.Map;

@Data
public class ComponentLocationResponse {
    private String taskId;
    private Map<String, ComponentLocationResult> componentLocationResult;
}