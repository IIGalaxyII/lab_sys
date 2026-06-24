package com.lab.mapper;

import com.lab.entity.Adm;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 管理员Mapper接口
 */
@Mapper
public interface AdmMapper {

    /**
     * 根据ID查询管理员
     * @param id 管理员ID
     * @return 管理员信息
     */
    Adm findById(@Param("id") String id);

    /**
     * 根据用户名、邮箱或生日查询管理员（用于登录）
     * @param username 用户名
     * @param email 邮箱
     * @param birthday 生日
     * @return 管理员信息
     */
    Adm findByLoginInfo(@Param("username") String username, 
                        @Param("email") String email, 
                        @Param("birthday") java.util.Date birthday);

    /**
     * 添加管理员
     * @param adm 管理员信息
     * @return 影响行数
     */
    int insert(Adm adm);

    /**
     * 更新管理员信息
     * @param adm 管理员信息
     * @return 影响行数
     */
    int update(Adm adm);

    /**
     * 修改密码
     * @param id 管理员ID
     * @param passwordHash 新密码哈希值
     * @return 影响行数
     */
    int updatePassword(@Param("id") String id, @Param("passwordHash") String passwordHash);
}
