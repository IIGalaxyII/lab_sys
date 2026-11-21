# 高校实验室设备预约管理系统（lab_sys）

基于 Spring Boot + MyBatis + PostgreSQL + Vue 3 的双角色实验室设备预约与管理系统，支持管理员审核与设备维护、普通用户预约与取消，并完成键盘操作、屏幕阅读器语义和系统高对比度适配。

## 技术栈

| 层次 | 技术 |
| --- | --- |
| 后端 | Java 17+（当前 JDK 21）、Spring Boot 3.2.5、MyBatis 3.0.3 |
| 数据库 | PostgreSQL 12+（当前使用 18） |
| 认证 | JWT（jjwt 0.12.5）+ 拦截器权限校验 |
| 前端 | Vue 3.5、Vue Router 4、Pinia、Element Plus 2.14、Axios、Vite 7 |
| 构建 | Maven、npm |

## 功能

**管理员**
- 数据概览：用户数、设备数、可用设备数、待审核预约数
- 用户管理：新增、编辑、删除普通用户
- 设备管理：查看设备、变更状态（可用/占用/维修/报废）、刷新使用频率
- 预约管理：查看全部/待审核预约，批准或拒绝
- 维护日志：查看全部日志或按日期范围筛选
- 个人信息与密码修改

**普通用户**
- 浏览设备及状态、使用频率
- 按日期与时间段提交预约
- 查看我的预约、取消待审核预约
- 个人信息与密码修改

**通用**
- 管理员/普通用户双身份登录，支持用户名、邮箱、生日三种登录方式
- 注册普通用户/管理员（管理员需密钥）
- 未登录时可通过“用户名/邮箱/生日 + 旧密码”修改密码
- JWT 鉴权、角色路由守卫、越权接口拦截

## 目录结构

```
lab_sys/
├── springboot_backend/                 # 后端
│   ├── src/main/java/com/lab/
│   │   ├── controller/                 # 接口层
│   │   ├── service/                    # 业务层
│   │   ├── mapper/                     # MyBatis Mapper 接口
│   │   ├── entity/                     # 实体类
│   │   ├── config/ interceptor/ util/  # 配置、鉴权、工具
│   │   ├── common/ exception/          # 统一响应与异常
│   │   └── App.java
│   ├── src/main/resources/
│   │   ├── application.yml             # 数据库与服务配置
│   │   ├── mapper/*.xml                # SQL 映射
│   │   └── mybatis-config.xml
│   ├── querytable.sql                  # PostgreSQL 建库建表脚本
│   ├── test_data.sql                   # 测试数据
│   └── pom.xml
├── vue_front/
│   └── vue_front/                      # 前端工程（注意双层目录）
│       ├── src/
│       │   ├── views/                  # 登录、注册、管理员端、用户端
│       │   ├── components/             # 布局、资料表单、改密表单
│       │   ├── router/ stores/ utils/  # 路由、状态、请求与无障碍工具
│       │   └── styles/global.css       # 全局样式与高对比度适配
│       ├── index.html
│       ├── vite.config.js
│       └── package.json
├── demo/                               # 独立示例模块（非主系统）
├── QUICK_START_GUIDE.md                # 快速启动指南
└── 无障碍适配说明与测试记录.md          # 无障碍适配说明与测试记录
```

## 环境要求

- JDK 17 及以上
- Maven 3.6+
- PostgreSQL 12 及以上，默认端口 `5432`
- Node.js 18+（建议 20/22/24）、npm

## 快速开始

### 1. 初始化数据库

先创建数据库（若不存在）：

```bash
psql -U postgres -c "CREATE DATABASE labsys;"
```

**必须显式指定 `-d labsys`** 执行脚本，否则表会建到默认的 `postgres` 库：

```bash
psql -U postgres -d labsys -f springboot_backend/querytable.sql
psql -U postgres -d labsys -f springboot_backend/test_data.sql
```

验证：

```bash
psql -U postgres -d labsys -c "\dt"
```

应看到 `adm`、`nuser`、`equipment`、`restore_table`、`log`、`nview`。

### 2. 配置后端

编辑 [springboot_backend/src/main/resources/application.yml](springboot_backend/src/main/resources/application.yml)：

```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/labsys?currentSchema=public&characterEncoding=UTF-8
    username: postgres
    password: 你的PostgreSQL密码
    driver-class-name: org.postgresql.Driver
server:
  port: 8084
```

### 3. 启动后端

