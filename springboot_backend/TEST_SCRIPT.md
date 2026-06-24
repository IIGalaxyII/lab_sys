# 后端完整功能测试脚本

## 测试前准备

### 1. 确保数据库已创建
```bash
mysql -u root -p < querytable.sql
```

### 2. 启动后端服务
```bash
mvn spring-boot:run
```

---

## 一、认证测试

### 1.1 管理员注册
```bash
curl -X POST http://localhost:8080/api/login/register/admin \
  -H "Content-Type: application/json" \
  -d '{
    "id": "admin001",
    "username": "系统管理员",
    "email": "admin@lab.com",
    "birthday": "2000-01-01",
    "password": "admin123",
    "adkey": "11111"
  }'
```

**预期响应**:
```json
{
  "code": 200,
  "message": "注册成功",
  "data": null,
  "timestamp": xxx
}
```

### 1.2 普通用户注册
```bash
curl -X POST http://localhost:8080/api/login/register/user \
  -H "Content-Type: application/json" \
  -d '{
    "id": "2021001",
    "username": "张三",
    "email": "zhangsan@test.com",
    "birthday": "2003-05-15",
    "password": "123456"
  }'
```

### 1.3 管理员登录
```bash
curl -X POST http://localhost:8080/api/login/admin \
  -H "Content-Type: application/json" \
  -d '{
    "username": "系统管理员",
    "email": "admin@lab.com",
    "birthday": "2000-01-01",
    "password": "admin123"
  }'
```

**保存返回的token**，后续请求需要使用。

### 1.4 普通用户登录
```bash
curl -X POST http://localhost:8080/api/login/user \
  -H "Content-Type: application/json" \
  -d '{
    "username": "张三",
    "email": "zhangsan@test.com",
    "birthday": "2003-05-15",
    "password": "123456"
  }'
```

---

## 二、管理员功能测试

**注意**: 以下所有请求都需要在Header中添加：
```
Authorization: Bearer YOUR_ADMIN_TOKEN
```

### 2.1 获取管理员个人信息
```bash
curl -X GET http://localhost:8080/api/admin/profile \
  -H "Authorization: Bearer YOUR_ADMIN_TOKEN"
```

### 2.2 更新管理员个人信息
```bash
curl -X PUT http://localhost:8080/api/admin/profile \
  -H "Authorization: Bearer YOUR_ADMIN_TOKEN" \
  -H "Content-Type: application/json" \
  -d '{
    "username": "新管理员名",
    "email": "newadmin@lab.com",
    "birthday": "2000-01-01"
  }'
```

### 2.3 查询所有普通用户
```bash
curl -X GET http://localhost:8080/api/admin/users \
  -H "Authorization: Bearer YOUR_ADMIN_TOKEN"
```

### 2.4 添加普通用户
```bash
curl -X POST http://localhost:8080/api/admin/users \
  -H "Authorization: Bearer YOUR_ADMIN_TOKEN" \
  -H "Content-Type: application/json" \
  -d '{
    "id": "2021002",
    "username": "李四",
    "email": "lisi@test.com",
    "birthday": "2003-06-20"
  }'
```

### 2.5 查询所有设备
```bash
curl -X GET http://localhost:8080/api/admin/equipment \
  -H "Authorization: Bearer YOUR_ADMIN_TOKEN"
```

### 2.6 更新设备状态
```bash
curl -X PUT http://localhost:8080/api/admin/equipment/state \
  -H "Authorization: Bearer YOUR_ADMIN_TOKEN" \
  -H "Content-Type: application/json" \
  -d '{
    "id": "01",
    "state": "2"
  }'
```

### 2.7 查询待审核预约
```bash
curl -X GET http://localhost:8080/api/admin/reservations/pending \
  -H "Authorization: Bearer YOUR_ADMIN_TOKEN"
```

