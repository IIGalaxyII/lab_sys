package com.lab.controller;

import com.lab.common.Result;
import com.lab.entity.Equipment;
import com.lab.service.EquipmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 设备查询控制器（所有用户可访问）
 */
@RestController
@RequestMapping("/api/equipment")
@CrossOrigin
public class EquipmentController {

    @Autowired
    private EquipmentService equipmentService;

    /**
     * 查询所有设备（需要登录）
     */
    @GetMapping
    public Result<List<Equipment>> getAllEquipment(HttpServletRequest request) {
        try {
            // 只需要登录，不限制角色
            String role = (String) request.getAttribute("role");
            if (role == null || role.isEmpty()) {
                return Result.forbidden("请先登录");
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
            String role = (String) request.getAttribute("role");
            if (role == null || role.isEmpty()) {
                return Result.forbidden("请先登录");
            }

            Equipment equipment = equipmentService.findById(id);
            return Result.success(equipment);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
}
