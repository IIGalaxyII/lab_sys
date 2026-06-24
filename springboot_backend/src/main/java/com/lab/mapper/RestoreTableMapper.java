package com.lab.mapper;

import com.lab.entity.RestoreTable;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

/**
 * 预约表Mapper接口
 */
@Mapper
public interface RestoreTableMapper {

    /**
     * 查询所有预约记录
     * @return 预约列表
     */
    List<RestoreTable> findAll();

    /**
     * 根据用户ID查询预约
     * @param nuserId 用户ID
     * @return 预约列表
     */
    List<RestoreTable> findByUserId(@Param("nuserId") String nuserId);

    /**
     * 根据设备ID查询预约
     * @param equipmentId 设备ID
     * @return 预约列表
     */
    List<RestoreTable> findByEquipmentId(@Param("equipmentId") String equipmentId);

    /**
     * 检查预约冲突（同一设备、同一日期、同一时间段）
     * @param rdate 预约日期
     * @param rtime 时间段
     * @param equipmentId 设备ID
     * @return 冲突的预约记录
     */
    RestoreTable checkConflict(@Param("rdate") Date rdate,
                               @Param("rtime") String rtime,
                               @Param("equipmentId") String equipmentId);

    /**
     * 检查用户时间冲突（同一用户、同一日期、同一时间段）
     * @param nuserId 用户ID
     * @param rdate 预约日期
     * @param rtime 时间段
     * @return 冲突的预约记录
     */
    RestoreTable checkUserTimeConflict(@Param("nuserId") String nuserId,
                                       @Param("rdate") Date rdate,
                                       @Param("rtime") String rtime);

    /**
     * 添加预约
     * @param restoreTable 预约信息
     * @return 影响行数
     */
    int insert(RestoreTable restoreTable);

    /**
     * 更新预约审批状态
     * @param nuserId 用户ID
     * @param rdate 预约日期
     * @param rtime 时间段
     * @param permit 新状态
     * @return 影响行数
     */
    int updatePermit(@Param("nuserId") String nuserId, 
                     @Param("rdate") Date rdate, 
                     @Param("rtime") String rtime, 
                     @Param("permit") String permit);

    /**
     * 删除过时的预约（日期过时或当天但时间段已过期）
     * @param currentDate 当前日期
     * @param expiredTime 已过期的最大时间段值('0'-'3')，'-1'表示当天无过期时段
     * @return 影响行数
     */
    int deleteExpired(@Param("currentDate") Date currentDate,
                      @Param("expiredTime") String expiredTime);

    /**
     * 查询待审核的预约
     * @return 预约列表
     */
    List<RestoreTable> findPendingReservations();

    /**
     * 根据主键查询预约记录
     * @param nuserId 用户ID
     * @param rdate 预约日期
     * @param rtime 时间段
     * @return 预约记录
     */
    RestoreTable findByPrimaryKey(@Param("nuserId") String nuserId,
                                   @Param("rdate") Date rdate,
                                   @Param("rtime") String rtime);

    /**
     * 取消预约（删除未审核的预约）
     * @param nuserId 用户ID
     * @param rdate 预约日期
     * @param rtime 时间段
     * @return 影响行数
     */
    int deleteReservation(@Param("nuserId") String nuserId,
                          @Param("rdate") Date rdate,
                          @Param("rtime") String rtime);

    /**
     * 拒绝某设备的所有待审核预约
     * @param equipmentId 设备ID
     * @return 影响行数
     */
    int rejectPendingByEquipment(@Param("equipmentId") String equipmentId);
}
