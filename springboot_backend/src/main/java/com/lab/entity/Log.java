package com.lab.entity;

import java.io.Serializable;
import java.util.Date;

/**
 * 设备维护日志实体类
 */
public class Log implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 设备ID
     */
    private String equipmentId;

    /**
     * 日志日期（设备更改状态的时间）
     */
    private Date logDate;

    /**
     * 设备状态: 0-维修, 1-报废, 2-正常
     */
    private String state;

    /**
     * 设备名称（用于显示）
     */
    private String equipmentName;

    /**
     * 状态描述（用于显示）
     */
    private String stateDesc;

    public Log() {
    }

    public Log(String equipmentId, Date logDate, String state) {
        this.equipmentId = equipmentId;
        this.logDate = logDate;
        this.state = state;
    }

    public String getEquipmentId() {
        return equipmentId;
    }

    public void setEquipmentId(String equipmentId) {
        this.equipmentId = equipmentId;
    }

    public Date getLogDate() {
        return logDate;
    }

    public void setLogDate(Date logDate) {
        this.logDate = logDate;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getEquipmentName() {
        return equipmentName;
    }

    public void setEquipmentName(String equipmentName) {
        this.equipmentName = equipmentName;
    }

    public String getStateDesc() {
        return stateDesc;
    }

    public void setStateDesc(String stateDesc) {
        this.stateDesc = stateDesc;
    }

    @Override
    public String toString() {
        return "Log{" +
                "equipmentId='" + equipmentId + '\'' +
                ", logDate=" + logDate +
                ", state='" + state + '\'' +
                ", equipmentName='" + equipmentName + '\'' +
                ", stateDesc='" + stateDesc + '\'' +
                '}';
    }
}