package com.tvds.newtvdsbackend.domain.entity;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 *
 * @TableName railway_vehicle
 */
@TableName(value ="railway_vehicle")
@Data
public class RailwayVehicle implements Serializable {
    /**
     *
     */
    @TableId(type = IdType.ASSIGN_ID)
    private String id;

    /**
     *
     */
    private String imagePathA;

    /**
     *
     */
    private String imagePathB;

    /**
     *
     */
    private String imagePathC;

    /**
     *
     */
    private String imagePathE;

    /**
     *
     */
    private String imagePathD;

    /**
     * 记录车次信息
     */
    private String vehicleInfo;

    /**
     * 记录辆序（车厢）
     */
    private Integer vehicleSeq;

    /**
     * 记录总辆序（车厢）数
     */
    private Integer totalSequence;

    /**
     * 记录车号信息
     */
    private String vehicleIdentity;

    /**
     * 探测站名称
     */
    private String recordStation;

    /**
     * 车辆行驶方向
     */
    private String travelDirection;

    /**
     * 担当局
     */
    private String bureau;

    /**
     * 担当段
     */
    private String section;

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
     * 客车备注信息
     */
    private String vehicleDesc;

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
        RailwayVehicle other = (RailwayVehicle) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
                && (this.getImagePathA() == null ? other.getImagePathA() == null : this.getImagePathA().equals(other.getImagePathA()))
                && (this.getImagePathB() == null ? other.getImagePathB() == null : this.getImagePathB().equals(other.getImagePathB()))
                && (this.getImagePathC() == null ? other.getImagePathC() == null : this.getImagePathC().equals(other.getImagePathC()))
                && (this.getImagePathE() == null ? other.getImagePathE() == null : this.getImagePathE().equals(other.getImagePathE()))
                && (this.getImagePathD() == null ? other.getImagePathD() == null : this.getImagePathD().equals(other.getImagePathD()))
                && (this.getVehicleInfo() == null ? other.getVehicleInfo() == null : this.getVehicleInfo().equals(other.getVehicleInfo()))
                && (this.getVehicleSeq() == null ? other.getVehicleSeq() == null : this.getVehicleSeq().equals(other.getVehicleSeq()))
                && (this.getTotalSequence() == null ? other.getTotalSequence() == null : this.getTotalSequence().equals(other.getTotalSequence()))
                && (this.getVehicleIdentity() == null ? other.getVehicleIdentity() == null : this.getVehicleIdentity().equals(other.getVehicleIdentity()))
                && (this.getRecordStation() == null ? other.getRecordStation() == null : this.getRecordStation().equals(other.getRecordStation()))
                && (this.getTravelDirection() == null ? other.getTravelDirection() == null : this.getTravelDirection().equals(other.getTravelDirection()))
                && (this.getBureau() == null ? other.getBureau() == null : this.getBureau().equals(other.getBureau()))
                && (this.getSection() == null ? other.getSection() == null : this.getSection().equals(other.getSection()))
                && (this.getIsDeleted() == null ? other.getIsDeleted() == null : this.getIsDeleted().equals(other.getIsDeleted()))
                && (this.getCreatedAt() == null ? other.getCreatedAt() == null : this.getCreatedAt().equals(other.getCreatedAt()))
                && (this.getVehicleDesc() == null ? other.getVehicleDesc() == null : this.getVehicleDesc().equals(other.getVehicleDesc()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getImagePathA() == null) ? 0 : getImagePathA().hashCode());
        result = prime * result + ((getImagePathB() == null) ? 0 : getImagePathB().hashCode());
        result = prime * result + ((getImagePathC() == null) ? 0 : getImagePathC().hashCode());
        result = prime * result + ((getImagePathE() == null) ? 0 : getImagePathE().hashCode());
        result = prime * result + ((getImagePathD() == null) ? 0 : getImagePathD().hashCode());
        result = prime * result + ((getVehicleInfo() == null) ? 0 : getVehicleInfo().hashCode());
        result = prime * result + ((getVehicleSeq() == null) ? 0 : getVehicleSeq().hashCode());
        result = prime * result + ((getTotalSequence() == null) ? 0 : getTotalSequence().hashCode());
        result = prime * result + ((getVehicleIdentity() == null) ? 0 : getVehicleIdentity().hashCode());
        result = prime * result + ((getRecordStation() == null) ? 0 : getRecordStation().hashCode());
        result = prime * result + ((getTravelDirection() == null) ? 0 : getTravelDirection().hashCode());
        result = prime * result + ((getBureau() == null) ? 0 : getBureau().hashCode());
        result = prime * result + ((getSection() == null) ? 0 : getSection().hashCode());
        result = prime * result + ((getIsDeleted() == null) ? 0 : getIsDeleted().hashCode());
        result = prime * result + ((getCreatedAt() == null) ? 0 : getCreatedAt().hashCode());
        result = prime * result + ((getVehicleDesc() == null) ? 0 : getVehicleDesc().hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", imagePathA=").append(imagePathA);
        sb.append(", imagePathB=").append(imagePathB);
        sb.append(", imagePathC=").append(imagePathC);
        sb.append(", imagePathE=").append(imagePathE);
        sb.append(", imagePathD=").append(imagePathD);
        sb.append(", vehicleInfo=").append(vehicleInfo);
        sb.append(", vehicleSeq=").append(vehicleSeq);
        sb.append(", totalSequence=").append(totalSequence);
        sb.append(", vehicleIdentity=").append(vehicleIdentity);
        sb.append(", recordStation=").append(recordStation);
        sb.append(", travelDirection=").append(travelDirection);
        sb.append(", bureau=").append(bureau);
        sb.append(", section=").append(section);
        sb.append(", isDeleted=").append(isDeleted);
        sb.append(", createdAt=").append(createdAt);
        sb.append(", vehicleDesc=").append(vehicleDesc);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}