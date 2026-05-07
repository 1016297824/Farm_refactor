# Implementation Plan: Hibernate 迁移至 MyBatis

**Branch**: `001-hibernate-to-mybatis` | **Date**: 2026-04-30 | **Spec**: [spec.md](./spec.md)
**Input**: Feature specification from `/specs/001-hibernate-to-mybatis/spec.md`

## Summary

将后端持久层框架从 Hibernate/JPA 替换为 MyBatis，同时保持所有 7 个 Controller 的 API 契约不变。采用"最小变更原则"，变更范围严格限定在 Entity（去 JPA 注解）、Repository→Mapper、新增 Mapper XML、配置文件四个层面，Service 层仅做最小适配，Controller 层零改动。

## Technical Context

**Language/Version**: Java 11
**Primary Dependencies**: Spring Boot 2.2.5.RELEASE, MyBatis-Spring-Boot-Starter 2.1.3 (替代 spring-boot-starter-data-jpa)
**Storage**: MySQL 8 (localhost:3306/farm)，已有 schema 由原 Hibernate ddl-auto=update 创建
**Testing**: Spring Boot Test (JUnit 5 / Spring Test)
**Target Platform**: Windows/Linux server (内网部署)
**Project Type**: web-service (Spring Boot 后端 + Vue.js 前端)
**Performance Goals**: 保持现有响应速度不变，无性能退化
**Constraints**:
- 最小变更原则：Controller 零改动，Service 最小改动
- API 契约完全兼容：前端无需任何修改
- 数据库 schema 不变：复用已有表结构
- Java 11 + Spring Boot 2.2.5 版本锁定不变
**Scale/Scope**: 21 个 Entity → POJO，21 个 Repository → Mapper 接口 + XML，60+ 个自定义 HQL 查询 → SQL，7 个 Controller 不变，21 个 Service 最小适配

## Constitution Check

*GATE: Must pass before Phase 0 research. Re-check after Phase 1 design.*

Constitution 为默认模板（未自定义），无特定门禁规则。以下为基于项目规则的检查：

| 原则 | 状态 | 说明 |
|------|------|------|
| 最小变更原则 | ✅ PASS | 变更限定在持久层，Controller 零改动 |
| API 兼容性 | ✅ PASS | 所有端点路径/参数/响应格式保持一致 |
| 数据完整性 | ✅ PASS | 复用已有数据库 schema |
| 事务一致性 | ✅ PASS | Spring @Transactional 继续有效 |

## Project Structure

### Documentation (this feature)

```text
specs/001-hibernate-to-mybatis/
├── spec.md              # 功能规格说明 (/speckit-specify 输出)
├── plan.md              # 本文件 (/speckit-plan 输出)
├── research.md          # Phase 0 技术调研输出
├── data-model.md        # Phase 1 数据模型设计
├── quickstart.md        # Phase 1 快速开始指南
├── contracts/           # Phase 1 API 契约文档
│   └── api-contract.md  # API 接口契约
└── tasks.md             # Phase 2 任务拆分 (/speckit-tasks 输出)
```

### Source Code (repository root) — 变更范围

```text
backend/
├── pom.xml                              # [修改] 移除 JPA 依赖，添加 MyBatis 依赖
├── src/main/resources/
│   ├── application.properties           # [修改] 移除 JPA 配置，添加 MyBatis 配置
│   └── mapper/                          # [新增] MyBatis Mapper XML 目录
│       ├── StaffMapper.xml              # [新增]
│       ├── CustomerMapper.xml           # [新增]
│       ├── PositionMapper.xml           # [新增]
│       ├── AttendanceMapper.xml         # [新增]
│       ├── SaleMapper.xml               # [新增]
│       ├── SaleNoMapper.xml             # [新增]
│       ├── ProductMapper.xml            # [新增]
│       ├── OrderingMapper.xml           # [新增]
│       ├── OrderingCompleteMapper.xml   # [新增]
│       ├── PurchaseMapper.xml           # [新增]
│       ├── ReserveMapper.xml            # [新增]
│       ├── RepairMapper.xml             # [新增]
│       ├── ConsumptionMapper.xml        # [新增]
│       ├── ProduceMapper.xml            # [新增]
│       ├── FertilizerMapper.xml         # [新增]
│       ├── DiningTableMapper.xml        # [新增]
│       ├── MenuMapper.xml               # [新增]
│       ├── RestaurantMaterialMapper.xml # [新增]
│       └── SaleNoMapper.xml             # [新增]
└── src/main/java/com/farm/
    ├── entity/                           # [修改] 21 个 Entity 去除 JPA 注解，保留 Lombok
    │   ├── Staff.java                   # [修改] @Entity/@Column 等 → 纯 POJO
    │   ├── Customer.java                # [修改] 同上
    │   ├── ... (其余 19 个实体)          # [修改] 同上
    │   └── body/                        # [不改] DTO 类不受影响
    ├── repository/                       # [重构] Repository → Mapper 接口
    │   ├── StaffRepository.java         # [重写] → StaffMapper.java
    │   ├── CustomerRepository.java      # [重写] → CustomerMapper.java
    │   ├── ... (其余 19 个 Repository)   # [重写] → 对应 Mapper
    │   └── impl/                        # [删除] CustomizedRepository 不再需要
    │       ├── CustomizedRepository.java
    │       └── CustomizedRepositoryImpl.java
    ├── service/                          # [最小修改] 适配新 Mapper 调用方式
    │   ├── StaffService.java            # [微调] save/delete 方法签名适配
    │   ├── ... (其余 Service)            # [微调] 同上
    └── controller/                       # [不改] 7 个 Controller 完全不动
        ├── SuperManagerController.java
        ├── FarmManagerController.java
        └── ... (其余 Controller)

frontend/                                  # [不改] Vue.js 前端完全不动
```

**Structure Decision**: 采用 Option 2 (Web application) 结构。变更集中在 `backend/src/main/java/com/farm/entity/`、`backend/src/main/java/com/farm/repository/`、`backend/src/main/resources/mapper/`、`backend/pom.xml`、`backend/src/main/resources/application.properties` 五个区域。

## Complexity Tracking

> 无 Constitution 违规需要记录。本迁移的复杂度主要来自：
>
> - **关联关系映射**: 6 组 @ManyToOne/@OneToMany 关联需在 MyBatis resultMap 中手动维护
> - **HQL→SQL 转换**: 60+ 个自定义 JPQL 查询需逐一手写等价 SQL
> - **级联操作**: delete cascade 行为需在 SQL 或代码中显式处理
>
> 这些复杂度是框架替换固有的，已通过分阶段实施策略（Entity→Mapper→Service→验证）控制风险。
