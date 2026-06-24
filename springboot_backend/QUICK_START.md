# 快速启动指南

## 5分钟快速开始

### 步骤1: 准备数据库（2分钟）

1. **确保MySQL 8.0正在运行**

2. **执行建表脚本**
```bash
# 方法1: 命令行执行
mysql -u root -p < querytable.sql

# 方法2: MySQL客户端中执行
source C:/Users/60325/mywork/Java/lab_admin_sys/springboot_backend/querytable.sql
```

3. **执行测试数据脚本（可选）**
```bash
mysql -u root -p < test_data.sql
```

### 步骤2: 配置项目（1分钟）

编辑 `src/main/resources/application.yml`:
```yaml
spring:
  datasource:
    username: root
    password: 你的MySQL密码  # 修改这里
```

### 步骤3: 编译运行（2分钟）

```bash
# 进入项目目录
cd C:\Users\60325\mywork\Java\lab_admin_sys\springboot_backend

# 编译项目
mvn clean package

# 运行项目
mvn spring-boot:run
```

或者在IDE中直接运行 `com.lab.App` 类

### 步骤4: 创建管理员账号

使用Postman或curl执行：

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

### 步骤5: 登录获取Token

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

响应中包含token，复制保存。

### 步骤6: 测试API

```bash
# 查询所有用户
curl -X GET http://localhost:8080/api/admin/users \
  -H "Authorization: Bearer YOUR_TOKEN_HERE"

# 查询所有设备
curl -X GET http://localhost:8080/api/admin/equipment \
  -H "Authorization: Bearer YOUR_TOKEN_HERE"
```

---

## 常见问题速查

### ❌ 问题1: 数据库连接失败
**错误信息**: `Communications link failure`

**解决方案**:
1. 检查MySQL是否启动
2. 确认数据库名是 `labsys`
3. 检查 `application.yml` 中的用户名和密码

### ❌ 问题2: 端口被占用
**错误信息**: `Port 8080 is already in use`

**解决方案**:
修改 `application.yml`:
```yaml
server:
  port: 8081  # 改为其他端口
```

### ❌ 问题3: Token无效
**错误信息**: `未登录，请先登录`

**解决方案**:
1. 确认请求头中包含 `Authorization: Bearer <token>`
2. 检查token是否过期（24小时）
3. 重新登录获取新token

### ❌ 问题4: Maven依赖下载失败
**错误信息**: `Could not resolve dependencies`

**解决方案**:
```bash
# 清理Maven缓存
mvn dependency:purge-local-repository

# 重新下载
mvn clean install
```

---

## 测试账号清单

### 管理员账号（需要先注册）
- ID: admin001
- 用户名: 管理员
- 邮箱: admin@test.com
- 生日: 2000-01-01
- 密码: admin123
- 管理员密钥: 11111

### 普通用户账号（test_data.sql中提供）
- 学号: 2021001
- 用户名: 张三
- 邮箱: zhangsan@test.com
- 生日: 2003-05-15
- 密码: 123456

---

## API测试模板（Postman）

### 1. 管理员注册
```
POST http://localhost:8080/api/login/register/admin
Content-Type: application/json

{
  "id": "admin001",
  "username": "管理员",
  "email": "admin@test.com",
  "birthday": "2000-01-01",
  "password": "admin123",
  "adkey": "11111"
}
```

### 2. 管理员登录
```
POST http://localhost:8080/api/login/admin
Content-Type: application/json

{
  "username": "管理员",
  "email": "admin@test.com",
  "birthday": "2000-01-01",
  "password": "admin123"
}
```

### 3. 查询用户（需要Token）
```
GET http://localhost:8080/api/admin/users
Authorization: Bearer {{token}}
```

### 4. 更新设备状态（需要Token）
```
PUT http://localhost:8080/api/admin/equipment/state
Authorization: Bearer {{token}}
Content-Type: application/json

{
  "id": "01",
  "state": "2"
}
```

---

## 开发工具推荐

### 后端开发
- **IDE**: IntelliJ IDEA Ultimate
- **API测试**: Postman / Apifox
- **数据库管理**: Navicat / DBeaver
- **Maven**: 3.6+

### 前端开发（后续）
- **框架**: Vue 3 + Vite
- **UI库**: Element Plus
- **HTTP客户端**: Axios
- **状态管理**: Pinia

---

## 下一步

1. ✅ 后端管理员功能已完成
2. ⏳ 前端开发
3. ⏳ 前后端联调
4. ⏳ 测试与部署

祝开发顺利！🚀
