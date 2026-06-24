package com.lab.service;

import com.lab.entity.Adm;
import com.lab.mapper.AdmMapper;
import com.lab.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

/**
 * 管理员服务类
 */
@Service
public class AdmService {

    @Autowired
    private AdmMapper admMapper;

    /**
     * 管理员登录
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

        // 查询管理员
        Adm adm = admMapper.findByLoginInfo(username, email, parseDate(birthday));
        
        if (adm == null) {
            throw new RuntimeException("用户不存在");
        }

        // 验证密码
        String inputPasswordHash = encryptPassword(password);
        if (!inputPasswordHash.equals(adm.getPasswordHash())) {
            throw new RuntimeException("密码错误");
        }

        // 生成JWT token
        String token = JwtUtil.generateToken(adm.getId(), adm.getUsername(), "admin");

        // 返回结果
        Map<String, Object> result = new HashMap<>();
        result.put("token", token);
        result.put("userId", adm.getId());
        result.put("username", adm.getUsername());
        result.put("role", "admin");

        return result;
    }

    /**
     * 管理员注册
     * @param id 管理员ID
     * @param username 用户名
     * @param email 邮箱
     * @param birthday 生日
     * @param password 密码
     * @param adkey 管理员密钥
     */
    @Transactional
    public void register(String id, String username, String email, String birthday, String password, String adkey) {
        // 验证管理员密钥
        if (!"11111".equals(adkey)) {
            throw new RuntimeException("管理员密钥错误");
        }

        // 检查ID是否已存在
        Adm existAdm = admMapper.findById(id);
        if (existAdm != null) {
            throw new RuntimeException("该ID已注册");
        }

        // 加密密码
        String passwordHash = encryptPassword(password);

        // 创建管理员
        Adm adm = new Adm();
        adm.setId(id);
        adm.setUsername(username);
        adm.setEmail(email);
        adm.setBirthday(parseDate(birthday));
        adm.setPasswordHash(passwordHash);

        admMapper.insert(adm);
    }

    /**
     * 根据ID查询管理员信息
     * @param id 管理员ID
     * @return 管理员信息
     */
    public Adm findById(String id) {
        Adm adm = admMapper.findById(id);
        if (adm == null) {
            throw new RuntimeException("管理员不存在");
        }
        // 不返回密码哈希值
        adm.setPasswordHash(null);
        return adm;
    }

    /**
     * 更新管理员信息
     * @param id 管理员ID
     * @param username 用户名
     * @param email 邮箱
     * @param birthday 生日
     */
    @Transactional
    public void updateInfo(String id, String username, String email, String birthday) {
        Adm adm = admMapper.findById(id);
        if (adm == null) {
            throw new RuntimeException("管理员不存在");
        }

        adm.setUsername(username);
        adm.setEmail(email);
        adm.setBirthday(parseDate(birthday));

        admMapper.update(adm);
    }

    /**
     * 修改密码
     * @param id 管理员ID
     * @param oldPassword 旧密码
     * @param newPassword 新密码
     */
    @Transactional
    public void changePassword(String id, String oldPassword, String newPassword) {
        Adm adm = admMapper.findById(id);
        if (adm == null) {
            throw new RuntimeException("管理员不存在");
        }

        // 验证旧密码
        String oldPasswordHash = encryptPassword(oldPassword);
        if (!oldPasswordHash.equals(adm.getPasswordHash())) {
            throw new RuntimeException("旧密码错误");
        }

        // 更新密码
        String newPasswordHash = encryptPassword(newPassword);
        admMapper.updatePassword(id, newPasswordHash);
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
