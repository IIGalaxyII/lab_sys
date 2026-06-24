# 快速启动指南

## 前置条件

1. **Java JDK** - JDK 8 或 JDK 17（注意：JDK 24 与 Spring Boot 2.7.x 不兼容，请使用低版本）
2. **MySQL 8.0+** - 已安装并运行
3. **Node.js** - 已安装（建议 v16+）
4. **Maven** - 已安装并配置环境变量

## 第一步：初始化数据库

启动 MySQL 后，依次执行 SQL 脚本：

```bash
# 1. 建库建表
mysql -u root -p < springboot_backend/querytable.sql

# 2. 插入测试数据
mysql -u root -p < springboot_backend/test_data.sql
```

数据库配置（在 `application.yml` 中）：
- 数据库名：`labsys`
- 用户名：`root`
- 密码：`Ykt@17720516407`
- 端口：`3306`（默认）

如需修改数据库连接信息，编辑 `springboot_backend/src/main/resources/application.yml`。

## 第二步：启动后端服务

```bash
cd springboot_backend
.\mvnw.cmd spring-boot:run
```

或直接在 IDE 中运行 `com.lab.App` 主类。

**验证**: 控制台输出 `=== 系统启动，执行初始化任务 ===` 即表示启动成功。后端监听 **8084** 端口。

## 第三步：启动前端服务

打开新的终端窗口：

```bash
cd vue_front/vue_front
npm install    # 首次运行需要安装依赖
npm run serve
```

**验证**: 访问 http://localhost:8083，应该能看到登录页面。

## 第四步：测试系统

### 1. 使用测试账号登录

测试数据已内置以下账号，无需注册即可使用：

| 角色 | 用户ID | 用户名 | 邮箱 | 密码 |
|------|--------|--------|------|------|
| 管理员 | admin001 | 系统管理员 | admin@lab.com | admin123 |
| 普通用户 | 2021001 | 张三 | zhangsan@test.com | 123456 |
| 普通用户 | 2021002 | 李四 | lisi@test.com | 123456 |

访问 http://localhost:8083/login，输入用户名和密码即可登录。

### 2. 注册新账号（可选）

访问 http://localhost:8083/register

- 注册普通用户：填写信息后直接提交
- 注册管理员：额外填写管理员密钥，固定为 **11111**

### 3. 修改密码

访问 http://localhost:8083/updatePassword

需提供用户名/邮箱/生日中至少一项，以及旧密码和新密码。

## 常见问题排查

### Q1: 端口被占用

**正常停止项目**：端口会立即释放，不会被继续占用。

**异常停止后端口残留**：

```bash
# 查找占用端口的进程
netstat -ano | findstr :8084    # 后端端口
netstat -ano | findstr :8083    # 前端端口

# 结束进程（替换PID为实际进程号）
taskkill /PID <PID> /F
```

或修改端口：
- 后端：修改 `application.yml` 中的 `server.port`
- 前端：修改 `vue.config.js` 中的 `devServer.port`，同时修改 `src/utils/request.js` 中的 `baseURL`

### Q2: 登录时提示网络错误

**可能原因**:
- 后端服务未启动
- 后端端口不是 8084

**解决方案**:
1. 确认后端已启动并监听 **8084** 端口
2. 检查 `src/utils/request.js` 中的 baseURL 为 `http://localhost:8084/api`
3. 查看浏览器控制台的网络请求详情

### Q3: 后端启动报 JDK 版本错误

Spring Boot 2.7.x 最高支持 JDK 17。如果使用 JDK 24 启动报错，请切换到 JDK 8 或 JDK 17。

### Q4: 注册时提示 adkey 错误

注册管理员需要输入管理员密钥，固定为 **11111**（5个1）。注册普通用户不需要填写此字段。

### Q5: 修改密码失败

1. 确认用户名和旧密码正确
2. 系统会先用身份信息+旧密码登录验证身份，验证通过后才能修改密码

## 功能概览

### 管理员功能
- 用户管理：增/删/查普通用户
- 预约管理：审批/拒绝预约申请
- 设备管理：查看设备、修改设备状态、更新使用频率
- 个人信息：修改姓名、邮箱、生日、密码
- 日志查看：查看操作日志

### 普通用户功能
- 申请预约：选择设备、日期、时间段提交预约
- 我的预约：查看预约状态，取消未审核的预约
- 个人信息：修改姓名、邮箱、生日、密码

## 开发调试技巧

### 查看 localStorage 中的 token

打开浏览器控制台，输入：
```javascript
localStorage.getItem('token')
```

### 手动清除 token（模拟登出）
```javascript
localStorage.removeItem('token')
location.reload()
```

### 查看 Vue 组件状态

安装 Vue Devtools 浏览器扩展，可以实时查看组件数据和状态。

## 技术支持

如遇问题，请检查：
1. 后端日志 - 查看是否有错误信息
2. 前端控制台 - 查看 JavaScript 错误
3. 网络请求 - 查看 HTTP 请求状态码和响应内容