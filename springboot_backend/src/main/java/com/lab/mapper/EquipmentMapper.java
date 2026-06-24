package com.lab.mapper;

import com.lab.entity.Equipment;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 设备Mapper接口
 */
@Mapper
public interface EquipmentMapper {

    /**
     * 查询所有设备
     * @return 设备列表
     */
    List<Equipment> findAll();

    /**
     * 根据ID查询设备
     * @param id 设备ID
     * @return 设备信息
     */
    Equipment findById(@Param("id") String id);

    /**
     * 更新设备状态
     * @param id 设备ID
     * @param state 新状态
     * @return 影响行数
     */
    int updateState(@Param("id") String id, @Param("state") String state);

    /**
     * 更新设备使用频率
     * @param id 设备ID
     * @param useFrequency 使用频率
     * @return 影响行数
     */
    int updateUseFrequency(@Param("id") String id, @Param("useFrequency") Float useFrequency);

    /**
     * 统计某设备的批准预约数
     * @param equipmentId 设备ID
     * @return 批准预约数
     */
    int countApprovedReservations(@Param("equipmentId") String equipmentId);

    /**
     * 统计所有设备的总批准预约数
     * @return 总批准预约数
     */
    int countTotalApprovedReservations();
}
