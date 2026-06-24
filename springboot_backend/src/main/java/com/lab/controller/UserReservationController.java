package com.lab.controller;

import com.lab.common.Result;
import com.lab.entity.RestoreTable;
import com.lab.service.RestoreTableService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 普通用户预约控制器
 */
@RestController
@RequestMapping("/api/user/reservations")
@CrossOrigin
public class UserReservationController {

    @Autowired
    private RestoreTableService restoreTableService;

    /**
     * 查看个人预约记录
     */
    @GetMapping
    public Result<List<RestoreTable>> getMyReservations(HttpServletRequest request) {
        try {
            String userId = (String) request.getAttribute("userId");
            String role = (String) request.getAttribute("role");

            if (!"user".equals(role)) {
                return Result.forbidden("无权访问");
            }

            List<RestoreTable> reservations = restoreTableService.findByUserId(userId);
            return Result.success(reservations);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    /**
     * 创建新预约
     */
    @PostMapping
    public Result<Void> createReservation(@RequestBody Map<String, String> data, HttpServletRequest request) {
        try {
            String userId = (String) request.getAttribute("userId");
            String role = (String) request.getAttribute("role");

            if (!"user".equals(role)) {
                return Result.forbidden("无权访问");
            }

            String rdateStr = data.get("rdate");
            String rtime = data.get("rtime");
            String equipmentId = data.get("equipmentId");

            // 参数校验
            if (rdateStr == null || rdateStr.isEmpty()) {
                return Result.error("日期不能为空");
            }
            if (rtime == null || rtime.isEmpty()) {
                return Result.error("时间段不能为空");
            }
            if (equipmentId == null || equipmentId.isEmpty()) {
                return Result.error("设备ID不能为空");
            }

            // 验证时间段合法性
            if (!"0".equals(rtime) && !"1".equals(rtime) && !"2".equals(rtime) && !"3".equals(rtime)) {
                return Result.error("无效的时间段");
            }

            // 解析日期
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date rdate;
            try {
                rdate = sdf.parse(rdateStr);
            } catch (ParseException e) {
                return Result.error("日期格式错误，应为 yyyy-MM-dd");
            }

            // 检查日期是否为今天或未来日期（不能预约过去的日期）
            Calendar today = Calendar.getInstance();
            today.set(Calendar.HOUR_OF_DAY, 0);
            today.set(Calendar.MINUTE, 0);
            today.set(Calendar.SECOND, 0);
            today.set(Calendar.MILLISECOND, 0);
            Calendar reservationDate = Calendar.getInstance();
            reservationDate.setTime(rdate);
            reservationDate.set(Calendar.HOUR_OF_DAY, 0);
            reservationDate.set(Calendar.MINUTE, 0);
            reservationDate.set(Calendar.SECOND, 0);
            reservationDate.set(Calendar.MILLISECOND, 0);
            if (reservationDate.before(today)) {
                return Result.error("不能预约过去的日期");
            }

            restoreTableService.createReservation(userId, rdate, rtime, equipmentId);
            return Result.success("预约申请已提交，等待管理员审核", null);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    /**
     * 取消预约（仅未审核状态可取消）
     */
    @DeleteMapping("/{rdate}/{rtime}/{equipmentId}")
    public Result<Void> cancelReservation(@PathVariable String rdate,
                                          @PathVariable String rtime,
                                          @PathVariable String equipmentId,
                                          HttpServletRequest request) {
        try {
            String userId = (String) request.getAttribute("userId");
            String role = (String) request.getAttribute("role");

            if (!"user".equals(role)) {
                return Result.forbidden("无权访问");
            }

            // 解析日期
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date date;
            try {
                date = sdf.parse(rdate);
            } catch (ParseException e) {
                return Result.error("日期格式错误");
            }

            restoreTableService.cancelReservation(userId, date, rtime);
            return Result.success("预约已取消", null);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
}
