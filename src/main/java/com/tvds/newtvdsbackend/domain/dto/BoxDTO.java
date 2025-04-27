package com.tvds.newtvdsbackend.domain.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

@Data
public class BoxDTO {
    /**
     * 坐标列表
     */
    @NotNull(message = "坐标列表不能为空")
    @Size(min = 4, max = 4, message = "坐标列表必须为[x1,y1,x2,y2]")
    private List<@NotNull(message = "坐标值不能为空") Float> coor;
}