-- 测试数据初始化脚本
USE labsys;

-- 插入测试管理员账号（密码: admin123, SHA-256+Base64加密后的哈希值）
INSERT INTO Adm(id, username, email, birthday, password_hash)
VALUES('admin001', '系统管理员', 'admin@lab.com', '2000-01-01',
       'JAvlGPq9JyTdtvBO6x2llnRI1+gxwIyPqCKAn3THIKk=')
ON DUPLICATE KEY UPDATE username='系统管理员';

-- 插入测试普通用户账号（密码: 123456, SHA-256+Base64加密后的哈希值）
INSERT INTO Nuser(id, username, email, birthday, password_hash)
VALUES('2021001', '张三', 'zhangsan@test.com', '2003-05-15',
       'jZae727K08KaOmKSgOaGzww/XVqGr/PKEgIMkjrcbJI=')
ON DUPLICATE KEY UPDATE username='张三';

INSERT INTO Nuser(id, username, email, birthday, password_hash)
VALUES('2021002', '李四', 'lisi@test.com', '2003-06-20',
       'jZae727K08KaOmKSgOaGzww/XVqGr/PKEgIMkjrcbJI=')
ON DUPLICATE KEY UPDATE username='李四';

-- 查看插入的数据
SELECT '管理员账号:' AS info;
SELECT * FROM Adm;

SELECT '普通用户账号:' AS info;
SELECT id, username, email, birthday FROM Nuser;

SELECT '设备列表:' AS info;
SELECT * FROM equipment;

-- 说明：
-- 1. 管理员账号: admin001 / admin123
-- 2. 普通用户: 2021001 / 123456
-- 3. 普通用户: 2021002 / 123456
