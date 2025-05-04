package com.tvds.newtvdsbackend.domain.mq;

import lombok.Data;

import java.util.List;

@Data
public class ComponentLocationResult {
    private List<List<Integer>> boxes;
    private List<Double> confidences;
    private List<String> abnormalityResults;
    private List<Boolean> isAbnormal;
    private List<String> imagePaths;
}