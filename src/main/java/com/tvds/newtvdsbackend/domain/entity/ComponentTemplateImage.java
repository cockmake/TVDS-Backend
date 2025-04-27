package com.tvds.newtvdsbackend.domain.entity;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;


@TableName(value ="component_template_image")
@Data
public class ComponentTemplateImage implements Serializable {
    /**
     * 模板图片的唯一标识，用于唯一区分不同的模板图片
     */
    @TableId(type = IdType.ASSIGN_ID)
    private String id;

    /**
     * 关联的零部件 ID，对应 component_info 表的 id，用于建立与零部件的关联关系
     */
    private String componentId;

    /**
     * 模板图片在 Minio 对象存储中的路径，用于定位模板图片
     */
    private String imagePath;

    /**
     * 逻辑删除标识，0 表示未删除，1 表示已删除
     */
    @TableLogic
    private Integer isDeleted;

    /**
     * 记录创建时间，记录该模板图片信息创建的具体时间
     */
    private Date createdAt;

    /**
     * 记录更新时间，记录该模板图片信息最后一次更新的具体时间
     */
    private Date updatedAt;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

    @Override
    public boolean equals(Object that) {
        if (this == that) {
            return true;
        }
        if (that == null) {
            return false;
        }
        if (getClass() != that.getClass()) {
            return false;
        }
        ComponentTemplateImage other = (ComponentTemplateImage) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
            && (this.getComponentId() == null ? other.getComponentId() == null : this.getComponentId().equals(other.getComponentId()))
            && (this.getImagePath() == null ? other.getImagePath() == null : this.getImagePath().equals(other.getImagePath()))
            && (this.getIsDeleted() == null ? other.getIsDeleted() == null : this.getIsDeleted().equals(other.getIsDeleted()))
            && (this.getCreatedAt() == null ? other.getCreatedAt() == null : this.getCreatedAt().equals(other.getCreatedAt()))
            && (this.getUpdatedAt() == null ? other.getUpdatedAt() == null : this.getUpdatedAt().equals(other.getUpdatedAt()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getComponentId() == null) ? 0 : getComponentId().hashCode());
        result = prime * result + ((getImagePath() == null) ? 0 : getImagePath().hashCode());
        result = prime * result + ((getIsDeleted() == null) ? 0 : getIsDeleted().hashCode());
        result = prime * result + ((getCreatedAt() == null) ? 0 : getCreatedAt().hashCode());
        result = prime * result + ((getUpdatedAt() == null) ? 0 : getUpdatedAt().hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", componentId=").append(componentId);
        sb.append(", imagePath=").append(imagePath);
        sb.append(", isDeleted=").append(isDeleted);
        sb.append(", createdAt=").append(createdAt);
        sb.append(", updatedAt=").append(updatedAt);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}