package com.tvds.newtvdsbackend.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tvds.newtvdsbackend.configuration.MinioConfig;
import com.tvds.newtvdsbackend.domain.dto.RailwayVehicleFormDTO;
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
import java.util.*;


@Service
@RequiredArgsConstructor
public class RailwayVehicleServiceImpl extends ServiceImpl<RailwayVehicleMapper, RailwayVehicle>
        implements RailwayVehicleService {

    private final MinioConfig minioConfig;
    private final MinioClient minioClient;
    private final RailwayVehicleMapper railwayVehicleMapper;

    @Override
    public boolean addRailwayVehicle(
            String recordStation,
            String travelDirection,
            String vehicleInfo,
            String vehicleIdentity,
            String bureau,
            String section,
            String vehicleDesc,
            String vehicleSeq,
            String totalSequence,
            MultipartFile[] imageFiles
    ) {
        // 必须要有5个图片
        if (imageFiles == null || imageFiles.length < 5) {
            throw new ServiceException(Map.of("1", "至少需要上传5个方位的行车图像"));
        }
        List<String> fileNames = new ArrayList<>();
        for (MultipartFile imageFile : imageFiles) {
            // 检查文件是否为空
            if (imageFile.isEmpty()) {
                throw new ServiceException(Map.of("1", "行车图像不能为空"));
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
                                .build()
                );
            } catch (Exception e) {
                System.out.println("上传行车图像失败: " + e.getMessage());
                return false;
            }
            // 添加到文件名列表
            fileNames.add(objectName);
        }
        // 写入到数据库
        String railwayVehicleId = SnowflakeUtil.getNext().toString();
        RailwayVehicle railwayVehicle = new RailwayVehicle();
        railwayVehicle.setId(railwayVehicleId);
        railwayVehicle.setRecordStation(recordStation == null ? "" : recordStation);
        railwayVehicle.setTravelDirection(travelDirection == null ? "" : travelDirection);
        railwayVehicle.setVehicleInfo(vehicleInfo == null ? "" : vehicleInfo);
        railwayVehicle.setVehicleIdentity(vehicleIdentity == null ? "" : vehicleIdentity);
        railwayVehicle.setBureau(bureau == null ? "" : bureau);
        railwayVehicle.setSection(section == null ? "" : section);
        railwayVehicle.setVehicleDesc(vehicleDesc == null ? "" : vehicleDesc);
        railwayVehicle.setImagePathA(fileNames.get(0));
        railwayVehicle.setImagePathB(fileNames.get(1));
        railwayVehicle.setImagePathC(fileNames.get(2));
        railwayVehicle.setImagePathD(fileNames.get(3));
        railwayVehicle.setImagePathE(fileNames.get(4));

        railwayVehicle.setVehicleSeq(vehicleSeq == null ? 1 : Integer.parseInt(vehicleSeq));
        railwayVehicle.setTotalSequence(totalSequence == null ? 1 : Integer.parseInt(totalSequence));
        return this.save(railwayVehicle);
    }

    @Override
    public PageVO<RailwayVehicleVO> getRailwayVehiclePage(RailwayVehiclePageDTO railwayVehiclePageDTO) {
        Page<RailwayVehicleVO> page = new Page<>(railwayVehiclePageDTO.getCurrentPage(), railwayVehiclePageDTO.getPageSize());
        railwayVehicleMapper.getRailwayVehiclePage(page, railwayVehiclePageDTO);
        return new PageVO<>(page.getTotal(), page.getCurrent(), page.getSize(), page.getRecords());
    }

    @Override
    public boolean updateRailwayVehicle(String id, RailwayVehicleFormDTO railwayVehicleFormDTO) {
        LambdaUpdateWrapper<RailwayVehicle> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(RailwayVehicle::getId, id)
                .set(RailwayVehicle::getRecordStation, railwayVehicleFormDTO.getRecordStation())
                .set(RailwayVehicle::getTravelDirection, railwayVehicleFormDTO.getTravelDirection())
                .set(RailwayVehicle::getVehicleInfo, railwayVehicleFormDTO.getVehicleInfo())
                .set(RailwayVehicle::getVehicleIdentity, railwayVehicleFormDTO.getVehicleIdentity())
                .set(RailwayVehicle::getBureau, railwayVehicleFormDTO.getBureau())
                .set(RailwayVehicle::getSection, railwayVehicleFormDTO.getSection())
                .set(RailwayVehicle::getVehicleDesc, railwayVehicleFormDTO.getVehicleDesc());
        return this.update(updateWrapper);
    }

    @Override
    public InputStream getRailwayVehicleImage(String id, Integer direction) {
        RailwayVehicle railwayVehicle = this.getById(id);
        if (railwayVehicle == null) {
            throw new ServiceException(Map.of("1", "行车记录已被删除"));
        }
        String imagePath = "";
        switch (direction) {
            case 0:
                imagePath = railwayVehicle.getImagePathA();
                break;
            case 1:
                imagePath = railwayVehicle.getImagePathB();
                break;
            case 2:
                imagePath = railwayVehicle.getImagePathC();
                break;
            case 3:
                imagePath = railwayVehicle.getImagePathD();
                break;
            case 4:
                imagePath = railwayVehicle.getImagePathE();
                break;
            default:
                throw new ServiceException(Map.of("1", "无效的行车图像方向"));
        }
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

    @Override
    public List<String> getVehicleInfoOptions(Date startDate, Date endDate) {
        // 聚类出vehicleInfo
        LambdaQueryWrapper<RailwayVehicle> queryWrapper = new LambdaQueryWrapper<>();
        if (startDate == null || endDate == null) {
            queryWrapper.select(RailwayVehicle::getVehicleInfo)
                    .groupBy(RailwayVehicle::getVehicleInfo);
        } else {
            queryWrapper.select(RailwayVehicle::getVehicleInfo)
                    .between(RailwayVehicle::getCreatedAt, startDate, endDate)
                    .groupBy(RailwayVehicle::getVehicleInfo);
        }
        List<RailwayVehicle> railwayVehicles = this.list(queryWrapper);
        List<String> vehicleInfoOptions = new ArrayList<>();
        for (RailwayVehicle railwayVehicle : railwayVehicles) {
            String vehicleInfo = railwayVehicle.getVehicleInfo();
            vehicleInfoOptions.add(vehicleInfo);
        }
        return vehicleInfoOptions;
    }
}
