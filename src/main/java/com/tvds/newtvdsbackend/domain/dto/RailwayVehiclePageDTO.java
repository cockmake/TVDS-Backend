package com.tvds.newtvdsbackend.domain.dto;

import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Data
public class RailwayVehiclePageDTO {
    @NotNull(message = "行车信息不能为空")
    String vehicleInfo;
    @NotNull(message = "行车备注不能为空")
    String vehicleDesc;
    @Min(value = 1, message = "当前页码不能小于1")
    Integer currentPage;
    @Min(value = 1, message = "每页条数不能小于1")
    Integer pageSize;
}
