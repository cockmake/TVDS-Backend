package com.tvds.newtvdsbackend.controller;

import com.tvds.newtvdsbackend.domain.dto.ComponentTemplateImageBoxDTO;
import com.tvds.newtvdsbackend.domain.vo.BaseResponseVO;
import com.tvds.newtvdsbackend.exception.ServiceException;
import com.tvds.newtvdsbackend.service.ComponentTemplateImageBoxService;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/component-template-image-box")
@RequiredArgsConstructor
public class ComponentTemplateImageBoxController {
    private final ComponentTemplateImageBoxService componentTemplateImageBoxService;

    @PutMapping("/{templateImageId}")
    public BaseResponseVO removeAndSave(
            @PathVariable String templateImageId,
            @RequestBody @Validated ComponentTemplateImageBoxDTO componentTemplateImageBoxDTO

    ) {
        boolean f = componentTemplateImageBoxService.removeAndSave(templateImageId, componentTemplateImageBoxDTO);
        if (f){
            return BaseResponseVO.success("保存成功");
        }
        throw new ServiceException(Map.of("1", "保存失败"));
    }

    @GetMapping("/{templateImageId}")
    public BaseResponseVO getComponentTemplateImageBox(
            @PathVariable String templateImageId
    ) {
        return BaseResponseVO.success(componentTemplateImageBoxService.getComponentTemplateImageBox(templateImageId));
    }
}
