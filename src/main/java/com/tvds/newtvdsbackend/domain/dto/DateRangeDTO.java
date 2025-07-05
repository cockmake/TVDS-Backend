package com.tvds.newtvdsbackend.domain.dto;

import lombok.Data;

import java.util.Date;

@Data
public class DateRangeDTO {
    private Date startDate; // 开始日期，格式为 "yyyy-MM-dd"
    private Date endDate;   // 结束日期，格式为 "yyyy-MM-dd"
}
