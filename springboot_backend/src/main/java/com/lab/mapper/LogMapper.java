package com.lab.mapper;

import com.lab.entity.Log;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

/**
 * 设备维护日志Mapper接口
 */
@Mapper
public interface LogMapper {

    /**
     * 插入日志
     * @param log 日志对象
     * @return 影响行数
     */
    int insert(Log log);

    /**
     * 查询所有日志
     * @return 日志列表
     */
    List<Log> findAll();

    /**
     * 根据设备ID查询日志
     * @param equipmentId 设备ID
     * @return 日志列表
     */
    List<Log> findByEquipmentId(@Param("equipmentId") String equipmentId);

    /**
     * 根据日期范围查询日志
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @return 日志列表
     */
    List<Log> findByDateRange(@Param("startDate") Date startDate, 
                              @Param("endDate") Date endDate);
}