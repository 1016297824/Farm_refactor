# Farm Optimize — 农场综合管理系统

基于 Spring Boot + Vue 2 的农场与餐厅综合管理系统，支持员工管理、考勤、销售、采购、预约点餐、报表导出等功能，涵盖 6 种角色权限。

## 技术栈

| 层      | 技术                                                    |
| ------ | ----------------------------------------------------- |
| 后端     | Spring Boot 2.2.5 / MyBatis 2.1.3 / MySQL 8 / Java 11 |
| 前端     | Vue 2 / Element UI / Axios / Bootstrap                |
| 部署     |  腾讯云 CVM / EdgeOne Pages CDN                          |
| AI 工具链 | TRAE / Spec Kit / OpenSpec /  ui-ux-pro-max           |

## 功能模块

- **超级管理员**：全局员工管理、考勤报表、销售/采购报表导出
- **农场经理**：农场员工管理、考勤、销售/采购统计
- **农场员工**：农产品管理、销售录入、肥料/原料管理
- **餐厅经理**：餐厅员工管理、考勤、物料/维修管理
- **餐厅员工**：桌台管理、点餐结算、维修上报
- **顾客**：预约、点餐、修改密码

## 项目结构

```
Farm_optimize/
├── backend/                    # Spring Boot 后端
│   ├── src/main/java/com/farm/
│   │   ├── controller/         # 7 个 Controller（80+ API 端点）
│   │   ├── service/            # 21 个 Service
│   │   ├── repository/         # 18 个 MyBatis Mapper 接口
│   │   ├── entity/             # 21 个 Entity + DTO
│   │   ├── config/             # CORS / Web 配置
│   │   ├── interceptor/        # 7 个角色拦截器
│   │   └── component/          # Excel 导出 / 加密组件
│   ├── src/main/resources/
│   │   ├── mapper/             # 18 个 MyBatis Mapper XML
│   │   └── application.properties
│   └── pom.xml
├── frontend/                   # Vue 2 前端
│   ├── src/
│   │   ├── views/              # 32 个页面视图
│   │   ├── components/         # Navbar / AlertDialog
│   │   ├── api/                # 7 个 API 模块
│   │   └── router/             # Vue Router
│   └── package.json
├── 部署/                       # 部署配置
│   ├── docker-compose.yml
│   ├── nginx.conf
│   └── schema.sql
└── openspec/                   # OpenSpec 变更管理
```

## 快速开始

### 环境要求

- Java 11+
- Maven 3.6+
- Node.js 12+
- MySQL 8.0

### 后端启动

```bash
cd backend
mvn spring-boot:run
```

后端默认运行在 `http://localhost:8080`，需确保 MySQL 在 `localhost:3306` 运行且存在 `farm` 数据库。

### 前端启动

```bash
cd frontend
npm install --registry=https://registry.npmmirror.com
npm run serve
```

前端默认运行在 `http://localhost:8081`，API 请求通过 vue.config.js 代理转发到后端。

## AI 驱动重构

本项目采用 AI 编码工作流全流程辅助重构：

- **后端重构**：Spec Kit 规格驱动，将 Hibernate→MyBatis 框架迁移拆解为 86 个任务，8 阶段全量完成
- **前端重构**：OpenSpec 变更驱动，管理 UI 美化迭代；ui-ux-pro-max 负责页面优化
- **质量保障**：gstack 自动化 QA（32 页面全量验证，健康评分 100/100）+ AI 代码审查 + 工程架构审查

