package com.lab.service;

import com.lab.entity.Equipment;
import com.lab.entity.RestoreTable;
import com.lab.exception.BusinessException;
import com.lab.mapper.EquipmentMapper;
import com.lab.mapper.RestoreTableMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.annotation.PostConstruct;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * 预约服务类
 */
@Service
public class RestoreTableService {

    @Autowired
    private RestoreTableMapper restoreTableMapper;

    @Autowired
    private EquipmentMapper equipmentMapper;
    
    @Autowired
    private EquipmentService equipmentService;

    /**
     * 创建预约
     * @param nuserId 用户ID
     * @param rdate 预约日期
     * @param rtime 时间段
     * @param equipmentId 设备ID
     */
    @Transactional
    public void createReservation(String nuserId, Date rdate, String rtime, String equipmentId) {
        // 验证时间段
        if (!"0".equals(rtime) && !"1".equals(rtime) && !"2".equals(rtime) && !"3".equals(rtime)) {
            throw new BusinessException("无效的时间段");
        }

        // 检查设备是否存在
        Equipment equipment = equipmentMapper.findById(equipmentId);
        if (equipment == null) {
            throw new BusinessException("设备不存在");
        }

        // 检查设备状态（维修/报废时不能预约）
        if ("2".equals(equipment.getState()) || "3".equals(equipment.getState())) {
            throw new BusinessException("设备处于维修或报废状态，无法预约");
        }

        // 检查预约冲突（同一设备同一时间段，排除已拒绝的）
        RestoreTable conflict = restoreTableMapper.checkConflict(rdate, rtime, equipmentId);
        if (conflict != null) {
            throw new BusinessException("该时间段该设备已被预约");
        }

        // 检查用户时间冲突（同一用户同一时间段不能预约不同设备）
        RestoreTable userConflict = restoreTableMapper.checkUserTimeConflict(nuserId, rdate, rtime);
        if (userConflict != null) {
            throw new BusinessException("您在该时间段已有预约");
        }

        // 创建预约（初始状态为未审核）
        RestoreTable restoreTable = new RestoreTable();
        restoreTable.setNuserId(nuserId);
        restoreTable.setRdate(rdate);
        restoreTable.setRtime(rtime);
        restoreTable.setEquipmentId(equipmentId);
        restoreTable.setPermit("0"); // 未审核

        restoreTableMapper.insert(restoreTable);
    }

    /**
     * 查询用户的所有预约
     * @param nuserId 用户ID
     * @return 预约列表
     */
    public List<RestoreTable> findByUserId(String nuserId) {
        return restoreTableMapper.findByUserId(nuserId);
    }

    /**
     * 查询所有预约（管理员功能）
     * @return 预约列表
     */
    public List<RestoreTable> findAll() {
        return restoreTableMapper.findAll();
    }

    /**
     * 查询待审核的预约（管理员功能）
     * @return 预约列表
     */
    public List<RestoreTable> findPendingReservations() {
        return restoreTableMapper.findPendingReservations();
    }

    /**
     * 更新预约审批状态（管理员功能）
     * @param nuserId 用户ID
     * @param rdate 预约日期
     * @param rtime 时间段
     * @param permit 新状态（1-拒绝, 2-批准）
     */
    @Transactional
    public void updatePermit(String nuserId, Date rdate, String rtime, String permit) {
        // 验证审批状态
        if (!"1".equals(permit) && !"2".equals(permit)) {
            throw new BusinessException("无效的审批状态，只能设为拒绝(1)或批准(2)");
        }

        // 查询预约记录
        RestoreTable reservation = restoreTableMapper.findByPrimaryKey(nuserId, rdate, rtime);
        if (reservation == null) {
            throw new BusinessException("预约记录不存在");
        }

        // 状态流转校验：只有未审核(0)的预约可以审批
        if (!"0".equals(reservation.getPermit())) {
            throw new BusinessException("该预约已处理，不能重复审批");
        }

        // 如果要批准，需要检查设备状态
        if ("2".equals(permit)) {
            Equipment equipment = equipmentMapper.findById(reservation.getEquipmentId());
            if (equipment != null && ("2".equals(equipment.getState()) || "3".equals(equipment.getState()))) {
                throw new BusinessException("设备处于维修或报废状态，不能批准");
            }
        }

        restoreTableMapper.updatePermit(nuserId, rdate, rtime, permit);

        // 批准后联动更新设备状态为占用
        if ("2".equals(permit)) {
            String equipId = reservation.getEquipmentId();
            Equipment equipment = equipmentMapper.findById(equipId);
            if (equipment != null && "0".equals(equipment.getState())) {
                equipmentMapper.updateState(equipId, "1");
            }
        }
    }