```bash
cd springboot_backend
mvn spring-boot:run
```

或打包运行：

```bash
mvn -DskipTests package
java -jar target/springboot_backend.jar
```

后端地址：`http://localhost:8084`，接口前缀 `/api`。

### 4. 启动前端

前端工程在**双层目录** `vue_front/vue_front` 下：

```bash
cd vue_front/vue_front
npm install
npm run dev
```

访问：`http://localhost:8083`

生产构建：

```bash
npm run build
npm run preview
```

默认接口地址为 `http://localhost:8084/api`，可通过环境变量覆盖：

```bash
# PowerShell
$env:VITE_API_BASE_URL="http://localhost:8084/api"; npm run dev
```

## 默认测试账号

| 角色 | 账号 | 密码 | 说明 |
| --- | --- | --- | --- |
| 管理员 | `admin001` | `admin123` | 也可用邮箱 `admin@lab.com` 或生日 `2000-01-01` 登录 |
| 普通用户 | `2021001` | `123456` | 张三，`zhangsan@test.com` |
| 普通用户 | `2021002` | `123456` | 李四，`lisi@test.com` |

> 账号数据来自 `test_data.sql`，仅用于本地开发测试。

## 主要接口

| 模块 | 方法与路径 | 说明 |
| --- | --- | --- |
| 认证 | `POST /api/login/admin`、`/api/login/user` | 登录，返回 JWT |
| 认证 | `POST /api/login/register/admin`、`/register/user` | 注册 |
| 认证 | `POST /api/login/change-password` | 登录页身份校验改密 |
| 管理员 | `GET/PUT /api/admin/profile`，`POST /api/admin/profile/change-password` | 个人信息与改密 |
| 管理员 | `GET/POST/PUT /api/admin/users`，`DELETE /api/admin/users/{id}` | 用户管理 |
| 管理员 | `GET /api/admin/equipment`，`PUT /api/admin/equipment/state` | 设备管理 |
| 管理员 | `GET /api/admin/reservations`、`/pending`，`PUT /api/admin/reservations/permit` | 预约审核 |
| 管理员 | `GET /api/admin/logs`，`POST /api/admin/logs/date-range` | 维护日志 |
| 用户 | `GET/PUT /api/user/profile`，`POST /api/user/profile/change-password` | 个人信息与改密 |
| 用户 | `GET/POST /api/user/reservations`，`DELETE /api/user/reservations/{date}/{time}/{equipmentId}` | 我的预约 |
| 通用 | `GET /api/equipment` | 设备列表 |

所有受保护接口需携带请求头：`Authorization: Bearer <token>`。

## 无障碍支持

界面已按《Windows 应用无障碍可访问性开发指南》完成适配：

- 所有表单控件具备可访问名称；表格使用 `caption` 与表头作用域
- 支持“跳到主要内容”、完整键盘操作、路由切换焦点管理与弹窗焦点陷阱
- 使用 `aria-live` 实时播报登录、提交、删除等结果
- 支持应用内高对比度开关、Windows 强制颜色模式与系统高对比度偏好
- 状态同时以文字和颜色表达，支持 200% 缩放重排与减少动态效果

详细说明与测试记录见 [无障碍适配说明与测试记录.md](无障碍适配说明与测试记录.md)。

## 常见问题

**1. `npm install` 报 `ENOENT: no such file or directory, open '...\vue_front\package.json'`**

当前目录不是前端工程根目录。前端在 `vue_front/vue_front`，请进入该目录后再安装。

**2. 应用报 `relation "restore_table" does not exist`，但数据库里能看到表**

表被建到了错误的数据库（通常是 `postgres`）。请确认 `application.yml` 连接的是 `labsys`，并使用 `-d labsys` 重新执行建表脚本。

**3. 端口被占用**

后端默认 `8084`，前端默认 `8083`。可在 `application.yml` 修改 `server.port`，或使用 `npm run dev -- --port 8084` 调整前端端口。

**4. 修改密码后登录失败**

密码以 SHA-256 + Base64 存储。请确认使用新密码登录；如仍失败，可重新执行 `test_data.sql` 恢复测试账号。

## 说明

- 前端 `node_modules`、`dist` 已被忽略，可随时删除后重新 `npm install` / `npm run build`。
- 若需将重建的前端提交到 Git，请先移除原无效子模块引用，再添加目录：

```bash
git rm --cached vue_front/vue_front
git add vue_front/vue_front
```
