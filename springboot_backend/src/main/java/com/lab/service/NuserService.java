package com.lab.service;

import com.lab.entity.Nuser;
import com.lab.mapper.NuserMapper;
import com.lab.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 普通用户服务类（管理员管理功能）
 */
@Service
public class NuserService {

    @Autowired
    private NuserMapper nuserMapper;

    /**
     * 查询所有普通用户（管理员功能）
     * @return 用户列表
     */
    public List<Nuser> findAll() {
        List<Nuser> users = nuserMapper.findAll();
        // 不返回密码哈希值
        users.forEach(user -> user.setPasswordHash(null));
        return users;
    }

    /**
     * 根据ID查询用户（管理员功能）
     * @param id 用户ID
     * @return 用户信息
     */
    public Nuser findById(String id) {
        Nuser nuser = nuserMapper.findById(id);
        if (nuser == null) {
            throw new RuntimeException("用户不存在");
        }
        nuser.setPasswordHash(null);
        return nuser;
    }

    /**
     * 添加普通用户（管理员功能）
     * @param nuser 用户信息
     */
    @Transactional
    public void addUser(Nuser nuser) {
        // 检查ID是否已存在
        Nuser existUser = nuserMapper.findById(nuser.getId());
        if (existUser != null) {
            throw new RuntimeException("该学号已存在");
        }

        // 加密密码
        String passwordHash = encryptPassword("123456"); // 默认密码
        nuser.setPasswordHash(passwordHash);

        nuserMapper.insert(nuser);
    }

    /**
     * 删除普通用户（管理员功能）
     * @param id 用户ID
     */
    @Transactional
    public void deleteUser(String id) {
        Nuser nuser = nuserMapper.findById(id);
        if (nuser == null) {
            throw new RuntimeException("用户不存在");
        }

        nuserMapper.deleteById(id);
    }

    /**
     * 更新用户信息（管理员功能）
     * @param nuser 用户信息
     */
    @Transactional
    public void updateUser(Nuser nuser) {
        Nuser existUser = nuserMapper.findById(nuser.getId());
        if (existUser == null) {
            throw new RuntimeException("用户不存在");
        }

        nuserMapper.update(nuser);
    }

    /**
     * 普通用户登录
     * @param username 用户名
     * @param email 邮箱
     * @param birthday 生日
     * @param password 密码
     * @return 登录结果（包含token）
     */
    public Map<String, Object> login(String username, String email, String birthday, String password) {
        // 至少需要一个身份标识条件
        if ((username == null || username.isEmpty()) &&
            (email == null || email.isEmpty()) &&
            (birthday == null || birthday.isEmpty())) {
            throw new RuntimeException("请至少提供用户名、邮箱或生日中的一项");
        }

        // 查询用户
        Nuser nuser = nuserMapper.findByLoginInfo(username, email, parseDate(birthday));
        
        if (nuser == null) {
            throw new RuntimeException("用户不存在");
        }

        // 验证密码
        String inputPasswordHash = encryptPassword(password);
        if (!inputPasswordHash.equals(nuser.getPasswordHash())) {
            throw new RuntimeException("密码错误");
        }

        // 生成JWT token
        String token = JwtUtil.generateToken(nuser.getId(), nuser.getUsername(), "user");

        // 返回结果
        Map<String, Object> result = new HashMap<>();
        result.put("token", token);
        result.put("userId", nuser.getId());
        result.put("username", nuser.getUsername());
        result.put("role", "user");

        return result;
    }

    /**
     * 普通用户注册
     * @param id 用户ID
     * @param username 用户名
     * @param email 邮箱
     * @param birthday 生日
     * @param password 密码
     */
    @Transactional
    public void register(String id, String username, String email, String birthday, String password) {
        // 检查ID是否已存在
        Nuser existUser = nuserMapper.findById(id);
        if (existUser != null) {
            throw new RuntimeException("该ID已注册");
        }

        // 加密密码
        String passwordHash = encryptPassword(password);

        // 创建用户
        Nuser nuser = new Nuser();
        nuser.setId(id);
        nuser.setUsername(username);
        nuser.setEmail(email);
        nuser.setBirthday(parseDate(birthday));
        nuser.setPasswordHash(passwordHash);

        nuserMapper.insert(nuser);
    }

    /**
     * 更新个人信息
     * @param id 用户ID
     * @param username 用户名
     * @param email 邮箱
     * @param birthday 生日
     */
    @Transactional
    public void updateInfo(String id, String username, String email, String birthday) {
        Nuser nuser = nuserMapper.findById(id);
        if (nuser == null) {
            throw new RuntimeException("用户不存在");
        }

        nuser.setUsername(username);
        nuser.setEmail(email);
        nuser.setBirthday(parseDate(birthday));

        nuserMapper.update(nuser);
    }

    /**
     * 修改密码
     * @param id 用户ID
     * @param oldPassword 旧密码
     * @param newPassword 新密码
     */
    @Transactional
    public void changePassword(String id, String oldPassword, String newPassword) {
        Nuser nuser = nuserMapper.findById(id);
        if (nuser == null) {
            throw new RuntimeException("用户不存在");
        }

        // 验证旧密码
        String oldPasswordHash = encryptPassword(oldPassword);
        if (!oldPasswordHash.equals(nuser.getPasswordHash())) {
            throw new RuntimeException("旧密码错误");
        }

        // 更新密码
        String newPasswordHash = encryptPassword(newPassword);
        nuserMapper.updatePassword(id, newPasswordHash);
    }

    /**
     * 密码加密（SHA-256）
     * @param password 明文密码
     * @return 加密后的密码哈希值
     */
    private String encryptPassword(String password) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(password.getBytes());
            return Base64.getEncoder().encodeToString(hash);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("密码加密失败");
        }
    }

    /**
     * 解析日期字符串
     * @param dateStr 日期字符串（yyyy-MM-dd），可为null
     * @return Date对象，null如果输入为空
     */
    private java.util.Date parseDate(String dateStr) {
        if (dateStr == null || dateStr.isEmpty()) {
            return null;
        }
        try {
            java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd");
            sdf.setLenient(false);
            return sdf.parse(dateStr);
        } catch (Exception e) {
            throw new RuntimeException("日期格式错误，应为 yyyy-MM-dd");
        }
    }
}
