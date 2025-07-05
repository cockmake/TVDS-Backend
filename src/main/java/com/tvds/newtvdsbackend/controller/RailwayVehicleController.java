package com.tvds.newtvdsbackend.controller;

import cn.hutool.http.ContentType;
import com.tvds.newtvdsbackend.domain.dto.DateRangeDTO;
import com.tvds.newtvdsbackend.domain.dto.RailwayVehicleFormDTO;
import com.tvds.newtvdsbackend.domain.dto.RailwayVehiclePageDTO;
import com.tvds.newtvdsbackend.domain.entity.RailwayVehicle;
import com.tvds.newtvdsbackend.domain.vo.BaseResponseVO;
import com.tvds.newtvdsbackend.domain.vo.PageVO;
import com.tvds.newtvdsbackend.domain.vo.RailwayVehicleVO;
import com.tvds.newtvdsbackend.exception.ServiceException;
import com.tvds.newtvdsbackend.service.RailwayVehicleService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/railway-vehicle")
@RequiredArgsConstructor
public class RailwayVehicleController {
    private final RailwayVehicleService railwayTrainService;


    @PostMapping("/vehicle-info-options")
    public BaseResponseVO getVehicleInfoOptions(
            @RequestBody DateRangeDTO dateRange
    ) {
        List<String> vehicleInfoOptions = railwayTrainService.getVehicleInfoOptions(
                dateRange.getStartDate(),
                dateRange.getEndDate()
        );
        return BaseResponseVO.success(vehicleInfoOptions);
    }

    @PostMapping
    public BaseResponseVO addRailwayVehicle(
            @RequestPart(required = false) String recordStation,
            @RequestPart(required = false) String travelDirection,
            @RequestPart(required = false) String vehicleInfo,
            @RequestPart(required = false) String vehicleIdentity,
            @RequestPart(required = false) String bureau,
            @RequestPart(required = false) String section,
            @RequestPart(required = false) String vehicleSeq,
            @RequestPart(required = false) String totalSequence,
            @RequestPart(required = false) String vehicleDesc,
            @RequestPart(required = false) MultipartFile[] imageFiles
    ) {
        System.out.println(recordStation);
        System.out.println(imageFiles.length);
        boolean f = railwayTrainService.addRailwayVehicle(
                recordStation,
                travelDirection,
                vehicleInfo,
                vehicleIdentity,
                bureau,
                section,
                vehicleDesc,
                vehicleSeq,
                totalSequence,
                imageFiles
        );
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
            @RequestBody @Validated RailwayVehicleFormDTO railwayVehicleFormDTO

    ) {
        boolean f = railwayTrainService.updateRailwayVehicle(id, railwayVehicleFormDTO);
        return BaseResponseVO.success(f);
    }

    @PostMapping("/page")
    public BaseResponseVO getRailwayVehiclePage(
            @RequestBody @Validated RailwayVehiclePageDTO railwayVehiclePageDTO
    ) {
        PageVO<RailwayVehicleVO> railwayVehiclePage = railwayTrainService.getRailwayVehiclePage(railwayVehiclePageDTO);
        return BaseResponseVO.success(railwayVehiclePage);
    }

    @GetMapping("/{id}/{direction}/preview")
    public ResponseEntity<InputStreamResource> getRailwayVehiclePreview(
            @PathVariable @NotEmpty String id,
            @PathVariable @NotEmpty Integer direction
    ) {
        InputStream stream = railwayTrainService.getRailwayVehicleImage(id, direction);

        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_PNG)
                .header("Cache-Control", "max-age=2592000")
                .body(new InputStreamResource(stream));
    }
}
