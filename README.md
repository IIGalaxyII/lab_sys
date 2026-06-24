# 高校实验室设备预约管理系统

## 项目简介

这是一个基于Spring Boot + Vue 3的高校实验室设备预约管理系统，包含管理员和普通用户两个角色，实现了设备管理、预约管理、用户管理等功能。

## 技术栈

### 后端
- **框架**: Spring Boot 2.7.18
- **持久层**: MyBatis 2.3.1
- **数据库**: MySQL 8.0.46
- **认证**: JWT (jjwt 0.9.1)
- **其他**: Lombok

### 前端
- **框架**: Vue 3.2.13 (Composition API)
- **UI组件库**: Element Plus 2.x
- **路由**: Vue Router 4.x
- **HTTP客户端**: Axios 1.x
- **构建工具**: Vue CLI 5.0

## 项目结构

```
lab_admin_sys/
├── springboot_backend/          # 后端项目
│   ├── src/main/java/com/lab/   # Java源代码
│   │   ├── controller/          # 控制器层
│   │   ├── service/             # 服务层
│   │   ├── mapper/              # 数据访问层
│   │   ├── entity/              # 实体类
│   │   ├── config/              # 配置类
│   │   ├── interceptor/         # 拦截器
│   │   ├── util/                # 工具类
│   │   └── exception/           # 异常处理
│   ├── src/main/resources/      # 资源文件
│   │   ├── mapper/              # MyBatis映射文件
│   │   ├── application.yml      # 应用配置
│   │   └── mybatis-config.xml   # MyBatis配置
│   ├── API_DOCUMENTATION.md     # API文档
│   ├── QUICK_START.md           # 后端快速启动
│   └── pom.xml                  # Maven依赖
│
├── vue_front/vue_front/         # 前端项目
│   ├── src/                     # 源代码
│   │   ├── views/               # 页面组件
│   │   │   ├── Login.vue        # 登录页面
│   │   │   ├── Register.vue     # 注册页面
│   │   │   ── UpdatePassword.vue # 修改密码页面
│   │   ├── router/              # 路由配置
│   │   ├── utils/               # 工具类
│   │   ├── App.vue              # 根组件
│   │   ── main.js              # 入口文件
│   ├── FRONTEND_README.md       # 前端说明
│   ├── DEVELOPMENT_SUMMARY.md   # 开发总结
│   └── package.json             # npm依赖
│
├── QUICK_START_GUIDE.md         # 快速启动指南（前后端）
├── FRONTEND_FILE_CHECKLIST.md   # 前端文件清单
── 前端开发完成报告.md           # 前端开发报告
```

## 快速启动

### 方式一：使用快速启动指南（推荐）

查看 [QUICK_START_GUIDE.md](./QUICK_START_GUIDE.md) 获取详细的启动步骤。

### 方式二：手动启动

#### 1. 启动后端

```bash
cd springboot_backend
mvn spring-boot:run
```

后端运行在: http://localhost:8080

#### 2. 启动前端

打开新的终端窗口：

```bash
cd vue_front/vue_front
npm install    # 首次运行需要
npm run serve
```

前端运行在: http://localhost:8083

### 3. 测试系统

1. 访问 http://localhost:8083/register 注册管理员账号
   - 用户ID: admin001
   - 用户名: 管理员
   - 邮箱: admin@test.com
   - 生日: 任意日期
   - 密码: admin123
   - 管理员密钥(adkey): **11111**

2. 访问 http://localhost:8083/login 登录系统
   - 用户名: 管理员
   - 密码: admin123

## 功能特性

### 已完成功能 ✅

#### 认证模块
- ✅ 管理员登录
- ✅ 管理员注册
- ✅ 修改密码
- ✅ JWT Token认证
- ✅ 响应式登录界面

#### 管理员功能
- ✅ **数据统计（Dashboard）** - 用户、设备、预约统计概览
- ✅ **用户管理** - 查看、添加、编辑、删除普通用户
- ✅ **设备管理** - 查看设备列表、更新设备状态、更新使用频率
- ✅ **预约管理** - 查看所有预约、筛选待审核预约、批准/拒绝预约

#### 响应式设计
- ✅ 桌面端布局（≥800px）
- ✅ 移动端布局（<800px）
- ✅ 自适应界面切换

### 待开发功能 🚧

#### 普通用户功能
- ⏳ 用户登录
- ⏳ 设备浏览
- ⏳ 设备预约
- ⏳ 我的预约查看
-  个人信息管理

#### 管理员功能增强
- ⏳ 分页功能（用户、设备、预约列表）
- ⏳ 数据导出Excel
- ⏳ 批量操作功能
- ⏳ 高级搜索和筛选
- ⏳ 操作日志查看
- ⏳ 数据图表展示

