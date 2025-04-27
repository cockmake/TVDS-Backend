package com.tvds.newtvdsbackend.domain.dto;

import lombok.Data;

import javax.validation.constraints.Min;

@Data
public class ComponentPageDTO {
    String componentName;
    String componentType;
    @Min(value = 1, message = "页码不能小于1")
    Long currentPage;
    @Min(value = 1, message = "每页条数不能小于1")
    Long pageSize;
}
