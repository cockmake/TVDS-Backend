package com.tvds.newtvdsbackend.domain.vo;

import lombok.Data;

import java.util.Date;

@Data
public class RailwayVehicleVO {
    String id;
    String vehicleInfo;
    String vehicleDesc;
    Date createdAt;
}
