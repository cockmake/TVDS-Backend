package com.tvds.newtvdsbackend.controller;

import com.tvds.newtvdsbackend.domain.vo.BaseResponseVO;
import com.tvds.newtvdsbackend.exception.ServiceException;
import com.tvds.newtvdsbackend.service.ComponentTemplateImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/component-template-image")
@RequiredArgsConstructor
public class ComponentTemplateImageController {
    private final ComponentTemplateImageService componentTemplateImageService;

    @PostMapping("/{componentId}")
    public BaseResponseVO addComponentTemplateImage(
            @PathVariable String componentId,
            @RequestPart MultipartFile[] imageFiles
    ) {
        List<String> imageIdList = componentTemplateImageService.saveComponentTemplateImage(componentId, imageFiles);
        return BaseResponseVO.success(Map.of("imageIdList", imageIdList));
    }

    @DeleteMapping("/{componentId}/{imageId}")
    public BaseResponseVO deleteComponentTemplateImage(
            @PathVariable String componentId,
            @PathVariable String imageId
    ) {
        boolean f = componentTemplateImageService.removeByImageId(componentId, imageId);
        if (f) {
            return BaseResponseVO.success(f);
        }
        throw new ServiceException(Map.of("1", "删除失败"));
    }

    @GetMapping("/{componentId}")
    public BaseResponseVO getComponentTemplateImage(
            @PathVariable String componentId
    ) {
        return BaseResponseVO.success(componentTemplateImageService.getTemplateImageByComponentId(componentId));
    }

    @GetMapping("/{componentId}/{imageId}")
    public ResponseEntity<InputStreamResource> getComponentTemplateImageById(
            @PathVariable String componentId,
            @PathVariable String imageId
    ) {
        InputStream inputStream = componentTemplateImageService.getTemplateImageStreamById(componentId, imageId);
        return ResponseEntity.ok()
                // 可以添加 Content-Disposition 头以下载文件
                .header("Cache-Control", "max-age=2592000")
                .contentType(MediaType.IMAGE_PNG)
                .body(new InputStreamResource(inputStream));
    }

}
