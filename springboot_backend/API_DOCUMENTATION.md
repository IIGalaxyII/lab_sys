# 高校实验室设备预约管理系统 - 后端管理员部分 API 文档

## 基础信息
- **基础URL**: `http://localhost:8080/api`
- **认证方式**: JWT Token (在请求头中携带 `Authorization: Bearer <token>`)
- **数据格式**: JSON

---

## 一、认证接口（无需Token）

### 1. 管理员登录
**POST** `/login/admin`

**请求体**:
```json
{
  "username": "管理员用户名",
  "email": "管理员邮箱",
  "birthday": "2000-01-01",
  "password": "密码"
}
```
> 注意：username、email、birthday 至少提供一项

**响应示例**:
```json
{
  "code": 200,
  "message": "登录成功",
  "data": {
    "token": "eyJhbGciOiJIUzI1NiJ9...",
    "userId": "admin001",
    "username": "管理员",
    "role": "admin"
  },
  "timestamp": 1718251234567
}
```

### 2. 管理员注册
**POST** `/login/register/admin`

**请求体**:
```json
{
  "id": "admin001",
  "username": "管理员",
  "email": "admin@example.com",
  "birthday": "2000-01-01",
  "password": "密码",
  "adkey": "11111"
}
```
> 注意：adkey 必须为 "11111" 才能注册成功

**响应示例**:
```json
{
  "code": 200,
  "message": "注册成功",
  "data": null,
  "timestamp": 1718251234567
}
```

---

## 二、管理员个人信息接口（需要Token）

### 1. 获取个人信息
**GET** `/admin/profile`

**请求头**:
```
Authorization: Bearer <token>
```

**响应示例**:
```json
{
  "code": 200,
  "message": "操作成功",
  "data": {
    "id": "admin001",
    "username": "管理员",
    "email": "admin@example.com",
    "birthday": "2000-01-01"
  },
  "timestamp": 1718251234567
}
```

### 2. 更新个人信息
**PUT** `/admin/profile`

**请求头**:
```
Authorization: Bearer <token>
```

**请求体**:
```json
{
  "username": "新用户名",
  "email": "newemail@example.com",
  "birthday": "2000-01-01"
}
```

**响应示例**:
```json
{
  "code": 200,
  "message": "信息更新成功",
  "data": null,
  "timestamp": 1718251234567
}
```

### 3. 修改密码
**POST** `/admin/profile/change-password`

**请求头**:
```
Authorization: Bearer <token>
```

**请求体**:
```json
{
  "oldPassword": "旧密码",
  "newPassword": "新密码"
}
```

**响应示例**:
```json
{
  "code": 200,
  "message": "密码修改成功",
  "data": null,
  "timestamp": 1718251234567
}
```

---

## 三、用户管理接口（管理员权限）

### 1. 查询所有普通用户
**GET** `/admin/users`

**请求头**:
```
Authorization: Bearer <token>
```

**响应示例**:
```json
{
  "code": 200,
  "message": "操作成功",
  "data": [
    {
      "id": "2021001",
      "username": "张三",
      "email": "zhangsan@example.com",
      "birthday": "2003-05-15"
    }
  ],
  "timestamp": 1718251234567
}
```

### 2. 根据ID查询用户
**GET** `/admin/users/{id}`

**请求头**:
```
Authorization: Bearer <token>
```

**响应示例**:
```json
{
  "code": 200,
  "message": "操作成功",
  "data": {
    "id": "2021001",
    "username": "张三",
    "email": "zhangsan@example.com",
    "birthday": "2003-05-15"
  },
  "timestamp": 1718251234567
}
```

### 3. 添加普通用户
**POST** `/admin/users`

**请求头**:
```
Authorization: Bearer <token>
```

**请求体**:
```json
{
  "id": "2021002",
  "username": "李四",
  "email": "lisi@example.com",
  "birthday": "2003-06-20"
}
```
> 注意：默认密码为 "123456"

**响应示例**:
```json
{
  "code": 200,
  "message": "添加成功",
  "data": null,
  "timestamp": 1718251234567
}
```

### 4. 删除普通用户
**DELETE** `/admin/users/{id}`

**请求头**:
```
Authorization: Bearer <token>
```

**响应示例**:
```json
{
  "code": 200,
  "message": "删除成功",
  "data": null,
  "timestamp": 1718251234567
}
```

### 5. 更新用户信息
**PUT** `/admin/users`

**请求头**:
```
Authorization: Bearer <token>
```

**请求体**:
```json
{
  "id": "2021001",
  "username": "张三丰",
  "email": "zhangsanfeng@example.com",
  "birthday": "2003-05-15"
}
```

**响应示例**:
```json
{
  "code": 200,
  "message": "更新成功",
  "data": null,
  "timestamp": 1718251234567
}
```

---

## 四、设备管理接口（管理员权限）

### 1. 查询所有设备
**GET** `/admin/equipment`

**请求头**:
```
Authorization: Bearer <token>
```

**响应示例**:
```json
{
  "code": 200,
  "message": "操作成功",
  "data": [
    {
      "id": "01",
      "ename": "电子显微镜",
      "state": "0",
      "useFrequency": 0.33
    }
  ],
  "timestamp": 1718251234567
}
```

> 设备状态说明：
> - `0`: 可用
> - `1`: 占用
> - `2`: 维修
> - `3`: 报废

