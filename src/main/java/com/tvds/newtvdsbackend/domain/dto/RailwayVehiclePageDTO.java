package com.tvds.newtvdsbackend.domain.dto;

import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;

@Data
public class RailwayVehiclePageDTO {
    @NotNull(message = "车次列表字段不能为空")
    List<String> vehicleInfoList;
    @Min(value = 1, message = "当前页码不能小于1")
    Integer currentPage;
    @Min(value = 1, message = "每页条数不能小于1")
    Integer pageSize;
    Date startDate;
    Date endDate;
}
