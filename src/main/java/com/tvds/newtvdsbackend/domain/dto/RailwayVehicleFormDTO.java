package com.tvds.newtvdsbackend.domain.dto;

import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Data
public class RailwayVehicleFormDTO {
    @NotNull(message = "探测站信息不能为空")
    String recordStation;
    @NotNull(message = "车辆行驶方向不能为空")
    String travelDirection;
    @NotNull(message = "车次信息不能为空")
    String vehicleInfo;
    @NotNull(message = "车辆唯一标识不能为空")
    String vehicleIdentity;
    @NotNull(message = "局信息不能为空")
    String bureau;
    @NotNull(message = "段信息不能为空")
    String section;
    @NotNull(message = "行车备注不能为空")
    String vehicleDesc;
}
