package com.lab.controller;

import com.lab.common.Result;
import com.lab.entity.Adm;
import com.lab.service.AdmService;
import com.lab.service.NuserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * 管理员认证控制器
 */
@RestController
@RequestMapping("/api/login")
@CrossOrigin
public class AuthController {

    @Autowired
    private AdmService admService;

    @Autowired
    private NuserService nuserService;

    /**
     * 管理员登录
     */
    @PostMapping("/admin")
    public Result<Map<String, Object>> adminLogin(@RequestBody Map<String, String> loginData, HttpServletRequest request) {
        try {
            String username = loginData.get("username");
            String email = loginData.get("email");
            String birthday = loginData.get("birthday");
            String password = loginData.get("password");

            // 参数校验
            if ((username == null || username.isEmpty()) && 
                (email == null || email.isEmpty()) && 
                (birthday == null || birthday.isEmpty())) {
                return Result.error("请至少提供用户名、邮箱或生日中的一项");
            }
            if (password == null || password.isEmpty()) {
                return Result.error("密码不能为空");
            }

            Map<String, Object> result = admService.login(username, email, birthday, password);
            return Result.success("登录成功", result);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    /**
     * 普通用户登录
     */
    @PostMapping("/user")
    public Result<Map<String, Object>> userLogin(@RequestBody Map<String, String> loginData, HttpServletRequest request) {
        try {
            String username = loginData.get("username");
            String email = loginData.get("email");
            String birthday = loginData.get("birthday");
            String password = loginData.get("password");

            // 参数校验
            if ((username == null || username.isEmpty()) && 
                (email == null || email.isEmpty()) && 
                (birthday == null || birthday.isEmpty())) {
                return Result.error("请至少提供用户名、邮箱或生日中的一项");
            }
            if (password == null || password.isEmpty()) {
                return Result.error("密码不能为空");
            }

            Map<String, Object> result = nuserService.login(username, email, birthday, password);
            return Result.success("登录成功", result);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    /**
     * 管理员注册
     */
    @PostMapping("/register/admin")
    public Result<Void> adminRegister(@RequestBody Map<String, String> registerData) {
        try {
            String id = registerData.get("id");
            String username = registerData.get("username");
            String email = registerData.get("email");
            String birthday = registerData.get("birthday");
            String password = registerData.get("password");
            String adkey = registerData.get("adkey");

            // 参数校验
            if (id == null || id.isEmpty() || username == null || username.isEmpty() ||
                email == null || email.isEmpty() || birthday == null || birthday.isEmpty() ||
                password == null || password.isEmpty()) {
                return Result.error("所有字段都不能为空");
            }

            admService.register(id, username, email, birthday, password, adkey);
            return Result.success("注册成功", null);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    /**
     * 普通用户注册
     */
    @PostMapping("/register/user")
    public Result<Void> userRegister(@RequestBody Map<String, String> registerData) {
        try {
            String id = registerData.get("id");
            String username = registerData.get("username");
            String email = registerData.get("email");
            String birthday = registerData.get("birthday");
            String password = registerData.get("password");

            // 参数校验
            if (id == null || id.isEmpty() || username == null || username.isEmpty() ||
                email == null || email.isEmpty() || birthday == null || birthday.isEmpty() ||
                password == null || password.isEmpty()) {
                return Result.error("所有字段都不能为空");
            }

            nuserService.register(id, username, email, birthday, password);
            return Result.success("注册成功", null);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    /**
     * 修改密码（通用）
     */
    @PostMapping("/change-password")
    public Result<Void> changePassword(HttpServletRequest request, @RequestBody Map<String, String> data) {
        try {
            String userId = (String) request.getAttribute("userId");
            String role = (String) request.getAttribute("role");
            String oldPassword = data.get("oldPassword");
            String newPassword = data.get("newPassword");

            // 参数校验
            if (oldPassword == null || oldPassword.isEmpty() || newPassword == null || newPassword.isEmpty()) {
                return Result.error("密码不能为空");
            }

            // 根据角色调用不同的服务
            if ("admin".equals(role)) {
                admService.changePassword(userId, oldPassword, newPassword);
            } else {
                nuserService.changePassword(userId, oldPassword, newPassword);
            }

            return Result.success("密码修改成功", null);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
}