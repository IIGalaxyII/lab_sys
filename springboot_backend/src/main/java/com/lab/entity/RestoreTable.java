package com.lab.entity;

import java.io.Serializable;
import java.util.Date;

/**
 * 预约表实体类
 */
public class RestoreTable implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 用户ID
     */
    private String nuserId;

    /**
     * 预约日期
     */
    private Date rdate;

    /**
     * 时间段: 0-8:00-10:00, 1-10:00-12:00, 2-14:00-17:00, 3-19:00-21:00
     */
    private String rtime;

    /**
     * 设备ID
     */
    private String equipmentId;

    /**
     * 审批状态: 0-未审核, 1-拒绝, 2-批准
     */
    private String permit;

    public RestoreTable() {
    }

    public RestoreTable(String nuserId, Date rdate, String rtime, String equipmentId, String permit) {
        this.nuserId = nuserId;
        this.rdate = rdate;
        this.rtime = rtime;
        this.equipmentId = equipmentId;
        this.permit = permit;
    }

    // Getters and Setters
    public String getNuserId() {
        return nuserId;
    }

    public void setNuserId(String nuserId) {
        this.nuserId = nuserId;
    }

    public Date getRdate() {
        return rdate;
    }

    public void setRdate(Date rdate) {
        this.rdate = rdate;
    }

    public String getRtime() {
        return rtime;
    }

    public void setRtime(String rtime) {
        this.rtime = rtime;
    }

    public String getEquipmentId() {
        return equipmentId;
    }

    public void setEquipmentId(String equipmentId) {
        this.equipmentId = equipmentId;
    }

    public String getPermit() {
        return permit;
    }

    public void setPermit(String permit) {
        this.permit = permit;
    }

    @Override
    public String toString() {
        return "RestoreTable{" +
                "nuserId='" + nuserId + '\'' +
                ", rdate=" + rdate +
                ", rtime='" + rtime + '\'' +
                ", equipmentId='" + equipmentId + '\'' +
                ", permit='" + permit + '\'' +
                '}';
    }
}