    /**
     * 应用启动时执行：清理过时预约和更新设备使用频率
     */
    @PostConstruct
    public void init() {
        System.out.println("=== 系统启动，执行初始化任务 ===");
        try {
            deleteExpiredReservations();
        } catch (Exception e) {
            System.err.println("清理过时预约失败: " + e.getMessage());
        }
        try {
            updateEquipmentFrequency();
        } catch (Exception e) {
            System.err.println("设备使用频率更新失败: " + e.getMessage());
        }
    }

    /**
     * 删除过时的预约（启动时及定时执行）
     */
    @Scheduled(fixedRate = 600000) // 每10分钟执行一次
    public void deleteExpiredReservations() {
        Calendar calendar = Calendar.getInstance();

        // 去掉时间部分，只保留日期
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        Date currentDate = calendar.getTime();

        // 重新获取当前小时用于计算过期时段
        Calendar now = Calendar.getInstance();
        int hour = now.get(Calendar.HOUR_OF_DAY);
        String expiredTime; // 已过期的最大时间段值
        if (hour >= 21) {
            // 21:00之后，所有时段(0,1,2,3)都已过时
            expiredTime = "3";
        } else if (hour >= 17) {
            // 17:00-21:00，时段0,1,2已过时
            expiredTime = "2";
        } else if (hour >= 12) {
            // 12:00-17:00，时段0,1已过时
            expiredTime = "1";
        } else if (hour >= 10) {
            // 10:00-12:00，时段0已过时
            expiredTime = "0";
        } else {
            // 10:00之前，当天没有过时的时段
            expiredTime = "-1";
        }

        int deletedCount = restoreTableMapper.deleteExpired(currentDate, expiredTime);
        System.out.println("已删除 " + deletedCount + " 条过时预约记录");
    }
    
    /**
     * 取消预约（仅未审核状态可取消）
     * @param nuserId 用户ID
     * @param rdate 预约日期
     * @param rtime 时间段
     */
    @Transactional
    public void cancelReservation(String nuserId, Date rdate, String rtime) {
        RestoreTable reservation = restoreTableMapper.findByPrimaryKey(nuserId, rdate, rtime);
        if (reservation == null) {
            throw new BusinessException("预约不存在");
        }
        if (!"0".equals(reservation.getPermit())) {
            throw new BusinessException("只能取消未审核的预约");
        }
        int rows = restoreTableMapper.deleteReservation(nuserId, rdate, rtime);
        if (rows == 0) {
            throw new BusinessException("取消预约失败");
        }
    }

    /**
     * 根据主键查询预约记录
     * @param nuserId 用户ID
     * @param rdate 预约日期
     * @param rtime 时间段
     * @return 预约记录
     */
    public RestoreTable findByPrimaryKey(String nuserId, Date rdate, String rtime) {
        return restoreTableMapper.findByPrimaryKey(nuserId, rdate, rtime);
    }

    /**
     * 更新设备使用频率
     */
    public void updateEquipmentFrequency() {
        equipmentService.updateUseFrequency();
        System.out.println("设备使用频率更新完成");
    }
}