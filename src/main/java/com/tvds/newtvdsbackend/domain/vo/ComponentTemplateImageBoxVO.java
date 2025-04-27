package com.tvds.newtvdsbackend.domain.vo;


import com.tvds.newtvdsbackend.domain.dto.BoxDTO;
import lombok.Data;

import java.util.List;

@Data
public class ComponentTemplateImageBoxVO {
    private List<List<Float>> coors;
}
