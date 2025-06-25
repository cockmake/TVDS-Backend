package com.tvds.newtvdsbackend.domain.mq;

import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class ComponentLocationResponse {
    private String taskId;
    private List<Map<String, ComponentLocationResult>> componentLocationResults;
}