package com.lab.controller;

import com.lab.common.Result;
import com.lab.entity.Nuser;
import com.lab.service.NuserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 用户管理控制器（管理员功能）
 */
@RestController
@RequestMapping("/api/admin/users")
@CrossOrigin
public class AdminUserController {

    @Autowired
    private NuserService nuserService;

    /**
     * 查询所有普通用户
     */
    @GetMapping
    public Result<List<Nuser>> getAllUsers(HttpServletRequest request) {
        try {
            // 验证权限
            String role = (String) request.getAttribute("role");
            if (!"admin".equals(role)) {
                return Result.forbidden("无权访问");
            }

            List<Nuser> users = nuserService.findAll();
            return Result.success(users);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    /**
     * 根据ID查询用户
     */
    @GetMapping("/{id}")
    public Result<Nuser> getUserById(@PathVariable String id, HttpServletRequest request) {
        try {
            // 验证权限
            String role = (String) request.getAttribute("role");
            if (!"admin".equals(role)) {
                return Result.forbidden("无权访问");
            }

            Nuser user = nuserService.findById(id);
            return Result.success(user);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    /**
     * 添加普通用户
     */
    @PostMapping
    public Result<Void> addUser(@RequestBody Nuser nuser, HttpServletRequest request) {
        try {
            // 验证权限
            String role = (String) request.getAttribute("role");
            if (!"admin".equals(role)) {
                return Result.forbidden("无权访问");
            }

            // 参数校验
            if (nuser.getId() == null || nuser.getId().isEmpty()) {
                return Result.error("学号不能为空");
            }
            if (nuser.getUsername() == null || nuser.getUsername().isEmpty()) {
                return Result.error("用户名不能为空");
            }
            if (nuser.getEmail() == null || nuser.getEmail().isEmpty()) {
                return Result.error("邮箱不能为空");
            }
            if (nuser.getBirthday() == null) {
                return Result.error("生日不能为空");
            }

            nuserService.addUser(nuser);
            return Result.success("添加成功", null);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    /**
     * 删除普通用户
     */
    @DeleteMapping("/{id}")
    public Result<Void> deleteUser(@PathVariable String id, HttpServletRequest request) {
        try {
            // 验证权限
            String role = (String) request.getAttribute("role");
            if (!"admin".equals(role)) {
                return Result.forbidden("无权访问");
            }

            nuserService.deleteUser(id);
            return Result.success("删除成功", null);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    /**
     * 更新用户信息
     */
    @PutMapping
    public Result<Void> updateUser(@RequestBody Nuser nuser, HttpServletRequest request) {
        try {
            // 验证权限
            String role = (String) request.getAttribute("role");
            if (!"admin".equals(role)) {
                return Result.forbidden("无权访问");
            }

            // 参数校验
            if (nuser.getId() == null || nuser.getId().isEmpty()) {
                return Result.error("学号不能为空");
            }

            nuserService.updateUser(nuser);
            return Result.success("更新成功", null);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
}