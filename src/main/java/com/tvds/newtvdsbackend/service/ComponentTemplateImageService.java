package com.tvds.newtvdsbackend.service;

import com.tvds.newtvdsbackend.domain.entity.ComponentTemplateImage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.tvds.newtvdsbackend.domain.vo.ComponentTemplateImageVO;
import io.minio.errors.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.List;


public interface ComponentTemplateImageService extends IService<ComponentTemplateImage> {
    List<String> saveComponentTemplateImage(String componentId, MultipartFile[] imageFiles);
    boolean removeByImageId(String componentId, String imageId);
    List<ComponentTemplateImageVO> getTemplateImageByComponentId(String componentId);
    InputStream getTemplateImageStreamById(String componentId, String imageId);
}
