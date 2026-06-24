package com.lab.controller;

import com.lab.common.Result;
import com.lab.entity.Log;
import com.lab.service.LogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 设备维护日志控制器（管理员功能）
 */
@RestController
@RequestMapping("/api/admin/logs")
@CrossOrigin
public class AdminLogController {

    @Autowired
    private LogService logService;

    /**
     * 查询所有设备维护日志
     */
    @GetMapping
    public Result<List<Log>> getAllLogs(HttpServletRequest request) {
        try {
            String role = (String) request.getAttribute("role");
            if (!"admin".equals(role)) {
                return Result.forbidden("无权访问");
            }

            List<Log> logs = logService.findAll();
            return Result.success(logs);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    /**
     * 根据设备ID查询日志
     */
    @GetMapping("/equipment/{equipmentId}")
    public Result<List<Log>> getLogsByEquipmentId(@PathVariable String equipmentId, HttpServletRequest request) {
        try {
            String role = (String) request.getAttribute("role");
            if (!"admin".equals(role)) {
                return Result.forbidden("无权访问");
            }

            List<Log> logs = logService.findByEquipmentId(equipmentId);
            return Result.success(logs);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    /**
     * 根据日期范围查询日志
     */
    @PostMapping("/date-range")
    public Result<List<Log>> getLogsByDateRange(@RequestBody Map<String, String> data, 
                                                 HttpServletRequest request) {
        try {
            String role = (String) request.getAttribute("role");
            if (!"admin".equals(role)) {
                return Result.forbidden("无权访问");
            }

            String startDateStr = data.get("startDate");
            String endDateStr = data.get("endDate");

            if (startDateStr == null || startDateStr.isEmpty() || 
                endDateStr == null || endDateStr.isEmpty()) {
                return Result.error("日期范围不能为空");
            }

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date startDate, endDate;
            try {
                startDate = sdf.parse(startDateStr);
                endDate = sdf.parse(endDateStr);
            } catch (ParseException e) {
                return Result.error("日期格式错误");
            }

            List<Log> logs = logService.findByDateRange(startDate, endDate);
            return Result.success(logs);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
}