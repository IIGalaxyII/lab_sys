package com.lab.mapper;

import com.lab.entity.Nuser;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 普通用户Mapper接口
 */
@Mapper
public interface NuserMapper {

    /**
     * 查询所有普通用户
     * @return 用户列表
     */
    List<Nuser> findAll();

    /**
     * 根据ID查询用户
     * @param id 用户ID
     * @return 用户信息
     */
    Nuser findById(@Param("id") String id);

    /**
     * 根据用户名、邮箱或生日查询用户（用于登录）
     * @param username 用户名
     * @param email 邮箱
     * @param birthday 生日
     * @return 用户信息
     */
    Nuser findByLoginInfo(@Param("username") String username, 
                          @Param("email") String email, 
                          @Param("birthday") java.util.Date birthday);

    /**
     * 添加普通用户
     * @param nuser 用户信息
     * @return 影响行数
     */
    int insert(Nuser nuser);

    /**
     * 删除普通用户
     * @param id 用户ID
     * @return 影响行数
     */
    int deleteById(@Param("id") String id);

    /**
     * 更新用户信息
     * @param nuser 用户信息
     * @return 影响行数
     */
    int update(Nuser nuser);

    /**
     * 修改密码
     * @param id 用户ID
     * @param passwordHash 新密码哈希值
     * @return 影响行数
     */
    int updatePassword(@Param("id") String id, @Param("passwordHash") String passwordHash);
}