### 2.8 审批预约（批准）
```bash
curl -X PUT http://localhost:8080/api/admin/reservations/permit \
  -H "Authorization: Bearer YOUR_ADMIN_TOKEN" \
  -H "Content-Type: application/json" \
  -d '{
    "nuserId": "2021001",
    "rdate": "2026-06-20",
    "rtime": "0",
    "permit": "2"
  }'
```

### 2.9 查询操作日志
```bash
curl -X GET http://localhost:8080/api/admin/logs \
  -H "Authorization: Bearer YOUR_ADMIN_TOKEN"
```

---

## 三、普通用户功能测试

**注意**: 以下所有请求都需要在Header中添加：
```
Authorization: Bearer YOUR_USER_TOKEN
```

### 3.1 获取个人基本信息
```bash
curl -X GET http://localhost:8080/api/user/profile \
  -H "Authorization: Bearer YOUR_USER_TOKEN"
```

### 3.2 更新个人信息
```bash
curl -X PUT http://localhost:8080/api/user/profile \
  -H "Authorization: Bearer YOUR_USER_TOKEN" \
  -H "Content-Type: application/json" \
  -d '{
    "username": "张三丰",
    "email": "zhangsanfeng@test.com",
    "birthday": "2003-05-15"
  }'
```

### 3.3 查询所有设备
```bash
curl -X GET http://localhost:8080/api/equipment \
  -H "Authorization: Bearer YOUR_USER_TOKEN"
```

### 3.4 创建预约
```bash
curl -X POST http://localhost:8080/api/user/reservations \
  -H "Authorization: Bearer YOUR_USER_TOKEN" \
  -H "Content-Type: application/json" \
  -d '{
    "rdate": "2026-06-20",
    "rtime": "0",
    "equipmentId": "02"
  }'
```

### 3.5 查看个人预约记录
```bash
curl -X GET http://localhost:8080/api/user/reservations \
  -H "Authorization: Bearer YOUR_USER_TOKEN"
```

### 3.6 修改密码
```bash
curl -X POST http://localhost:8080/api/user/profile/change-password \
  -H "Authorization: Bearer YOUR_USER_TOKEN" \
  -H "Content-Type: application/json" \
  -d '{
    "oldPassword": "123456",
    "newPassword": "654321"
  }'
```

---

## 四、边界测试

### 4.1 预约冲突测试
使用同一用户或不同用户，尝试预约同一设备、同一时间：
```bash
# 第一次预约（应该成功）
curl -X POST http://localhost:8080/api/user/reservations \
  -H "Authorization: Bearer YOUR_USER_TOKEN" \
  -H "Content-Type: application/json" \
  -d '{
    "rdate": "2026-06-20",
    "rtime": "0",
    "equipmentId": "02"
  }'

# 第二次预约（应该失败：该时间段已被预约）
curl -X POST http://localhost:8080/api/user/reservations \
  -H "Authorization: Bearer ANOTHER_USER_TOKEN" \
  -H "Content-Type: application/json" \
  -d '{
    "rdate": "2026-06-20",
    "rtime": "0",
    "equipmentId": "02"
  }'
```

### 4.2 设备状态限制测试
将设备设置为维修状态后，尝试批准该设备的预约：

```bash
# 1. 设置设备为维修状态
curl -X PUT http://localhost:8080/api/admin/equipment/state \
  -H "Authorization: Bearer YOUR_ADMIN_TOKEN" \
  -H "Content-Type: application/json" \
  -d '{
    "id": "02",
    "state": "2"
  }'

# 2. 尝试批准该设备的预约（应该失败）
curl -X PUT http://localhost:8080/api/admin/reservations/permit \
  -H "Authorization: Bearer YOUR_ADMIN_TOKEN" \
  -H "Content-Type: application/json" \
  -d '{
    "nuserId": "2021001",
    "rdate": "2026-06-20",
    "rtime": "0",
    "permit": "2"
  }'
```

