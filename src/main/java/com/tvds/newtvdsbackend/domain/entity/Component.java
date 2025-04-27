package com.tvds.newtvdsbackend.domain.entity;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;


@TableName(value ="component")
@Data
public class Component implements Serializable {
    /**
     * 
     */
    @TableId(type = IdType.ASSIGN_ID)
    private String id;

    /**
     * 部件名称
     */
    private String componentName;

    /**
     * 部件车型
     */
    private String componentType;

    /**
     * 部件备注
     */
    private String componentDesc;

    /**
     * 检测交并比
     */
    private Double detectionIou;

    /**
     * 检测置信度
     */
    private Double detectionConf;

    /**
     * 异常描述文本
     */
    private String abnormalityDesc;

    /**
     * 逻辑删除标识，0 表示未删除，1 表示已删除
     */
    @TableLogic
    private Integer isDeleted;

    /**
     * 记录创建时间
     */
    private Date createdAt;

    /**
     * 记录更新时间
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
        Component other = (Component) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
            && (this.getComponentName() == null ? other.getComponentName() == null : this.getComponentName().equals(other.getComponentName()))
            && (this.getComponentType() == null ? other.getComponentType() == null : this.getComponentType().equals(other.getComponentType()))
            && (this.getComponentDesc() == null ? other.getComponentDesc() == null : this.getComponentDesc().equals(other.getComponentDesc()))
            && (this.getDetectionIou() == null ? other.getDetectionIou() == null : this.getDetectionIou().equals(other.getDetectionIou()))
            && (this.getDetectionConf() == null ? other.getDetectionConf() == null : this.getDetectionConf().equals(other.getDetectionConf()))
            && (this.getAbnormalityDesc() == null ? other.getAbnormalityDesc() == null : this.getAbnormalityDesc().equals(other.getAbnormalityDesc()))
            && (this.getIsDeleted() == null ? other.getIsDeleted() == null : this.getIsDeleted().equals(other.getIsDeleted()))
            && (this.getCreatedAt() == null ? other.getCreatedAt() == null : this.getCreatedAt().equals(other.getCreatedAt()))
            && (this.getUpdatedAt() == null ? other.getUpdatedAt() == null : this.getUpdatedAt().equals(other.getUpdatedAt()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getComponentName() == null) ? 0 : getComponentName().hashCode());
        result = prime * result + ((getComponentType() == null) ? 0 : getComponentType().hashCode());
        result = prime * result + ((getComponentDesc() == null) ? 0 : getComponentDesc().hashCode());
        result = prime * result + ((getDetectionIou() == null) ? 0 : getDetectionIou().hashCode());
        result = prime * result + ((getDetectionConf() == null) ? 0 : getDetectionConf().hashCode());
        result = prime * result + ((getAbnormalityDesc() == null) ? 0 : getAbnormalityDesc().hashCode());
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
        sb.append(", componentName=").append(componentName);
        sb.append(", componentType=").append(componentType);
        sb.append(", componentDesc=").append(componentDesc);
        sb.append(", detectionIou=").append(detectionIou);
        sb.append(", detectionConf=").append(detectionConf);
        sb.append(", abnormalityDesc=").append(abnormalityDesc);
        sb.append(", isDeleted=").append(isDeleted);
        sb.append(", createdAt=").append(createdAt);
        sb.append(", updatedAt=").append(updatedAt);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}