### 2. 根据ID查询设备
**GET** `/admin/equipment/{id}`

**请求头**:
```
Authorization: Bearer <token>
```

**响应示例**:
```json
{
  "code": 200,
  "message": "操作成功",
  "data": {
    "id": "01",
    "ename": "电子显微镜",
    "state": "0",
    "useFrequency": 0.33
  },
  "timestamp": 1718251234567
}
```

### 3. 更新设备状态
**PUT** `/admin/equipment/state`

**请求头**:
```
Authorization: Bearer <token>
```

**请求体**:
```json
{
  "id": "01",
  "state": "2"
}
```

**响应示例**:
```json
{
  "code": 200,
  "message": "状态更新成功",
  "data": null,
  "timestamp": 1718251234567
}
```

> 注意：当设备状态为维修(2)或报废(3)时，该设备的预约将不能被批准

### 4. 更新设备使用频率
**POST** `/admin/equipment/update-frequency`

**请求头**:
```
Authorization: Bearer <token>
```

**响应示例**:
```json
{
  "code": 200,
  "message": "使用频率更新成功",
  "data": null,
  "timestamp": 1718251234567
}
```

> 说明：使用频率 = 该设备批准预约数 / 总批准预约数

---

## 五、预约管理接口（管理员权限）

### 1. 查询所有预约
**GET** `/admin/reservations`

**请求头**:
```
Authorization: Bearer <token>
```

**响应示例**:
```json
{
  "code": 200,
  "message": "操作成功",
  "data": [
    {
      "nuserId": "2021001",
      "rdate": "2026-06-15",
      "rtime": "0",
      "equipmentId": "01",
      "permit": "0"
    }
  ],
  "timestamp": 1718251234567
}
```

> 时间段说明：
> - `0`: 8:00-10:00
> - `1`: 10:00-12:00
> - `2`: 14:00-17:00
> - `3`: 19:00-21:00

> 审批状态说明：
> - `0`: 未审核
> - `1`: 拒绝
> - `2`: 批准

### 2. 查询待审核的预约
**GET** `/admin/reservations/pending`

**请求头**:
```
Authorization: Bearer <token>
```

**响应示例**:
```json
{
  "code": 200,
  "message": "操作成功",
  "data": [
    {
      "nuserId": "2021001",
      "rdate": "2026-06-15",
      "rtime": "0",
      "equipmentId": "01",
      "permit": "0"
    }
  ],
  "timestamp": 1718251234567
}
```

### 3. 更新预约审批状态
**PUT** `/admin/reservations/permit`

**请求头**:
```
Authorization: Bearer <token>
```

**请求体**:
```json
{
  "nuserId": "2021001",
  "rdate": "2026-06-15",
  "rtime": "0",
  "permit": "2"
}
```

**响应示例**:
```json
{
  "code": 200,
  "message": "审批状态更新成功",
  "data": null,
  "timestamp": 1718251234567
}
```

> 注意：
> - 当设备状态为维修(2)或报废(3)时，不能批准该设备的预约
> - 批准时会检查设备状态，如果不符合条件会返回错误

---

## 六、错误响应

所有接口在发生错误时都会返回统一格式的错误响应：

```json
{
  "code": 500,
  "message": "错误描述信息",
  "data": null,
  "timestamp": 1718251234567
}
```

常见错误码：
- `200`: 成功
- `401`: 未授权（未登录或token过期）
- `403`: 禁止访问（无权限）
- `500`: 服务器错误（业务异常）

---

## 七、重要说明

### 1. 自动清理过时预约
系统启动时会自动删除过时的预约记录（日期已过或当天但时间段已过的预约）。

### 2. 密码加密
所有密码都使用 SHA-256 算法进行加密存储，数据库中不保存明文密码。

### 3. 权限验证
所有需要管理员权限的接口都会在拦截器中验证 token 中的角色信息，非管理员无法访问。

### 4. 预约冲突检测
创建预约时会自动检测同一设备、同一日期、同一时间段是否已被预约，避免冲突。

### 5. 设备状态联动
- 当设备状态设置为维修(2)或报废(3)时，该设备的新预约将被禁止
- 已经存在的预约在批准时也会检查设备状态

---

## 八、测试建议

### 1. 初始化管理员账号
首先通过注册接口创建一个管理员账号：
```bash
curl -X POST http://localhost:8080/api/login/register/admin \
  -H "Content-Type: application/json" \
  -d '{
    "id": "admin001",
    "username": "管理员",
    "email": "admin@test.com",
    "birthday": "2000-01-01",
    "password": "admin123",
    "adkey": "11111"
  }'
```

### 2. 登录获取Token
```bash
curl -X POST http://localhost:8080/api/login/admin \
  -H "Content-Type: application/json" \
  -d '{
    "username": "管理员",
    "email": "admin@test.com",
    "birthday": "2000-01-01",
    "password": "admin123"
  }'
```

### 3. 使用Token访问其他接口
```bash
curl -X GET http://localhost:8080/api/admin/users \
  -H "Authorization: Bearer <your_token_here>"
```

---

## 九、技术栈

- **框架**: Spring Boot 2.7.18
- **持久层**: MyBatis 2.3.1
- **数据库**: MySQL 8.0.46
- **认证**: JWT (jjwt 0.9.1)
- **其他**: Lombok
