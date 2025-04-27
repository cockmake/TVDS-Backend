package com.tvds.newtvdsbackend.domain.dto;


import com.tvds.newtvdsbackend.domain.entity.ComponentTemplateImageBox;
import lombok.Data;
import org.simpleframework.xml.core.Validate;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

@Data
public class ComponentTemplateImageBoxDTO {
    @NotNull(message = "坐标列表不能为空")
    @Valid
    private List<BoxDTO> coors;
}
