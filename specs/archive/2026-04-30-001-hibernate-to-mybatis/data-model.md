# Data Model: Hibernate → MyBatis Migration

**Feature**: Hibernate 迁移至 MyBatis
**Date**: 2026-04-30

## Entity Overview (21 个实体)

### 实体关系图（文本表示）

```
Position 1──N Staff
                │
                ├──N Attendance
                │
Customer 1──N Reserve N──1 DiningTable
                │
                └──N Ordering N──1 Menu

SaleNo 1──N Sale N──1 Product
                    ├──N Consumption
                    ├──N Produce
                    └──N Purchase
                              ├──N Fertilizer
                              └──N RestaurantMaterial

Customer 1──N OrderingComplete
Repair (独立)
```

---

## 实体详细定义

### E01: Staff（员工）

| 字段 | 类型 | Java 名称 | DB 列名(推测) | 约束 |
|------|------|-----------|--------------|------|
| ID | int | id | id | PK, AUTO_INCREMENT |
| 用户名 | String | username | username | UNIQUE |
| 密码 | String | password | password | - |
| 姓名 | String | name | name | - |
| 电话 | String | telNumber | tel_number | - |
| 邮箱 | String | email | email | @Email |
| 地址 | String | address | address | - |
| 身份证号 | String | idCardNo | id_card_no | - |

**关联**:
- `position` → Position (ManyToOne, 外键: position_id)
- `attendanceList` → List\<Attendance\> (OneToMany, mappedBy=staff, cascade=REMOVE)

---

### E02: Position（职位）

| 字段 | 类型 | Java 名称 | DB 列名(推测) | 约束 |
|------|------|-----------|--------------|------|
| ID | int | id | id | PK, AUTO_INCREMENT |
| 名称 | String | name | name | - |
| 地点 | String | location | location | - |
| 基本工资 | double | basicSalary | basic_salary | - |
| 权限 | String | authority | authority | - |

**关联**:
- 被 Staff 多对一引用

---

### E03: Customer（顾客）

| 字段 | 类型 | Java 名称 | DB 列名(推测) | 约束 |
|------|------|-----------|--------------|------|
| ID | int | id | id | PK, AUTO_INCREMENT |
| 用户名 | String | username | username | UNIQUE, NOT NULL |
| 密码 | String | password | password | NOT NULL |
| 姓名 | String | name | name | - |

**关联**:
- `reserveList` → List\<Reserve\> (OneToMany)
- `completeList` → List\<OrderingComplete\> (OneToMany)

---

### E04: Attendance（考勤）

| 字段 | 类型 | Java 名称 | DB 列名(推测) | 约束 |
|------|------|-----------|--------------|------|
| ID | int | id | id | PK, AUTO_INCREMENT |
| 时间 | LocalDateTime | insertTime | insert_time | - |
| 工时 | double | workingHours | working_hours | - |

**关联**:
- `staff` → Staff (ManyToOne, 外键: staff_id)

---

### E05: SaleNo（销售编号）

| 字段 | 类型 | Java 名称 | DB 列名(推测) | 约束 |
|------|------|-----------|--------------|------|
| ID | int | id | id | PK, AUTO_INCREMENT |
| 编号 | String | no | no | - |
| 时间 | LocalDateTime | insertTime | insert_time | - |

**关联**:
- 被 Sale 多对一引用

---

### E06: Sale（销售记录）

| 字段 | 类型 | Java 名称 | DB 列名(推测) | 约束 |
|------|------|-----------|--------------|------|
| ID | int | id | id | PK, AUTO_INCREMENT |
| 数量 | double | amount | amount | - |
| 单价 | double | price | price | - |

**关联**:
- `saleNo` → SaleNo (ManyToOne)
- `product` → Product (ManyToOne)

---

### E07: Product（产品/农产品）

| 字段 | 类型 | Java 名称 | DB 列名(推测) | 约束 |
|------|------|-----------|--------------|------|
| ID | int | id | id | PK, AUTO_INCREMENT |
| 名称 | String | name | name | - |
| 单位 | String | unit | unit | - |
| 数量 | double | amount | amount | - |
| 安全库存 | double | safeAmount | safe_amount | - |
| 类型 | String | productType | product_type | - |
| 基准数量 | double | baseAmount | base_amount | - |
| 基准单位 | String | baseUnit | base_unit | - |

**关联**:
- 被 Sale、Consumption、Produce、Purchase 多对一引用

---

### E08: Consumption（消耗）

| 字段 | 类型 | Java 名称 | DB 列名(推测) | 约束 |
|------|------|-----------|--------------|------|
| ID | int | id | id | PK, AUTO_INCREMENT |
| 时间 | LocalDateTime | insertTime | insert_time | - |
| 数量 | double | amount | amount | - |

**关联**:
- `product` → Product (ManyToOne)

---

### E09: Produce（生产记录）

| 字段 | 类型 | Java 名称 | DB 列名(推测) | 约束 |
|------|------|-----------|--------------|------|
| ID | int | id | id | PK, AUTO_INCREMENT |
| 时间 | LocalDateTime | insertTime | insert_time | - |
| 数量 | double | amount | amount | - |

**关联**:
- `product` → Product (ManyToOne)

---

### E10: Purchase（采购记录）

| 字段 | 类型 | Java 名称 | DB 列名(推测) | 约束 |
|------|------|-----------|--------------|------|
| ID | int | id | id | PK, AUTO_INCREMENT |
| 时间 | LocalDateTime | insertTime | insert_time | - |
| 数量 | double | amount | amount | - |
| 单价 | double | price | price | - |