### 4.3 权限隔离测试
使用普通用户Token访问管理员接口（应该返回403）：
```bash
curl -X GET http://localhost:8080/api/admin/users \
  -H "Authorization: Bearer YOUR_USER_TOKEN"
```

**预期响应**:
```json
{
  "code": 403,
  "message": "无权访问",
  "data": null,
  "timestamp": xxx
}
```

### 4.4 Token失效测试
使用无效Token访问需要认证的接口：
```bash
curl -X GET http://localhost:8080/api/admin/users \
  -H "Authorization: Bearer INVALID_TOKEN"
```

**预期响应**:
```json
{
  "code": 401,
  "message": "登录已过期，请重新登录",
  "data": null,
  "timestamp": xxx
}
```

---

## 五、自动化测试脚本（Bash）

创建 `test_api.sh` 文件：

```bash
#!/bin/bash

BASE_URL="http://localhost:8080/api"

echo "=== 开始API测试 ==="

# 1. 管理员注册
echo "1. 管理员注册..."
ADMIN_REG_RESULT=$(curl -s -X POST $BASE_URL/login/register/admin \
  -H "Content-Type: application/json" \
  -d '{
    "id": "admin001",
    "username": "管理员",
    "email": "admin@test.com",
    "birthday": "2000-01-01",
    "password": "admin123",
    "adkey": "11111"
  }')
echo "管理员注册结果: $ADMIN_REG_RESULT"

# 2. 管理员登录
echo "2. 管理员登录..."
ADMIN_LOGIN_RESULT=$(curl -s -X POST $BASE_URL/login/admin \
  -H "Content-Type: application/json" \
  -d '{
    "username": "管理员",
    "email": "admin@test.com",
    "birthday": "2000-01-01",
    "password": "admin123"
  }')
ADMIN_TOKEN=$(echo $ADMIN_LOGIN_RESULT | grep -o '"token":"[^"]*"' | cut -d'"' -f4)
echo "管理员Token: $ADMIN_TOKEN"

# 3. 查询用户
echo "3. 查询所有用户..."
curl -s -X GET $BASE_URL/admin/users \
  -H "Authorization: Bearer $ADMIN_TOKEN"

echo ""
echo "=== 测试完成 ==="
```

运行测试：
```bash
chmod +x test_api.sh
./test_api.sh
```

---

## 六、测试检查清单

- [ ] 管理员注册成功
- [ ] 普通用户注册成功
- [ ] 管理员登录成功并获取Token
- [ ] 普通用户登录成功并获取Token
- [ ] 管理员可以查询所有用户
- [ ] 管理员可以添加用户
- [ ] 管理员可以删除用户
- [ ] 管理员可以查询设备
- [ ] 管理员可以更新设备状态
- [ ] 管理员可以查询待审核预约
- [ ] 管理员可以审批预约
- [ ] 管理员可以查看操作日志
- [ ] 普通用户可以查询设备
- [ ] 普通用户可以创建预约
- [ ] 普通用户可以查看个人预约
- [ ] 普通用户可以修改个人信息
- [ ] 普通用户可以修改密码
- [ ] 预约冲突检测正常工作
- [ ] 设备状态限制正常工作
- [ ] 权限隔离正常工作
- [ ] Token验证正常工作
- [ ] 操作日志正常记录

---

## 七、常见问题排查

### 问题1: 注册失败
**检查**: 
- 数据库是否正常运行
- adkey是否正确（必须是"11111"）
- ID是否已存在

### 问题2: 登录失败
**检查**:
- 用户名、邮箱、生日是否匹配
- 密码是否正确
- 数据库中是否有对应的用户记录

### 问题3: Token无效
**检查**:
- Token是否正确复制
- 是否添加了"Bearer "前缀
- Token是否过期

### 问题4: 权限错误
**检查**:
- 是否使用了正确的角色Token
- 接口路径是否正确

---

**测试完成后，您应该有一个完全可用的后端系统！** 🎉
