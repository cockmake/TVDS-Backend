package com.tvds.newtvdsbackend.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.util.Date;

import lombok.Data;


@TableName(value = "detection_result")
@Data
public class DetectionResult implements Serializable {
    /**
     *
     */
    @TableId(type = IdType.ASSIGN_ID)
    private String id;

    /**
     * 与detection_task表中的id进行关联
     */
    private String taskId;

    /**
     * 记录车辆拍摄方向，0,1,2,3,4
     */
    private Integer direction;

    /**
     *
     */
    private Date detectionTime;

    /**
     * 该部件的定位置信度
     */
    private Double detectionConf;

    /**
     * 检测输出的结构化异常文本
     */
    private String abnormalityDesc;

    /**
     * 0非异常，1异常
     */
    private Integer isAbnormal;

    /**
     * 与component表中的id进行关联
     */
    private String componentId;

    /**
     * 记录该部件的存储路径
     */
    private String componentImagePath;

    /**
     *
     */
    private Double x1;

    /**
     *
     */
    private Double y1;

    /**
     *
     */
    private Double x2;

    /**
     *
     */
    private Double y2;

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
        DetectionResult other = (DetectionResult) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
                && (this.getTaskId() == null ? other.getTaskId() == null : this.getTaskId().equals(other.getTaskId()))
                && (this.getDirection() == null ? other.getDirection() == null : this.getDirection().equals(other.getDirection()))
                && (this.getDetectionTime() == null ? other.getDetectionTime() == null : this.getDetectionTime().equals(other.getDetectionTime()))
                && (this.getDetectionConf() == null ? other.getDetectionConf() == null : this.getDetectionConf().equals(other.getDetectionConf()))
                && (this.getAbnormalityDesc() == null ? other.getAbnormalityDesc() == null : this.getAbnormalityDesc().equals(other.getAbnormalityDesc()))
                && (this.getIsAbnormal() == null ? other.getIsAbnormal() == null : this.getIsAbnormal().equals(other.getIsAbnormal()))
                && (this.getComponentId() == null ? other.getComponentId() == null : this.getComponentId().equals(other.getComponentId()))
                && (this.getComponentImagePath() == null ? other.getComponentImagePath() == null : this.getComponentImagePath().equals(other.getComponentImagePath()))
                && (this.getX1() == null ? other.getX1() == null : this.getX1().equals(other.getX1()))
                && (this.getY1() == null ? other.getY1() == null : this.getY1().equals(other.getY1()))
                && (this.getX2() == null ? other.getX2() == null : this.getX2().equals(other.getX2()))
                && (this.getY2() == null ? other.getY2() == null : this.getY2().equals(other.getY2()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getTaskId() == null) ? 0 : getTaskId().hashCode());
        result = prime * result + ((getDirection() == null) ? 0 : getDirection().hashCode());
        result = prime * result + ((getDetectionTime() == null) ? 0 : getDetectionTime().hashCode());
        result = prime * result + ((getDetectionConf() == null) ? 0 : getDetectionConf().hashCode());
        result = prime * result + ((getAbnormalityDesc() == null) ? 0 : getAbnormalityDesc().hashCode());
        result = prime * result + ((getIsAbnormal() == null) ? 0 : getIsAbnormal().hashCode());
        result = prime * result + ((getComponentId() == null) ? 0 : getComponentId().hashCode());
        result = prime * result + ((getComponentImagePath() == null) ? 0 : getComponentImagePath().hashCode());
        result = prime * result + ((getX1() == null) ? 0 : getX1().hashCode());
        result = prime * result + ((getY1() == null) ? 0 : getY1().hashCode());
        result = prime * result + ((getX2() == null) ? 0 : getX2().hashCode());
        result = prime * result + ((getY2() == null) ? 0 : getY2().hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", taskId=").append(taskId);
        sb.append(", direction=").append(direction);
        sb.append(", detectionTime=").append(detectionTime);
        sb.append(", detectionConf=").append(detectionConf);
        sb.append(", abnormalityDesc=").append(abnormalityDesc);
        sb.append(", isAbnormal=").append(isAbnormal);
        sb.append(", componentId=").append(componentId);
        sb.append(", componentImagePath=").append(componentImagePath);
        sb.append(", x1=").append(x1);
        sb.append(", y1=").append(y1);
        sb.append(", x2=").append(x2);
        sb.append(", y2=").append(y2);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}