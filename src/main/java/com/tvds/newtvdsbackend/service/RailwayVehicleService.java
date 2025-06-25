package com.tvds.newtvdsbackend.service;

import com.tvds.newtvdsbackend.domain.dto.RailwayVehicleFormDTO;
import com.tvds.newtvdsbackend.domain.dto.RailwayVehiclePageDTO;
import com.tvds.newtvdsbackend.domain.entity.RailwayVehicle;
import com.baomidou.mybatisplus.extension.service.IService;
import com.tvds.newtvdsbackend.domain.vo.PageVO;
import com.tvds.newtvdsbackend.domain.vo.RailwayVehicleVO;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.List;


public interface RailwayVehicleService extends IService<RailwayVehicle> {
    boolean addRailwayVehicle(
            String recordStation,
            String travelDirection,
            String vehicleInfo,
            String vehicleIdentity,
            String bureau,
            String section,
            String vehicleDesc,
            MultipartFile[] imageFiles
    );
    PageVO<RailwayVehicleVO> getRailwayVehiclePage(RailwayVehiclePageDTO railwayVehiclePageDTO);
    boolean updateRailwayVehicle(String id, RailwayVehicleFormDTO railwayVehicleFormDTO);
    InputStream getRailwayVehicleImage(String id, Integer direction);
}