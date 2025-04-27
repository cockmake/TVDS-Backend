package com.tvds.newtvdsbackend.domain.vo;

import lombok.Data;

@Data
public class ComponentTemplateImageVO {
    /**
     * 模板图片的唯一标识，用于唯一区分不同的模板图片
     */
    private String id;

    /**
     * 关联的零部件 ID，对应 component_info 表的 id，用于建立与零部件的关联关系
     */
    private String componentId;
}
