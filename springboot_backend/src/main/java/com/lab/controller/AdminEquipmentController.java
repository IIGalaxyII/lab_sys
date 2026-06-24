package com.lab.controller;

import com.lab.common.Result;
import com.lab.entity.Equipment;
import com.lab.service.EquipmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * 设备管理控制器（管理员功能）
 */
@RestController
@RequestMapping("/api/admin/equipment")
@CrossOrigin
public class AdminEquipmentController {

    @Autowired
    private EquipmentService equipmentService;

    /**
     * 查询所有设备
     */
    @GetMapping
    public Result<List<Equipment>> getAllEquipment(HttpServletRequest request) {
        try {
            // 验证权限
            String role = (String) request.getAttribute("role");
            if (!"admin".equals(role)) {
                return Result.forbidden("无权访问");
            }

            List<Equipment> equipments = equipmentService.findAll();
            return Result.success(equipments);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    /**
     * 根据ID查询设备
     */
    @GetMapping("/{id}")
    public Result<Equipment> getEquipmentById(@PathVariable String id, HttpServletRequest request) {
        try {
            // 验证权限
            String role = (String) request.getAttribute("role");
            if (!"admin".equals(role)) {
                return Result.forbidden("无权访问");
            }

            Equipment equipment = equipmentService.findById(id);
            return Result.success(equipment);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    /**
     * 更新设备状态
     */
    @PutMapping("/state")
    public Result<Void> updateEquipmentState(@RequestBody Map<String, String> data, HttpServletRequest request) {
        try {
            // 验证权限
            String role = (String) request.getAttribute("role");
            if (!"admin".equals(role)) {
                return Result.forbidden("无权访问");
            }

            String id = data.get("id");
            String state = data.get("state");

            // 参数校验
            if (id == null || id.isEmpty()) {
                return Result.error("设备ID不能为空");
            }
            if (state == null || state.isEmpty()) {
                return Result.error("状态不能为空");
            }

            equipmentService.updateState(id, state);
            
            return Result.success("状态更新成功", null);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    /**
     * 更新设备使用频率（手动触发）
     */
    @PostMapping("/update-frequency")
    public Result<Void> updateUseFrequency(HttpServletRequest request) {
        try {
            // 验证权限
            String role = (String) request.getAttribute("role");
            if (!"admin".equals(role)) {
                return Result.forbidden("无权访问");
            }

            equipmentService.updateUseFrequency();
            return Result.success("使用频率更新成功", null);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
}