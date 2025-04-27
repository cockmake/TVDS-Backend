package com.tvds.newtvdsbackend.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;


@TableName(value ="component_template_image_box")
@Data
public class ComponentTemplateImageBox implements Serializable {
    /**
     * 位置框的唯一标识，用于唯一区分不同的位置框
     */
    @TableId(type = IdType.ASSIGN_ID)
    private String id;

    /**
     * 关联的模板图片 ID，对应 template_image_info 表的 id，用于建立与模板图片的关联关系
     */
    private String templateImageId;

    /**
     * 矩形框左上角的 x 坐标，用于确定位置框在图像中水平方向的起始位置
     */
    private Float x1;

    /**
     * 矩形框左上角的 y 坐标，用于确定位置框在图像中垂直方向的起始位置
     */
    private Float y1;

    /**
     * 矩形框右下角的 x 坐标，用于确定位置框在图像中水平方向的结束位置
     */
    private Float x2;

    /**
     * 矩形框右下角的 y 坐标，用于确定位置框在图像中垂直方向的结束位置
     */
    private Float y2;

    /**
     * 记录创建时间，记录该位置框信息创建的具体时间
     */
    private Date createdAt;

    /**
     * 记录更新时间，记录该位置框信息最后一次更新的具体时间
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
        ComponentTemplateImageBox other = (ComponentTemplateImageBox) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
            && (this.getTemplateImageId() == null ? other.getTemplateImageId() == null : this.getTemplateImageId().equals(other.getTemplateImageId()))
            && (this.getX1() == null ? other.getX1() == null : this.getX1().equals(other.getX1()))
            && (this.getY1() == null ? other.getY1() == null : this.getY1().equals(other.getY1()))
            && (this.getX2() == null ? other.getX2() == null : this.getX2().equals(other.getX2()))
            && (this.getY2() == null ? other.getY2() == null : this.getY2().equals(other.getY2()))
            && (this.getCreatedAt() == null ? other.getCreatedAt() == null : this.getCreatedAt().equals(other.getCreatedAt()))
            && (this.getUpdatedAt() == null ? other.getUpdatedAt() == null : this.getUpdatedAt().equals(other.getUpdatedAt()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getTemplateImageId() == null) ? 0 : getTemplateImageId().hashCode());
        result = prime * result + ((getX1() == null) ? 0 : getX1().hashCode());
        result = prime * result + ((getY1() == null) ? 0 : getY1().hashCode());
        result = prime * result + ((getX2() == null) ? 0 : getX2().hashCode());
        result = prime * result + ((getY2() == null) ? 0 : getY2().hashCode());
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
        sb.append(", templateImageId=").append(templateImageId);
        sb.append(", x1=").append(x1);
        sb.append(", y1=").append(y1);
        sb.append(", x2=").append(x2);
        sb.append(", y2=").append(y2);
        sb.append(", createdAt=").append(createdAt);
        sb.append(", updatedAt=").append(updatedAt);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}