package com.tvds.newtvdsbackend.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tvds.newtvdsbackend.domain.dto.RailwayVehiclePageDTO;
import com.tvds.newtvdsbackend.domain.entity.RailwayVehicle;
import com.tvds.newtvdsbackend.domain.vo.RailwayVehicleVO;
import org.apache.ibatis.annotations.Param;

/**
* @author make
* @description 针对表【railway_vehicle】的数据库操作Mapper
* @createDate 2025-06-25 21:18:37
* @Entity com.tvds.newtvdsbackend.RailwayVehicle
*/
public interface RailwayVehicleMapper extends BaseMapper<RailwayVehicle> {
    Page<RailwayVehicleVO> getRailwayVehiclePage(Page<RailwayVehicleVO> page, @Param("dto") RailwayVehiclePageDTO dto);
}




