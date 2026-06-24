package com.lab.controller;

import com.lab.common.Result;
import com.lab.entity.RestoreTable;
import com.lab.service.RestoreTableService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 预约管理控制器（管理员功能）
 */
@RestController
@RequestMapping("/api/admin/reservations")
@CrossOrigin
public class AdminReservationController {

    @Autowired
    private RestoreTableService restoreTableService;

    /**
     * 查询所有预约
     */
    @GetMapping
    public Result<List<RestoreTable>> getAllReservations(HttpServletRequest request) {
        try {
            // 验证权限
            String role = (String) request.getAttribute("role");
            if (!"admin".equals(role)) {
                return Result.forbidden("无权访问");
            }

            List<RestoreTable> reservations = restoreTableService.findAll();
            return Result.success(reservations);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    /**
     * 查询待审核的预约
     */
    @GetMapping("/pending")
    public Result<List<RestoreTable>> getPendingReservations(HttpServletRequest request) {
        try {
            // 验证权限
            String role = (String) request.getAttribute("role");
            if (!"admin".equals(role)) {
                return Result.forbidden("无权访问");
            }

            List<RestoreTable> reservations = restoreTableService.findPendingReservations();
            return Result.success(reservations);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    /**
     * 更新预约审批状态
     */
    @PutMapping("/permit")
    public Result<Void> updatePermit(@RequestBody Map<String, String> data, HttpServletRequest request) {
        try {
            // 验证权限
            String role = (String) request.getAttribute("role");
            if (!"admin".equals(role)) {
                return Result.forbidden("无权访问");
            }

            String nuserId = data.get("nuserId");
            String rdateStr = data.get("rdate");
            String rtime = data.get("rtime");
            String permit = data.get("permit");

            // 参数校验
            if (nuserId == null || nuserId.isEmpty()) {
                return Result.error("用户ID不能为空");
            }
            if (rdateStr == null || rdateStr.isEmpty()) {
                return Result.error("日期不能为空");
            }
            if (rtime == null || rtime.isEmpty()) {
                return Result.error("时间段不能为空");
            }
            if (permit == null || permit.isEmpty()) {
                return Result.error("审批状态不能为空");
            }

            // 解析日期
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date rdate;
            try {
                rdate = sdf.parse(rdateStr);
            } catch (ParseException e) {
                return Result.error("日期格式错误");
            }

            restoreTableService.updatePermit(nuserId, rdate, rtime, permit);
            return Result.success("审批状态更新成功", null);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
}