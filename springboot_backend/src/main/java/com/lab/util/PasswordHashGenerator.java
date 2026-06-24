package com.lab.util;

import java.security.MessageDigest;
import java.util.Base64;

/**
 * 密码哈希值生成工具
 */
public class PasswordHashGenerator {
//    public static void main(String[] args) {
//        try {
//            // 测试常用密码的哈希值
//            String[] passwords = {"admin123", "123456", "password", "test123"};
//
//            System.out.println("=== 密码哈希值生成器 ===\n");
//
//            for (String password : passwords) {
//                String hash = generateHash(password);
//                System.out.println("密码: " + password);
//                System.out.println("哈希值: " + hash);
//                System.out.println();
//            }
//
//            // 生成指定密码的哈希值
//            if (args.length > 0) {
//                String customPassword = args[0];
//                String customHash = generateHash(customPassword);
//                System.out.println("自定义密码: " + customPassword);
//                System.out.println("哈希值: " + customHash);
//            }
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
    
    /**
     * 生成密码哈希值（SHA-256 + Base64编码）
     */
    public static String generateHash(String password) throws Exception {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] hash = digest.digest(password.getBytes());
        return Base64.getEncoder().encodeToString(hash);
    }
}
