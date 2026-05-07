# Quickstart: Hibernate → MyBatis Migration

**Feature**: Hibernate 迁移至 MyBatis
**Date**: 2026-04-30

## 实施顺序概览

```
Phase A: 基础设施准备
  ├─ A1. pom.xml 依赖替换
  ├─ A2. application.properties 配置迁移
  └─ A3. 创建 mapper/ 目录结构

Phase B: Entity 层改造（21 个 POJO）
  ├─ B1. 核心实体组（Staff, Position, Customer）
  ├─ B2. 农场业务实体（Product, Sale, SaleNo, Produce, Fertilizer, Purchase, Consumption）
  ├─ B3. 餐厅业务实体（DiningTable, Menu, Reserve, Ordering, OrderingComplete, RestaurantMaterial）
  └─ B4. 辅助实体（Attendance, Repair）

Phase C: Mapper 层实现（21 个 Mapper 接口 + XML）
  ├─ C1. 核心 Mapper 组（StaffMapper, PositionMapper, CustomerMapper）
  ├─ C2. 农场 Mapper 组（ProductMapper, SaleMapper, ...）
  ├─ C3. 餐厅 Mapper 组（DiningTableMapper, MenuMapper, ...）
  └─ C4. 辅助 Mapper 组（AttendanceMapper, RepairMapper）

Phase D: Service 层适配（21 个 Service 微调）
  ├─ D1. Repository 注入改为 Mapper 注入
  ├─ D2. save/delete 方法签名适配
  └─ D3. saveAll 批量操作适配

Phase E: 清理与验证
  ├─ E1. 删除 CustomizedRepository 相关文件
  ├─ E2. 编译验证（mvn compile）
  └─ E3. 启动验证 + API 冒烟测试
```

## 关键文件变更清单

### 必须修改的文件（共 ~70 个文件操作）

| 操作 | 文件数 | 说明 |
|------|--------|------|
| 修改 pom.xml | 1 | 移除 JPA，添加 MyBatis |
| 修改 application.properties | 1 | 配置迁移 |
| 修改 Entity (.java) | 21 | 去除 JPA 注解 |
| 重写 Repository → Mapper (.java) | 21 | 接口转换 |
| 新增 Mapper XML (.xml) | 21 | SQL 映射 |
| 微调 Service (.java) | ~15 | 注入和方法适配 |
| 删除文件 | 2 | CustomizedRepository + Impl |
| **合计** | **~82** | |

### 完全不动的文件

- 7 个 Controller (.java) — **零改动**
- 所有 Interceptor (.java) — 不动
- Component (EncryptorComponent, ExcelComponent) — 不动
- entity/body/ 下所有 DTO — 不动
- config/WebConfig.java — 不动
- frontend/ 整个目录 — 不动

## 风险点与应对

| 风险 | 影响 | 应对措施 |
|------|------|----------|
| 关联查询 resultMap 不完整 | API 返回缺少嵌套对象 | 逐一对比原 JSON 响应验证 |
| HQL→SQL 语法差异 | 查询结果不正确 | 每个 SQL 手动验证或单元测试 |
| 级联删除行为变化 | 删除主记录时子记录残留 | 在 Service 层显式先删子记录 |
| 字段命名映射错误 | null 值出现在响应中 | 开启 camelCase 映射 + 逐字段检查 |
| 事务边界变化 | 数据不一致 | 保持 @Transactional 不变 + 集成测试 |

## 验证清单

- [ ] `mvn compile` 编译通过，无 JPA/Hibernate import 残留
- [ ] 应用启动成功，无 MyBatis mapper 扫描异常
- [ ] 数据库连接正常，所有表可访问
- [ ] POST `/api/login` 返回正确用户信息和 authority
- [ ] GET `/api/superManager/getStaff` 返回含 position 信息的完整 staffList
- [ ] POST `/api/superManager/addStaff` 添加成功且能查询到新记录
- [ ] POST `/api/superManager/deleteStaff/{username}` 删除成功且级联删除考勤
- [ ] Excel 导出接口返回正确的 .xls 文件
- [ ] 前端页面逐功能手动验证通过
