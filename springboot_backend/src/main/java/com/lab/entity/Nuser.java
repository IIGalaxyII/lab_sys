package com.lab.entity;

import java.io.Serializable;
import java.util.Date;

/**
 * 普通用户实体类
 */
public class Nuser implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 学号（主键）
     */
    private String id;

    /**
     * 用户名
     */
    private String username;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 生日
     */
    private Date birthday;

    /**
     * 密码哈希值
     */
    private String passwordHash;

    public Nuser() {
    }

    public Nuser(String id, String username, String email, Date birthday, String passwordHash) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.birthday = birthday;
        this.passwordHash = passwordHash;
    }

    // Getters and Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    @Override
    public String toString() {
        return "Nuser{" +
                "id='" + id + '\'' +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", birthday=" + birthday +
                '}';
    }
}
