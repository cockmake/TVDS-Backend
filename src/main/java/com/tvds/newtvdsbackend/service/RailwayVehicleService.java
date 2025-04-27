package com.tvds.newtvdsbackend.service;

import com.tvds.newtvdsbackend.domain.dto.RailwayVehiclePageDTO;
import com.tvds.newtvdsbackend.domain.entity.RailwayVehicle;
import com.baomidou.mybatisplus.extension.service.IService;
import com.tvds.newtvdsbackend.domain.vo.PageVO;
import com.tvds.newtvdsbackend.domain.vo.RailwayVehicleVO;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.List;


public interface RailwayVehicleService extends IService<RailwayVehicle> {
    boolean addRailwayVehicle(String vehicleInfo, String vehicleDesc, MultipartFile imageFile);
    PageVO<RailwayVehicleVO> getRailwayVehiclePage(RailwayVehiclePageDTO railwayVehiclePageDTO);
    boolean updateRailwayVehicle(String id, RailwayVehiclePageDTO railwayVehiclePageDTO);
    InputStream getRailwayVehicleImage(String id);
}
