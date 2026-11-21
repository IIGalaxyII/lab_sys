-- ============================================================
-- 高校实验室设备预约管理系统 - PostgreSQL 初始化脚本（labsys 库）
-- 适用版本: PostgreSQL 12+
--
-- 重要：本脚本必须连接到 labsys 数据库后执行，表会创建在当前库的 public 模式。
--   psql -U postgres -d labsys -f querytable.sql
-- 若 labsys 不存在，请先执行：
--   psql -U postgres -c "CREATE DATABASE labsys;"
-- 注意：不要在 postgres 库或图形客户端的默认库中直接执行，
--       否则表会建到错误的数据库，应用就会报 "relation xxx does not exist"。
-- ============================================================

SET search_path TO public;

-- 清理旧对象（按依赖顺序）
DROP VIEW IF EXISTS nview;
DROP TABLE IF EXISTS log CASCADE;
DROP TABLE IF EXISTS restore_table CASCADE;
DROP TABLE IF EXISTS equipment CASCADE;
DROP TABLE IF EXISTS nuser CASCADE;
DROP TABLE IF EXISTS adm CASCADE;

-- 普通用户表
CREATE TABLE nuser (
    id            CHAR(10)     NOT NULL,
    username      VARCHAR(15)  NOT NULL,
    email         VARCHAR(30)  NOT NULL,
    birthday      DATE         NOT NULL,
    password_hash VARCHAR(255) NOT NULL,
    CONSTRAINT pk_nuser PRIMARY KEY (id),
    CONSTRAINT uk_nuser_email UNIQUE (email)
);
COMMENT ON TABLE nuser IS '普通用户表';
COMMENT ON COLUMN nuser.id IS '学号';
COMMENT ON COLUMN nuser.password_hash IS '密码哈希值';

-- 管理员表
CREATE TABLE adm (
    id            CHAR(10)     NOT NULL,
    username      VARCHAR(15)  NOT NULL,
    email         VARCHAR(30)  NOT NULL,
    birthday      DATE         NOT NULL,
    password_hash VARCHAR(255) NOT NULL,
    CONSTRAINT pk_adm PRIMARY KEY (id),
    CONSTRAINT uk_adm_email UNIQUE (email)
);
COMMENT ON TABLE adm IS '管理员表';
COMMENT ON COLUMN adm.password_hash IS '密码哈希值';

-- 设备表
CREATE TABLE equipment (
    id            CHAR(2)      NOT NULL,
    ename         VARCHAR(30)  NOT NULL,
    state         CHAR(1)      NOT NULL DEFAULT '0',
    use_frequency REAL                  DEFAULT 0.00,
    CONSTRAINT pk_equipment PRIMARY KEY (id)
);
COMMENT ON TABLE equipment IS '设备表';
COMMENT ON COLUMN equipment.ename IS '设备名称';
COMMENT ON COLUMN equipment.state IS '设备状态: 0-可用, 1-占用, 2-维修, 3-报废';
COMMENT ON COLUMN equipment.use_frequency IS '使用频率';

-- 预约表
CREATE TABLE restore_table (
    nuser_id     CHAR(10) NOT NULL,
    rdate        DATE     NOT NULL,
    rtime        CHAR(1)  NOT NULL,
    equipment_id CHAR(2)  NOT NULL,
    permit       CHAR(1)  NOT NULL DEFAULT '0',
    CONSTRAINT pk_restore PRIMARY KEY (nuser_id, rdate, rtime),
    CONSTRAINT fk_restore_nuser FOREIGN KEY (nuser_id) REFERENCES nuser (id) ON DELETE CASCADE,
    CONSTRAINT fk_restore_equip FOREIGN KEY (equipment_id) REFERENCES equipment (id) ON DELETE CASCADE,
    CONSTRAINT ck_restore_rtime CHECK (rtime IN ('0', '1', '2', '3')),
    CONSTRAINT ck_restore_permit CHECK (permit IN ('0', '1', '2'))
);
COMMENT ON TABLE restore_table IS '预约表';
COMMENT ON COLUMN restore_table.nuser_id IS '用户ID';
COMMENT ON COLUMN restore_table.rdate IS '预约日期';
COMMENT ON COLUMN restore_table.rtime IS '时间段: 0-8:00-10:00, 1-10:00-12:00, 2-14:00-17:00, 3-19:00-21:00';
COMMENT ON COLUMN restore_table.equipment_id IS '设备ID';
COMMENT ON COLUMN restore_table.permit IS '审批状态: 0-未审核, 1-拒绝, 2-批准';

-- 预约表索引
CREATE INDEX idx_restore_date_time ON restore_table (rdate, rtime);
CREATE INDEX idx_restore_equipment ON restore_table (equipment_id);
CREATE INDEX idx_restore_permit ON restore_table (permit);

-- 设备维护日志表
CREATE TABLE log (
    equipment_id CHAR(2) NOT NULL,
    log_date     DATE    NOT NULL,
    state        CHAR(1) NOT NULL,
    CONSTRAINT pk_log PRIMARY KEY (equipment_id, log_date),
    CONSTRAINT fk_log_equipment FOREIGN KEY (equipment_id) REFERENCES equipment (id) ON DELETE CASCADE
);
COMMENT ON TABLE log IS '设备维护日志表';
COMMENT ON COLUMN log.log_date IS '设备更改状态的时间';
COMMENT ON COLUMN log.state IS '设备状态: 0-维修, 1-报废, 2-正常';

CREATE INDEX idx_log_date ON log (log_date);

-- 初始设备数据
INSERT INTO equipment (id, ename, state, use_frequency) VALUES ('01', '电子显微镜', '0', 0.00);
INSERT INTO equipment (id, ename, state, use_frequency) VALUES ('02', '红外光谱仪', '0', 0.00);
INSERT INTO equipment (id, ename, state, use_frequency) VALUES ('03', '质谱仪', '0', 0.00);

-- 普通用户对自己预约的视图
CREATE VIEW nview AS
SELECT nuser_id, rdate AS "date", rtime AS "time", equipment_id, permit
FROM restore_table;
