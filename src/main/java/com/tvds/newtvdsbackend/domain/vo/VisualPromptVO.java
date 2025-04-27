package com.tvds.newtvdsbackend.domain.vo;

import lombok.Data;

@Data
public class VisualPromptVO {
    private String componentId;
    private String componentName;
    private String componentType;
    private String imagePath;
    private Float x1;
    private Float y1;
    private Float x2;
    private Float y2;
}
