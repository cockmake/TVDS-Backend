package com.tvds.newtvdsbackend.controller;

import cn.hutool.http.ContentType;
import com.tvds.newtvdsbackend.domain.dto.RailwayVehiclePageDTO;
import com.tvds.newtvdsbackend.domain.entity.RailwayVehicle;
import com.tvds.newtvdsbackend.domain.vo.BaseResponseVO;
import com.tvds.newtvdsbackend.domain.vo.PageVO;
import com.tvds.newtvdsbackend.domain.vo.RailwayVehicleVO;
import com.tvds.newtvdsbackend.exception.ServiceException;
import com.tvds.newtvdsbackend.service.RailwayVehicleService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotNull;
import java.io.InputStream;
import java.util.Map;

@RestController
@RequestMapping("/railway-vehicle")
@RequiredArgsConstructor
public class RailwayVehicleController {
    private final RailwayVehicleService railwayTrainService;

    @PostMapping
    public BaseResponseVO addRailwayVehicle(
            @RequestPart(required = false) String vehicleInfo,
            @RequestPart(required = false) String vehicleDesc,
            @RequestPart MultipartFile imageFile
    ) {
        boolean f = railwayTrainService.addRailwayVehicle(vehicleInfo, vehicleDesc, imageFile);
        if (f) {
            return BaseResponseVO.success(f);
        }
        throw new ServiceException(Map.of("1", "添加失败"));
    }

    @DeleteMapping("/{id}")
    public BaseResponseVO deleteRailwayVehicle(
            @PathVariable String id
    ) {
        boolean f = railwayTrainService.removeById(id);
        return BaseResponseVO.success(f);
    }

    @PutMapping("/{id}")
    public BaseResponseVO updateRailwayVehicle(
            @PathVariable String id,
            @RequestBody RailwayVehiclePageDTO railwayVehiclePageDTO

    ) {
        boolean f = railwayTrainService.updateRailwayVehicle(id, railwayVehiclePageDTO);
        return BaseResponseVO.success(f);
    }

    @PostMapping("/page")
    public BaseResponseVO getRailwayVehiclePage(
            @RequestBody @Validated RailwayVehiclePageDTO railwayVehiclePageDTO
    ) {
        PageVO<RailwayVehicleVO> railwayVehiclePage = railwayTrainService.getRailwayVehiclePage(railwayVehiclePageDTO);
        return BaseResponseVO.success(railwayVehiclePage);
    }

    @GetMapping("/{id}/preview")
    public ResponseEntity<InputStreamResource> getRailwayVehiclePreview(
            @PathVariable String id
    ) {
        InputStream stream = railwayTrainService.getRailwayVehicleImage(id);

        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_PNG)
                .header("Cache-Control", "max-age=2592000")
                .body(new InputStreamResource(stream));
    }
}
