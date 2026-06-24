package com.lab.service;

import com.lab.entity.Log;
import com.lab.mapper.LogMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * 设备维护日志服务类
 */
@Service
public class LogService {

    @Autowired
    private LogMapper logMapper;

    /**
     * 添加设备维护日志
     * @param equipmentId 设备ID
     * @param state 设备状态
     */
    @Transactional
    public void addLog(String equipmentId, String state) {
        Log log = new Log(equipmentId, new Date(), state);
        logMapper.insert(log);
    }

    /**
     * 查询所有日志
     * @return 日志列表
     */
    public List<Log> findAll() {
        List<Log> logs = logMapper.findAll();
        for (Log log : logs) {
            log.setStateDesc(getStateDescription(log.getState()));
        }
        return logs;
    }

    /**
     * 根据设备ID查询日志
     * @param equipmentId 设备ID
     * @return 日志列表
     */
    public List<Log> findByEquipmentId(String equipmentId) {
        List<Log> logs = logMapper.findByEquipmentId(equipmentId);
        for (Log log : logs) {
            log.setStateDesc(getStateDescription(log.getState()));
        }
        return logs;
    }

    /**
     * 根据日期范围查询日志
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @return 日志列表
     */
    public List<Log> findByDateRange(Date startDate, Date endDate) {
        List<Log> logs = logMapper.findByDateRange(startDate, endDate);
        for (Log log : logs) {
            log.setStateDesc(getStateDescription(log.getState()));
        }
        return logs;
    }

    /**
     * 获取状态描述
     * @param state 状态码
     * @return 状态描述
     */
    private String getStateDescription(String state) {
        if (state == null) {
            return "未知";
        }
        switch (state) {
            case "0":
                return "维修";
            case "1":
                return "报废";
            case "2":
                return "正常";
            default:
                return "未知";
        }
    }
}