package com.lab.controller;

import com.lab.common.Result;
import com.lab.entity.Adm;
import com.lab.service.AdmService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * 管理员个人信息控制器
 */
@RestController
@RequestMapping("/api/admin/profile")
@CrossOrigin
public class AdminProfileController {

    @Autowired
    private AdmService admService;

    /**
     * 获取管理员个人信息
     */
    @GetMapping
    public Result<Adm> getProfile(HttpServletRequest request) {
        try {
            String userId = (String) request.getAttribute("userId");
            String role = (String) request.getAttribute("role");

            if (!"admin".equals(role)) {
                return Result.forbidden("无权访问");
            }

            Adm adm = admService.findById(userId);
            return Result.success(adm);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    /**
     * 更新管理员个人信息
     */
    @PutMapping
    public Result<Void> updateProfile(@RequestBody Map<String, String> data, HttpServletRequest request) {
        try {
            String userId = (String) request.getAttribute("userId");
            String role = (String) request.getAttribute("role");

            if (!"admin".equals(role)) {
                return Result.forbidden("无权访问");
            }

            String username = data.get("username");
            String email = data.get("email");
            String birthday = data.get("birthday");

            // 参数校验
            if (username == null || username.isEmpty()) {
                return Result.error("用户名不能为空");
            }
            if (email == null || email.isEmpty()) {
                return Result.error("邮箱不能为空");
            }
            if (birthday == null || birthday.isEmpty()) {
                return Result.error("生日不能为空");
            }

            admService.updateInfo(userId, username, email, birthday);
            return Result.success("信息更新成功", null);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    /**
     * 修改密码
     */
    @PostMapping("/change-password")
    public Result<Void> changePassword(@RequestBody Map<String, String> data, HttpServletRequest request) {
        try {
            String userId = (String) request.getAttribute("userId");
            String role = (String) request.getAttribute("role");

            if (!"admin".equals(role)) {
                return Result.forbidden("无权访问");
            }

            String oldPassword = data.get("oldPassword");
            String newPassword = data.get("newPassword");

            // 参数校验
            if (oldPassword == null || oldPassword.isEmpty()) {
                return Result.error("旧密码不能为空");
            }
            if (newPassword == null || newPassword.isEmpty()) {
                return Result.error("新密码不能为空");
            }

            admService.changePassword(userId, oldPassword, newPassword);
            return Result.success("密码修改成功", null);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
}