**关联**:
- `product` → Product (ManyToOne)
- `fertilizer` → Fertilizer (ManyToOne)
- `restaurantMaterial` → RestaurantMaterial (ManyToOne)

---

### E11: Fertilizer（肥料）

| 字段 | 类型 | Java 名称 | DB 列名(推测) | 约束 |
|------|------|-----------|--------------|------|
| ID | int | id | id | PK, AUTO_INCREMENT |
| 名称 | String | name | name | - |
| 数量 | double | amount | amount | - |
| 安全库存 | double | safeAmount | safe_amount | - |
| 单位 | String | unit | unit | - |
| 类型 | String | fertilizerType | fertilizer_type | - |

**关联**:
- 被 Purchase 多对一引用

---

### E12: RestaurantMaterial（餐厅物料）

| 字段 | 类型 | Java 名称 | DB 列名(推测) | 约束 |
|------|------|-----------|--------------|------|
| ID | int | id | id | PK, AUTO_INCREMENT |
| 名称 | String | name | name | - |
| 数量 | double | amount | amount | - |
| 安全库存 | double | safeAmount | safe_amount | - |
| 单位 | String | unit | unit | - |

**关联**:
- 被 Purchase 多对一引用

---

### E13: DiningTable（餐桌）

| 字段 | 类型 | Java 名称 | DB 列名(推测) | 约束 |
|------|------|-----------|--------------|------|
| ID | int | id | id | PK, AUTO_INCREMENT |
| 类型 | String | type | type | - |
| 座位数 | int | seat | seat | - |

**关联**:
- 被 Reserve 多对一引用

---

### E14: Reserve（预约）

| 字段 | 类型 | Java 名称 | DB 列名(推测) | 约束 |
|------|------|-----------|--------------|------|
| ID | int | id | id | PK, AUTO_INCREMENT |
| 编号 | String | no | no | - |
| 时间 | LocalDateTime | insertTime | insert_time | - |
| 开始时间 | LocalDateTime | startTime | start_time | - |
| 结束时间 | LocalDateTime | endTime | end_time | - |

**关联**:
- `customer` → Customer (ManyToOne)
- `diningTable` → DiningTable (ManyToOne)
- `orderingList` → List\<Ordering\> (OneToMany)

---

### E15: Menu（菜单项）

| 字段 | 类型 | Java 名称 | DB 列名(推测) | 约束 |
|------|------|-----------|--------------|------|
| ID | int | id | id | PK, AUTO_INCREMENT |
| 名称 | String | name | name | - |
| 价格 | double | price | price | - |
| 单位 | String | unite | unite | - |
| 类型 | String | type | type | - |

**关联**:
- 被 Ordering 多对一引用

---

### E16: Ordering（订单明细）

| 字段 | 类型 | Java 名称 | DB 列名(推测) | 约束 |
|------|------|-----------|--------------|------|
| ID | int | id | id | PK, AUTO_INCREMENT |
| 数量 | int | count | count | - |

**关联**:
- `reserve` → Reserve (ManyToOne)
- `menu` → Menu (ManyToOne)

---

### E17: OrderingComplete（已完成订单）

| 字段 | 类型 | Java 名称 | DB 列名(推测) | 约束 |
|------|------|-----------|--------------|------|
| ID | int | id | id | PK, AUTO_INCREMENT |
| 时间 | LocalDateTime | insertTime | insert_time | - |
| 预约编号 | String | reserveNo | reserve_no | - |
| 总价 | double | totalPrice | total_price | - |

**关联**:
- `customer` → Customer (ManyToOne)

---

### E18: Repair（维修记录）

| 字段 | 类型 | Java 名称 | DB 列名(推测) | 约束 |
|------|------|-----------|--------------|------|
| ID | int | id | id | PK, AUTO_INCREMENT |
| 时间 | LocalDateTime | insertTime | insert_time | - |
| 维修类型 | String | repairType | repair_type | - |
| 内容 | String | content | content | - |
| 原因 | String | cause | cause | - |
| 状态 | String | state | state | - |
| 价格 | double | price | price | - |

**关联**: 无外键关联（独立实体）

---

## MyBatis 映射策略汇总

| 实体 | 表名(推测) | resultMap 复杂度 | 关联处理方式 |
|------|-----------|-----------------|-------------|
| Staff | staff | 高（含 Position association） | 嵌套 select 查 Position |
| Position | position | 低 | 无关联 |
| Customer | customer | 低 | 一对多按需查询 |
| Attendance | attendance | 中（含 Staff association） | 嵌套 select |
| SaleNo | sale_no | 低 | 被 Sale 引用 |
| Sale | sale | 中（含 SaleNo + Product） | 双 association |
| Product | product | 低 | 被多个实体引用 |
| Consumption | consumption | 中（含 Product association） | 嵌套 select |
| Produce | produce | 中（含 Product association） | 嵌套 select |
| Purchase | purchase | 高（3 个 association） | 多 association |
| Fertilizer | fertilizer | 低 | 被 Purchase 引用 |
| RestaurantMaterial | restaurant_material | 低 | 被 Purchase 引用 |
| DiningTable | dining_table | 低 | 被 Reserve 引用 |
| Reserve | reserve | 高（Customer + DiningTable） | 双 association |
| Menu | menu | 低 | 被 Ordering 引用 |
| Ordering | ordering | 高（Reserve + Menu） | 双 association |
| OrderingComplete | ordering_complete | 中（含 Customer） | 嵌套 select |
| Repair | repair | 低 | 无关联 |
