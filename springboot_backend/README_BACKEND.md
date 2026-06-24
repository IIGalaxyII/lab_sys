# 高校实验室设备预约管理系统 - 后端

## 项目简介
基于 Spring Boot + MyBatis + MySQL 8.0 的高校实验室设备预约管理系统后端部分，实现了管理员和普通用户的双角色权限体系。

## 技术栈
- **Spring Boot**: 2.7.18
- **MyBatis**: 2.3.1
- **MySQL**: 8.0.46
- **JWT**: jjwt 0.9.1
- **Lombok**: 最新版本

## 功能特性

### 管理员功能
1. **认证管理**
   - 管理员登录（支持用户名/邮箱/生日 + 密码）
   - 管理员注册（需要管理员密钥：11111）
   - 修改个人信息
   - 修改密码

2. **用户管理**
   - 查询所有普通用户
   - 根据ID查询用户
   - 添加普通用户（默认密码：123456）
   - 删除普通用户
   - 更新用户信息

3. **设备管理**
   - 查询所有设备
   - 根据ID查询设备
   - 更新设备状态（可用/占用/维修/报废）
   - 更新设备使用频率

4. **预约管理**
   - 查询所有预约
   - 查询待审核预约
   - 审批预约（批准/拒绝）
   - 自动检查设备状态（维修/报废时禁止批准）

### 普通用户功能
1. **认证管理**
   - 普通用户登录
   - 普通用户注册
   - 修改个人信息
   - 修改密码

2. **预约管理**
   - 创建预约
   - 查看个人预约记录
   - 自动检测预约冲突

### 系统特性
1. **权限控制**
   - 基于 JWT 的无状态认证
   - 双角色权限隔离（管理员/普通用户）
   - 接口级别的权限验证

2. **数据安全**
   - 密码 SHA-256 加密存储
   - Token 24小时自动过期
   - 参数合法性校验

3. **业务逻辑**
   - 预约冲突检测（同一设备、同一时间不能重复预约）
   - 设备状态联动（维修/报废时禁止批准预约）
   - 过时预约自动清理（系统启动时）
   - 使用频率自动计算

## 快速开始

### 1. 环境要求
- JDK 8 或以上
- Maven 3.6+
- MySQL 8.0.46

### 2. 数据库配置

#### 2.1 执行 SQL 脚本
```bash
# 在 MySQL 中执行
mysql -u root -p < querytable.sql
```

或者直接在 MySQL 客户端执行 `querytable.sql` 文件中的内容。

#### 2.2 修改数据库配置
编辑 `src/main/resources/application.yml`：
```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/labsys?useSSL=false&serverTimezone=Asia/Shanghai&characterEncoding=utf8
    username: root
    password: 你的密码
```

### 3. 编译项目
```bash
mvn clean package
```

### 4. 运行项目
```bash
mvn spring-boot:run
```

或者直接运行主类：`com.lab.App`

### 5. 测试接口
项目启动后，访问 `http://localhost:8080`

详细 API 文档请查看 `API_DOCUMENTATION.md`

## 项目结构

```
src/main/java/com/lab/
├── App.java                          # 主启动类
├── common/                           # 公共类
│   └── Result.java                   # 统一响应结果
├── config/                           # 配置类
│   └── WebConfig.java                # Web配置（拦截器、跨域）
├── controller/                       # 控制器层
│   ├── AuthController.java           # 认证控制器
│   ├── AdminUserController.java      # 用户管理控制器
│   ├── AdminEquipmentController.java # 设备管理控制器
│   ├── AdminReservationController.java # 预约管理控制器
│   └── AdminProfileController.java   # 管理员个人信息控制器
├── entity/                           # 实体类
│   ├── Nuser.java                    # 普通用户
│   ├── Adm.java                      # 管理员
│   ├── Equipment.java                # 设备
│   └── RestoreTable.java             # 预约表
├── exception/                        # 异常处理
│   ├── BusinessException.java        # 业务异常
│   └── GlobalExceptionHandler.java   # 全局异常处理器
├── interceptor/                      # 拦截器
│   └── JwtInterceptor.java           # JWT拦截器
├── mapper/                           # Mapper接口
│   ├── NuserMapper.java
│   ├── AdmMapper.java
│   ├── EquipmentMapper.java
│   └── RestoreTableMapper.java
├── service/                          # 服务层
│   ├── NuserService.java
│   ├── AdmService.java
│   ├── EquipmentService.java
│   └── RestoreTableService.java
└── util/                             # 工具类
    └── JwtUtil.java                  # JWT工具类

src/main/resources/
├── application.yml                   # 应用配置文件
├── mybatis-config.xml                # MyBatis配置
└── mapper/                           # MyBatis XML映射文件
    ├── NuserMapper.xml
    ├── AdmMapper.xml
    ├── EquipmentMapper.xml
    └── RestoreTableMapper.xml
```

## 数据库设计

### 表结构
1. **Nuser** - 普通用户表
2. **Adm** - 管理员表
3. **equipment** - 设备表
4. **Restore_table** - 预约表

### 索引
- `idx_restore_date_time` - 预约日期和时间联合索引
- `idx_restore_equipment` - 设备ID索引
- `idx_restore_permit` - 审批状态索引

## API 示例

### 1. 管理员登录
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

### 2. 查询所有用户（需要Token）
```bash
curl -X GET http://localhost:8080/api/admin/users \
  -H "Authorization: Bearer YOUR_TOKEN_HERE"
```

### 3. 更新设备状态
```bash
curl -X PUT http://localhost:8080/api/admin/equipment/state \
  -H "Authorization: Bearer YOUR_TOKEN_HERE" \
  -H "Content-Type: application/json" \
  -d '{
    "id": "01",
    "state": "2"
  }'
```

更多 API 请查看 `API_DOCUMENTATION.md`

## 重要说明

### 1. 密码加密
所有密码使用 SHA-256 算法加密后存储，数据库中不保存明文密码。

### 2. Token 机制
- Token 有效期：24小时
- 需要在请求头中携带：`Authorization: Bearer <token>`
- Token 过期后需要重新登录

### 3. 预约时间段
- `0`: 8:00-10:00
- `1`: 10:00-12:00
- `2`: 14:00-17:00
- `3`: 19:00-21:00

### 4. 设备状态
- `0`: 可用
- `1`: 占用
- `2`: 维修
- `3`: 报废

### 5. 审批状态
- `0`: 未审核
- `1`: 拒绝
- `2`: 批准

### 6. 自动清理
系统启动时会自动删除过时的预约记录。

## 常见问题

### Q1: 启动时提示数据库连接失败？
A: 请检查：
1. MySQL 是否正常运行
2. 数据库 `labsys` 是否已创建
3. `application.yml` 中的数据库配置是否正确

### Q2: 如何创建第一个管理员账号？
A: 使用注册接口，adkey 必须为 "11111"：
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

### Q3: Token 如何使用？
A: 在所有需要认证的接口中，在请求头中添加：
```
Authorization: Bearer your_token_here
```

### Q4: 如何查看 SQL 执行情况？
A: 可以在 `application.yml` 中添加：
```yaml
mybatis:
  configuration:
    log-impl: org.apache.ibatis.logging.stdout.StdOutImpl
```

## 后续开发计划
- [ ] 完善普通用户界面功能
- [ ] 添加日志记录功能
- [ ] 实现设备使用率统计报表
- [ ] 添加邮件通知功能
- [ ] 实现预约取消功能

## 许可证
本项目仅供学习交流使用。

## 联系方式
如有问题，请提交 Issue。
