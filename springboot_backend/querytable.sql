CREATE DATABASE IF NOT EXISTS labsys;
use labsys;

-- 普通用户表
CREATE TABLE Nuser(
	id CHAR(10),
    username VARCHAR(15) NOT NULL,
    email VARCHAR(30) NOT NULL,
    birthday DATE NOT NULL,
    password_hash VARCHAR(255) NOT NULL COMMENT '密码哈希值',
    CONSTRAINT pk_nuser PRIMARY KEY(id),
    CONSTRAINT uk_nuser_email UNIQUE(email)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='普通用户表';

-- 管理员表
CREATE TABLE Adm(
	id CHAR(10),
    username VARCHAR(15) NOT NULL,
    email VARCHAR(30) NOT NULL,
    birthday DATE NOT NULL,
    password_hash VARCHAR(255) NOT NULL COMMENT '密码哈希值',
    CONSTRAINT pk_adm PRIMARY KEY(id),
    CONSTRAINT uk_adm_email UNIQUE(email)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='管理员表';

-- 设备表
CREATE TABLE equipment(
	id CHAR(2),
    ename VARCHAR(30) NOT NULL COMMENT '设备名称',
    state CHAR(1) NOT NULL DEFAULT '0' COMMENT '设备状态: 0-可用, 1-占用, 2-维修, 3-报废',
    use_frequency FLOAT(4,2) DEFAULT 0.00 COMMENT '使用频率',
    CONSTRAINT pk_equipment PRIMARY KEY(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='设备表';

-- 预约表
CREATE TABLE Restore_table(
    Nuser_id CHAR(10) NOT NULL COMMENT '用户ID',
    rdate DATE NOT NULL COMMENT '预约日期',
    rtime CHAR(1) NOT NULL COMMENT '时间段: 0-8:00-10:00, 1-10:00-12:00, 2-14:00-17:00, 3-19:00-21:00',
    equipment_id CHAR(2) NOT NULL COMMENT '设备ID',
    permit CHAR(1) NOT NULL DEFAULT '0' COMMENT '审批状态: 0-未审核, 1-拒绝, 2-批准',
    CONSTRAINT pk_restore PRIMARY KEY(Nuser_id,rdate,rtime),
    CONSTRAINT fk_Nuser FOREIGN KEY(Nuser_id) REFERENCES Nuser(id) ON DELETE CASCADE,
    CONSTRAINT fk_equip FOREIGN KEY(equipment_id) REFERENCES equipment(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='预约表';

-- 创建联合索引
CREATE INDEX idx_restore_date_time ON Restore_table(rdate, rtime);
CREATE INDEX idx_restore_equipment ON Restore_table(equipment_id);
CREATE INDEX idx_restore_permit ON Restore_table(permit);

-- 插入初始设备数据
INSERT INTO equipment VALUES('01','电子显微镜','0',0.00);
INSERT INTO equipment VALUES('02','红外光谱仪','0',0.00);
INSERT INTO equipment VALUES('03','质谱仪','0',0.00);

-- 设备维护日志表
CREATE TABLE Log(
    equipment_id CHAR(2) NOT NULL COMMENT '设备ID',
    log_date DATE NOT NULL COMMENT '设备更改状态的时间',
    state CHAR(1) NOT NULL COMMENT '设备状态: 0-维修, 1-报废, 2-正常',
    CONSTRAINT pk_log PRIMARY KEY(equipment_id, log_date),
    CONSTRAINT fk_log_equipment FOREIGN KEY(equipment_id) REFERENCES equipment(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='设备维护日志表';

-- 日志索引
CREATE INDEX idx_log_date ON Log(log_date);

-- 普通用户对自己预约的视图
CREATE VIEW Nview AS
SELECT Nuser_id, rdate AS date, rtime AS time, equipment_id, permit
FROM Restore_table;