## 文档索引

### 后端文档
- [API文档](./springboot_backend/API_DOCUMENTATION.md) - 完整的API接口说明
- [后端快速启动](./springboot_backend/QUICK_START.md) - 后端启动指南
- [后端功能总结](./springboot_backend/完整功能总结.md) - 后端功能列表

### 前端文档
- [前端说明](./vue_front/vue_front/FRONTEND_README.md) - 前端使用文档
- [开发总结](./vue_front/vue_front/DEVELOPMENT_SUMMARY.md) - 前端开发总结
- [文件清单](./FRONTEND_FILE_CHECKLIST.md) - 前端文件列表
- [开发报告](./前端开发完成报告.md) - 完整的前端开发报告
- [管理员功能指南](./vue_front/vue_front/ADMIN_GUIDE.md) - 管理员功能详细使用说明

### 通用文档
- [快速启动指南](./QUICK_START_GUIDE.md) - 前后端整体启动指南

## API接口概览

### 认证接口（无需Token）
- POST `/api/login/admin` - 管理员登录
- POST `/api/login/register/admin` - 管理员注册

### 管理员接口（需要Token）
- GET `/api/admin/profile` - 获取个人信息
- PUT `/api/admin/profile` - 更新个人信息
- POST `/api/admin/profile/change-password` - 修改密码
- GET `/api/admin/users` - 查询所有用户
- POST `/api/admin/users` - 添加用户
- DELETE `/api/admin/users/{id}` - 删除用户
- PUT `/api/admin/users` - 更新用户
- GET `/api/admin/equipment` - 查询所有设备
- PUT `/api/admin/equipment/state` - 更新设备状态
- GET `/api/admin/reservations` - 查询所有预约
- PUT `/api/admin/reservations/permit` - 审批预约

详细API文档请查看: [API_DOCUMENTATION.md](./springboot_backend/API_DOCUMENTATION.md)

## 注意事项

### 1. 数据库配置
确保MySQL数据库已创建并配置：
- 数据库名: `lab_system`（或根据application.yml配置）
- 用户名: root
- 密码: 根据实际情况配置

### 2. 跨域配置
后端已在 `WebConfig.java` 中配置CORS，允许前端跨域访问。

### 3. Token管理
- Token存储在localStorage中
- 有效期由JWT配置决定
- 401错误时自动清除并跳转登录页

### 4. 已移除的功能
以下功能在后端未实现，前端已禁用：
- ❌ QQ登录
- ❌ 微信登录
- ❌ 支付宝登录
- ❌ 头像上传

## 开发环境要求

- **Node.js**: v16+
- **Java JDK**: 11+
- **Maven**: 3.6+
- **MySQL**: 8.0+
- **浏览器**: Chrome、Firefox、Edge等现代浏览器

## 常见问题

### Q: 前端启动失败？
A: 检查端口是否被占用，或修改vue.config.js中的端口配置。

### Q: 登录时提示网络错误？
A: 确保后端服务已启动并运行在8080端口。

### Q: 注册时提示adkey错误？
A: adkey必须为 "11111"（5个1）。

### Q: 如何查看API请求详情？
A: 打开浏览器开发者工具（F12），切换到Network标签查看。

更多问题请查看 [QUICK_START_GUIDE.md](./QUICK_START_GUIDE.md)

## 后续开发计划

### Phase 1: 普通用户功能（2-3周）
- 用户登录页面
- 设备浏览和搜索
- 设备预约功能
- 我的预约查看
- 个人信息管理

### Phase 2: 管理员功能增强（2-3周）
- 分页功能
- 数据导出Excel
- 批量操作
- 高级搜索和筛选
- 操作日志查看
- 数据图表展示（ECharts）

### Phase 3: 功能完善（2-3周）
- 头像上传功能
- 第三方登录集成
- 消息通知系统
- 主题切换（深色/浅色）
- 性能优化

## 贡献指南

欢迎提交Issue和Pull Request来改进本项目。

### 提交Issue
- 描述问题或建议
- 提供复现步骤
- 附上相关截图或日志

### 提交PR
- Fork本仓库
- 创建功能分支
- 提交代码更改
- 编写测试用例
- 更新文档
- 提交Pull Request

## 许可证

本项目仅供学习和研究使用。

## 联系方式

如有问题或建议，欢迎联系开发团队。

---

**最后更新**: 2026年6月13日  
**版本**: v2.0.0  
**状态**: ✅ 前端基础功能和管理员功能已完成，后端功能完整
