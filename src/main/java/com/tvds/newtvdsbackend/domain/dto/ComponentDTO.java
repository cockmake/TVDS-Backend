package com.tvds.newtvdsbackend.domain.dto;

import lombok.Data;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotBlank;

@Data
public class ComponentDTO {
    @NotBlank(message = "部件名称不能为空")
    String componentName;
    String componentType;
    String componentDesc;
    @Range(min = 0, max = 1, message = "检测交并比范围在0到1之间")
    Float detectionIou;
    @Range(min = 0, max = 1, message = "检测置信度范围在0到1之间")
    Float detectionConf;
    String abnormalityDesc;
}
