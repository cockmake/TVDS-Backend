package com.tvds.newtvdsbackend.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.io.FileUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tvds.newtvdsbackend.configuration.MinioConfig;
import com.tvds.newtvdsbackend.domain.entity.ComponentTemplateImage;
import com.tvds.newtvdsbackend.domain.vo.ComponentTemplateImageVO;
import com.tvds.newtvdsbackend.exception.ServiceException;
import com.tvds.newtvdsbackend.service.ComponentService;
import com.tvds.newtvdsbackend.service.ComponentTemplateImageService;
import com.tvds.newtvdsbackend.mapper.ComponentTemplateImageMapper;
import com.tvds.newtvdsbackend.utils.CommonUtil;
import com.tvds.newtvdsbackend.utils.SnowflakeUtil;
import io.minio.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


@Service
@RequiredArgsConstructor
public class ComponentTemplateImageServiceImpl extends ServiceImpl<ComponentTemplateImageMapper, ComponentTemplateImage>
        implements ComponentTemplateImageService {

    private final ComponentService componentService;
    private final MinioClient minioClient;
    private final MinioConfig minioConfig;


    // CURD需要确保组件是否存

    @Override
    public List<String> saveComponentTemplateImage(String componentId, MultipartFile[] imageFiles) {
        // 1. 判断组件是否存在
        if (componentService.getById(componentId) == null) {
            Map<String, String> errors = Map.of("componentId", "组件不存在");
            throw new ServiceException(errors);
        }
        // 2. 判断文件类型
        for (MultipartFile imageFile : imageFiles) {
            String fileExtension = FileUtil.extName(imageFile.getOriginalFilename());

            if (!CommonUtil.imageTypeCheck(fileExtension)) {
                Map<String, String> errors = Map.of(
                        "fileType", "文件类型不合法",
                        "fileTypeList", "仅支持" + String.join(",", CommonUtil.imageTypeList()) + "后缀的文件"
                );
                throw new ServiceException(errors);
            }
        }
        // 3. 遍历文件，保存到MinIO和数据库
        List<String> imageIdList = new ArrayList<>();
        for (MultipartFile imageFile : imageFiles) {
            // 以组件id和文件名拼接成文件名
            String id = SnowflakeUtil.getNext().toString();
            String fileExtension = FileUtil.extName(imageFile.getOriginalFilename());
            String fileName = componentId + "/" + id + "." + fileExtension;
            // 3.1 上传到MinIO
            // 3.1.1 检查桶是否存在
            try {
                boolean b = minioClient.bucketExists(
                        BucketExistsArgs.builder()
                                .bucket(minioConfig.getTemplateImageBucket())
                                .build()
                );
                if (!b) {
                    // 新建桶
                    minioClient.makeBucket(
                            MakeBucketArgs.builder()
                                    .bucket(minioConfig.getTemplateImageBucket())
                                    .build()
                    );
                }
                // 3.1.2 上传文件
                ObjectWriteResponse objectWriteResponse = minioClient.putObject(
                        PutObjectArgs.builder()
                                .bucket(minioConfig.getTemplateImageBucket())
                                .object(fileName)
                                .stream(imageFile.getInputStream(), imageFile.getSize(), -1)
                                .build()
                );
                // 3.2 保存到数据库
                if (objectWriteResponse == null) {
                    Map<String, String> errors = Map.of("file", "文件上传失败");
                    throw new ServiceException(errors);
                }
                ComponentTemplateImage componentTemplateImage = new ComponentTemplateImage();
                componentTemplateImage.setId(id);
                componentTemplateImage.setComponentId(componentId);
                componentTemplateImage.setImagePath(fileName);
                this.save(componentTemplateImage);
                imageIdList.add(id);
            } catch (Exception e) {
                Map<String, String> errors = Map.of("file", "文件上传失败");
                throw new ServiceException(errors);
            }
        }
        return imageIdList;
    }

    @Override
    public boolean removeByImageId(String componentId, String imageId) {
        LambdaQueryWrapper<ComponentTemplateImage> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ComponentTemplateImage::getComponentId, componentId)
                .eq(ComponentTemplateImage::getId, imageId);
        return this.remove(wrapper);
    }

    @Override
    public List<ComponentTemplateImageVO> getTemplateImageByComponentId(String componentId) {
        LambdaQueryWrapper<ComponentTemplateImage> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ComponentTemplateImage::getComponentId, componentId);
        List<ComponentTemplateImage> templateImages = this.list(wrapper);
        return BeanUtil.copyToList(templateImages, ComponentTemplateImageVO.class);
    }

    @Override
    public InputStream getTemplateImageStreamById(String componentId, String imageId) {
        LambdaQueryWrapper<ComponentTemplateImage> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ComponentTemplateImage::getComponentId, componentId)
                .eq(ComponentTemplateImage::getId, imageId);
        ComponentTemplateImage componentTemplateImage = this.getOne(wrapper);
        if (componentTemplateImage == null) {
            Map<String, String> errors = Map.of("file", "文件不存在");
            throw new ServiceException(errors);
        }
        String fileName = componentTemplateImage.getImagePath();
        try {
            return minioClient.getObject(
                    GetObjectArgs.builder()
                            .bucket(minioConfig.getTemplateImageBucket())
                            .object(fileName)
                            .build()
            );
        } catch (Exception e) {
            Map<String, String> errors = Map.of("file", "文件读取失败");
            throw new ServiceException(errors);
        }
    }
}




