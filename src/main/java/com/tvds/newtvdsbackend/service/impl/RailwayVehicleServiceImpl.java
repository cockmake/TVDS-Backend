package com.tvds.newtvdsbackend.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tvds.newtvdsbackend.configuration.MinioConfig;
import com.tvds.newtvdsbackend.domain.dto.RailwayVehiclePageDTO;
import com.tvds.newtvdsbackend.domain.entity.RailwayVehicle;
import com.tvds.newtvdsbackend.domain.vo.PageVO;
import com.tvds.newtvdsbackend.domain.vo.RailwayVehicleVO;
import com.tvds.newtvdsbackend.exception.ServiceException;
import com.tvds.newtvdsbackend.service.RailwayVehicleService;
import com.tvds.newtvdsbackend.mapper.RailwayVehicleMapper;
import com.tvds.newtvdsbackend.utils.CommonUtil;
import com.tvds.newtvdsbackend.utils.SnowflakeUtil;
import io.minio.GetObjectArgs;
import io.minio.GetObjectResponse;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.Objects;


@Service
@RequiredArgsConstructor
public class RailwayVehicleServiceImpl extends ServiceImpl<RailwayVehicleMapper, RailwayVehicle>
        implements RailwayVehicleService {

    private final MinioConfig minioConfig;
    private final MinioClient minioClient;

    @Override
    public boolean addRailwayVehicle(String vehicleInfo, String vehicleDesc, MultipartFile imageFile) {
        if (vehicleInfo == null) {
            vehicleInfo = "";
        }
        if (vehicleDesc == null) {
            vehicleDesc = "";
        }

        // 保存到Minio
        // 检查文件名后缀
        String fileName = imageFile.getOriginalFilename();
        String extension = StringUtils.substringAfterLast(fileName, ".");
        if (StringUtils.isBlank(extension) || !CommonUtil.imageTypeCheck(extension)) {
            return false;
        }
        // 写入到Minio
        String id = SnowflakeUtil.getNext().toString();
        String objectName = "railway-vehicle/" + id + "." + extension;
        try {
            minioClient.putObject(
                    new PutObjectArgs.Builder()
                            .bucket(minioConfig.getRailwayVehicleBucket())
                            .object(objectName)
                            .stream(imageFile.getInputStream(), imageFile.getSize(), -1)
                            .contentType(imageFile.getContentType())
                            .build()
            );
        } catch (Exception e) {
            return false;
        }
        // 写入到数据库
        RailwayVehicle railwayVehicle = new RailwayVehicle();
        railwayVehicle.setId(id);
        railwayVehicle.setVehicleInfo(vehicleInfo);
        railwayVehicle.setVehicleDesc(vehicleDesc);
        railwayVehicle.setImagePath(objectName);
        return this.save(railwayVehicle);
    }

    @Override
    public PageVO<RailwayVehicleVO> getRailwayVehiclePage(RailwayVehiclePageDTO railwayVehiclePageDTO) {
        Page<RailwayVehicle> page = new Page<>(railwayVehiclePageDTO.getCurrentPage(), railwayVehiclePageDTO.getPageSize());
        LambdaQueryWrapper<RailwayVehicle> queryWrapper = new LambdaQueryWrapper<>();
        if (!Objects.equals(railwayVehiclePageDTO.getVehicleInfo(), "")) {
            queryWrapper.eq(RailwayVehicle::getVehicleInfo, railwayVehiclePageDTO.getVehicleInfo());
        }
        queryWrapper.like(RailwayVehicle::getVehicleDesc, railwayVehiclePageDTO.getVehicleDesc())
                .orderByDesc(RailwayVehicle::getCreatedAt);
        this.page(page, queryWrapper);
        List<RailwayVehicle> records = page.getRecords();
        List<RailwayVehicleVO> railwayVehicleVOS = BeanUtil.copyToList(records, RailwayVehicleVO.class);
        return new PageVO<>(page.getTotal(), page.getCurrent(), page.getSize(), railwayVehicleVOS);
    }

    @Override
    public boolean updateRailwayVehicle(String id, RailwayVehiclePageDTO railwayVehiclePageDTO) {
        String vehicleInfo = railwayVehiclePageDTO.getVehicleInfo();
        String vehicleDesc = railwayVehiclePageDTO.getVehicleDesc();
        if (vehicleDesc == null) {
            vehicleDesc = "";
        }
        if (vehicleInfo == null) {
            vehicleInfo = "";
        }
        LambdaUpdateWrapper<RailwayVehicle> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(RailwayVehicle::getId, id)
                .set(RailwayVehicle::getVehicleInfo, vehicleInfo)
                .set(RailwayVehicle::getVehicleDesc, vehicleDesc);
        return this.update(updateWrapper);
    }

    @Override
    public InputStream getRailwayVehicleImage(String id) {
        RailwayVehicle railwayVehicle = this.getById(id);
        if (railwayVehicle == null) {
            throw new ServiceException(Map.of("1", "行车记录已被删除"));
        }
        String imagePath = railwayVehicle.getImagePath();
        try {
            GetObjectResponse objectResponse = minioClient.getObject(
                    GetObjectArgs.builder()
                            .bucket(minioConfig.getRailwayVehicleBucket())
                            .object(imagePath)
                            .build()
            );
            if (objectResponse == null) {
                throw new ServiceException(Map.of("1", "行车图像不存在"));
            }
            return objectResponse;
        } catch (Exception e) {
            throw new ServiceException(Map.of("1", "获取行车图像失败"));
        }
    }
}




