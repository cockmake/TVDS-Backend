package com.tvds.newtvdsbackend.domain.vo;

import lombok.Data;

import java.util.Date;

@Data
public class RailwayVehicleVO {
    private String id;
    private String vehicleInfo;
    private String vehicleDesc;
    private Date createdAt;
}
