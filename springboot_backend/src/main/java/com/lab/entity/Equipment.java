package com.lab.entity;

import java.io.Serializable;

/**
 * 设备实体类
 */
public class Equipment implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 设备ID（主键）
     */
    private String id;

    /**
     * 设备名称
     */
    private String ename;

    /**
     * 设备状态: 0-可用, 1-占用, 2-维修, 3-报废
     */
    private String state;

    /**
     * 使用频率
     */
    private Float useFrequency;

    public Equipment() {
    }

    public Equipment(String id, String ename, String state, Float useFrequency) {
        this.id = id;
        this.ename = ename;
        this.state = state;
        this.useFrequency = useFrequency;
    }

    // Getters and Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEname() {
        return ename;
    }

    public void setEname(String ename) {
        this.ename = ename;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public Float getUseFrequency() {
        return useFrequency;
    }

    public void setUseFrequency(Float useFrequency) {
        this.useFrequency = useFrequency;
    }

    @Override
    public String toString() {
        return "Equipment{" +
                "id='" + id + '\'' +
                ", ename='" + ename + '\'' +
                ", state='" + state + '\'' +
                ", useFrequency=" + useFrequency +
                '}';
    }
}
