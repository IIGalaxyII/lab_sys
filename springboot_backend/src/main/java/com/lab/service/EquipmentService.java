package com.lab.service;

import com.lab.entity.Equipment;
import com.lab.mapper.EquipmentMapper;
import com.lab.mapper.RestoreTableMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 设备服务类
 */
@Service
public class EquipmentService {

    @Autowired
    private RestoreTableMapper restoreTableMapper;

    @Autowired
    private EquipmentMapper equipmentMapper;

    @Autowired
    private LogService logService;

    /**
     * 查询所有设备
     * @return 设备列表
     */
    public List<Equipment> findAll() {
        return equipmentMapper.findAll();
    }

    /**
     * 根据ID查询设备
     * @param id 设备ID
     * @return 设备信息
     */
    public Equipment findById(String id) {
        Equipment equipment = equipmentMapper.findById(id);
        if (equipment == null) {
            throw new RuntimeException("设备不存在");
        }
        return equipment;
    }

    /**
     * 更新设备状态（管理员功能）
     * @param id 设备ID
     * @param state 新状态（0-可用, 1-占用, 2-维修, 3-报废）
     */
    @Transactional
    public void updateState(String id, String state) {
        Equipment equipment = equipmentMapper.findById(id);
        if (equipment == null) {
            throw new RuntimeException("设备不存在");
        }

        // 验证状态值
        if (!"0".equals(state) && !"1".equals(state) && !"2".equals(state) && !"3".equals(state)) {
            throw new RuntimeException("无效的设备状态");
        }

        equipmentMapper.updateState(id, state);

        // 设备变为维修/报废状态时，自动拒绝该设备的所有待审核预约
        if ("2".equals(state) || "3".equals(state)) {
            restoreTableMapper.rejectPendingByEquipment(id);
        }

        // 记录设备维护日志
        // 设备状态映射: 设备表(0-可用,1-占用,2-维修,3-报废) -> 日志表(0-维修,1-报废,2-正常)
        String logState;
        if ("2".equals(state)) {
            logState = "0"; // 维修
        } else if ("3".equals(state)) {
            logState = "1"; // 报废
        } else {
            logState = "2"; // 正常（可用或占用）
        }
        logService.addLog(id, logState);
    }

    /**
     * 更新设备使用频率
     */
    @Transactional
    public void updateUseFrequency() {
        // 获取所有设备
        List<Equipment> equipments = equipmentMapper.findAll();

        // 获取总批准预约数
        int totalApproved = equipmentMapper.countTotalApprovedReservations();

        for (Equipment equip : equipments) {
            // 获取该设备的批准预约数
            int equipApproved = equipmentMapper.countApprovedReservations(equip.getId());

            // 计算使用频率
            Float frequency = 0.0f;
            if (totalApproved > 0) {
                frequency = (float) equipApproved / totalApproved;
            }

            // 更新数据库
            equipmentMapper.updateUseFrequency(equip.getId(), frequency);
        }
    }
}