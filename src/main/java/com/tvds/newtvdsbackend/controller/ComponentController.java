package com.tvds.newtvdsbackend.controller;

import com.tvds.newtvdsbackend.domain.dto.ComponentDTO;
import com.tvds.newtvdsbackend.domain.dto.ComponentPageDTO;
import com.tvds.newtvdsbackend.domain.enums.HttpEnums;
import com.tvds.newtvdsbackend.domain.vo.BaseResponseVO;
import com.tvds.newtvdsbackend.domain.vo.ComponentVO;
import com.tvds.newtvdsbackend.domain.vo.PageVO;
import com.tvds.newtvdsbackend.exception.ServiceException;
import com.tvds.newtvdsbackend.service.ComponentService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/component")
@RequiredArgsConstructor
public class ComponentController {
    private final ComponentService componentService;

    @PostMapping()
    public BaseResponseVO add(
            @Validated @RequestBody ComponentDTO componentDTO
    ) {
        boolean f = componentService.addNewComponent(componentDTO);
        if (f) {
            return BaseResponseVO.success("添加成功");
        } else {
            HashMap<String, String> errors = new HashMap<>();
            errors.put("1", "添加失败");
            throw new ServiceException(errors);
        }
    }

    @PutMapping("/{id}")
    public BaseResponseVO edit(
            @PathVariable String id,
            @Validated @RequestBody ComponentDTO componentDTO
    ) {
        boolean f = componentService.updateComponent(id, componentDTO);
        if (f) {
            return BaseResponseVO.success("修改成功");
        } else {
            HashMap<String, String> errors = new HashMap<>();
            errors.put("1", "修改失败");
            throw new ServiceException(errors);
        }
    }

    @PostMapping("/page")
    public BaseResponseVO page(@Validated @RequestBody ComponentPageDTO componentPageDTO) {
        PageVO<ComponentVO> pageVO = componentService.pageComponent(componentPageDTO);
        return BaseResponseVO.success(pageVO);
    }

    @DeleteMapping("/{id}")
    public BaseResponseVO delete(@PathVariable String id) {
        boolean f = componentService.deleteComponent(id);
        if (f) {
            return BaseResponseVO.success("删除成功");
        } else {
            HashMap<String, String> errors = new HashMap<>();
            errors.put("1", "删除失败");
            throw new ServiceException(errors);
        }
    }

    @GetMapping("/{id}/visual-prompt/preview")
    public ResponseEntity<InputStreamResource> getVisualPromptPreview(@PathVariable String id) throws IOException {
        // 1. 从 Service 获取图片字节数组
        byte[] imageBytes = componentService.getComponentVisualPrompt(id);

        // 2. 检查字节数组是否为空或长度为0
        if (imageBytes == null || imageBytes.length == 0) {
            // 返回错误响应
            throw new ServiceException(Map.of("1", "预览错误"));
        }

        // 3. 将字节数组包装成 ByteArrayResource
        ByteArrayResource resource = new ByteArrayResource(imageBytes);

        // 4. 构建 ResponseEntity
        return ResponseEntity.ok()
                // 设置 Content-Type 为 image/png (与 FastAPI 返回的一致)
                .contentType(MediaType.IMAGE_PNG)
                // 设置 Content-Disposition 为 inline，并指定文件名 (可选，但推荐)
                // 注意：文件名可以根据需要动态生成或使用固定值
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"preview.png\"")
                // 将资源放入响应体
                .body(new InputStreamResource(resource.getInputStream()));
    }
}
