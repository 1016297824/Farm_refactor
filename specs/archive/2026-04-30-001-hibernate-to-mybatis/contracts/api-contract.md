# API Contract: 后端接口契约

**Feature**: Hibernate 迁移至 MyBatis
**Date**: 2026-04-30
**约束**: 以下所有接口在迁移后必须保持完全一致的请求/响应格式

## 接口总览

| Controller | Base Path | 端点数 | 角色 |
|------------|-----------|--------|------|
| LoginController | `/api` | 2 | 公开 |
| CustomerController | `/api/customer` | 8 | 顾客 |
| FarmManagerController | `/api/farmManager` | 13 | 农场管理员 |
| FarmStaffController | `/api/farmStaff` | 18 | 农场员工 |
| RestaurantManagerController | `/api/restaurantManager` | 13 | 餐厅管理员 |
| RestaurantStaffController | `/api/restaurantStaff` | 17 | 餐厅员工 |
| SuperManagerController | `/api/superManager` | 9 | 超级管理员 |
| **合计** | — | **80** | — |

---

## C01: LoginController (`/api`)

| 方法 | 路径 | 功能 | 请求体 | 响应 |
|------|------|------|--------|------|
| POST | `/login` | 登录 | UserBody (username, password) | 含用户信息+authority 的 Map |
| POST | `/register` | 注册 | Customer 对象 | 注册结果消息 |

## C02: CustomerController (`/api/customer`)

| 方法 | 路径 | 功能 | 请求体 | 响应 |
|------|------|------|--------|------|
| GET | `/reserve/getReserve` | 查询预约列表 | - | 预约列表+分页信息 |
| POST | `/reserve/deleteReserve/{no}` | 取消预约 | 路径参数 no | 操作消息 |
| GET | `/reserveAdd/initDiningTable` | 初始化餐桌选择 | - | 餐桌列表 |
| POST | `/reserveAdd/doPage` | 餐桌分页查询 | PageBody | 分页餐桌列表 |
| POST | `/reserveAdd/{diningTableId}` | 创建预约 | 路径参数+Reserve | 操作消息 |
| GET | `/getOrdering/{no}` | 查询订单详情 | 路径参数 no | 订单列表 |
| POST | `/addOrdering` | 添加订单 | Ordering | 操作消息 |
| POST | `/changePassword` | 修改密码 | UserBodyChangePassword | 操作消息 |

## C03: FarmManagerController (`/api/farmManager`)

| 方法 | 路径 | 功能 | 请求体 | 响应 |
|------|------|------|--------|------|
| GET | `/getStaff` | 获取员工列表 | - | staffList+pageBody1+positionList |
| POST | `/addStaff` | 添加员工 | Staff | 操作消息 |
| POST | `/deleteStaff/{username}` | 解雇员工 | 路径参数 | 操作消息 |
| POST | `/takeOffice/{id}` | 任职/调任 | Staff+路径参数ID | 操作消息 |
| POST | `/changePassword` | 修改密码 | UserBodyChangePassword | 操作消息 |
| GET | `/initAttendance` | 初始化考勤 | - | attendanceList+pageBody1 |
| POST | `/chooseDate` | 选择日期查考勤 | LocalDateTime | attendanceList+pageBody1 |
| POST | `/updateAttendance` | 提交考勤 | List\<Attendance\> | 操作消息 |
| POST | `/downloadAttendanceExcel` | 导出考勤Excel | LocalDateTime | Excel 文件下载 |
| POST | `/downloadDaySaleExcel` | 导出日销售Excel | LocalDateTime | Excel 文件下载 |
| POST | `/downloadMonthSaleExcel` | 导出月销售Excel | LocalDateTime | Excel 文件下载 |
| POST | `/downloadDayPurchaseExcel` | 导出日采购Excel | LocalDateTime | Excel 文件下载 |
| POST | `/downloadMonthPurchaseExcel` | 导出月采购Excel | LocalDateTime | Excel 文件下载 |

## C04: FarmStaffController (`/api/farmStaff`)

| 方法 | 路径 | 功能 | 请求体 | 响应 |
|------|------|------|--------|------|
| POST | `/initProduct` | 初始化产品列表 | - | 产品分页列表 |
| POST | `/doPage/{productType}` | 产品分页 | 路径参数+PageBody | 产品分页列表 |
| POST | `/changePassword` | 修改密码 | UserBodyChangePassword | 操作消息 |
| POST | `/addProduct` | 添加产品 | Product | 操作消息 |
| POST | `/deleteProduct` | 删除产品 | Product | 操作消息 |
| POST | `/modifyProduct` | 修改产品 | Product | 操作消息 |
| POST | `/abnormalConsumption` | 异常消耗记录 | Consumption | 操作消息 |
| POST | `/produce` | 生产记录 | Produce | 操作消息 |
| POST | `/abnormalConsumption1` | 异常消耗(其他) | Consumption | 操作消息 |
| POST | `/addPurchase` | 添加采购 | Purchase | 操作消息 |
| POST | `/produce1` | 生产记录(其他) | Produce | 操作消息 |
| POST | `/initFertilizer` | 初始化肥料列表 | - | 肥料分页列表 |
| POST | `/doPage1/{fertilizerType}` | 肥料分页 | 路径参数+PageBody | 肥料分页列表 |
| POST | `/addPurchase1` | 添加肥料采购 | Purchase | 操作消息 |
| POST | `/abnormalConsumptionFertilizer` | 肥料异常消耗 | Consumption | 操作消息 |
| POST | `/farmUse` | 农场使用 | - | 操作消息 |
| POST | `/addFertilizer` | 添加肥料 | Fertilizer | 操作消息 |
| POST | `/deleteFertilizer` | 删除肥料 | Fertilizer | 操作消息 |
| POST | `/addSaleList` | 添加销售记录 | List\<Sale\> | 操作消息 |

## C05: RestaurantManagerController (`/api/restaurantManager`)

与 FarmManagerController 结构对称，端点完全一致（13 个），仅数据范围为餐厅侧。

## C06: RestaurantStaffController (`/api/restaurantStaff`)

| 方法 | 路径 | 功能 | 请求体 | 响应 |
|------|------|------|--------|------|
| GET | `/getReserve` | 获取预约列表 | - | 预约列表+分页 |
| POST | `/doPage` | 预约分页 | PageBody | 预约分页列表 |
| GET | `/deleteReserve/{no}` | 删除预约 | 路径参数 | 操作消息 |
| GET | `/getOrdering/{no}` | 查询订单 | 路径参数 | 订单列表 |
| GET | `/settleAccounts/{no}` | 结账 | 路径参数 | 结账结果 |
| POST | `/submitRepair` | 提交维修 | Repair | 操作消息 |
| POST | `/changePassword` | 修改密码 | UserBodyChangePassword | 操作消息 |
| GET | `/getRepair` | 获取维修列表 | - | 维修列表+分页 |
| POST | `/doPage1` | 维修分页 | PageBody | 维修分页列表 |
| POST | `/deleteRepair` | 删除维修 | Repair | 操作消息 |
| POST | `/completeRepair` | 完成维修 | Repair | 操作消息 |
| POST | `/addRestaurantMaterial` | 添加餐厅物料 | RestaurantMaterial | 操作消息 |
| GET | `/initRestaurantMaterial` | 初始化物料列表 | - | 物料分页列表 |
| POST | `/doPage2` | 物料分页 | PageBody | 物料分页列表 |
| POST | `/addRestaurantMaterialPurchase` | 物料采购 | Purchase | 操作消息 |
| POST | `/deleteRestaurantMaterial` | 删除物料 | RestaurantMaterial | 操作消息 |
| POST | `/consumptionRestaurantMaterial` | 物料消耗 | Consumption | 操作消息 |
| POST | `/useRestaurantMaterial` | 使用物料 | - | 操作消息 |

## C07: SuperManagerController (`/api/superManager`)

| 方法 | 路径 | 功能 | 请求体 | 响应 |
|------|------|------|--------|------|
| GET | `/getStaff` | 获取全部员工 | - | staffList+pageBody1+positionList |
| POST | `/addStaff` | 添加员工 | Staff | 操作消息 |
| POST | `/deleteStaff/{username}` | 解雇员工 | 路径参数 | 操作消息 |
| POST | `/takeOffice/{id}` | 任职/调任 | Staff+路径参数ID | 操作消息 |
| POST | `/changePassword` | 修改密码 | UserBodyChangePassword | 操作消息 |
| GET | `/initAttendance` | 初始化考勤 | - | attendanceList+pageBody1 |
| POST | `/chooseDate` | 选日期查考勤 | LocalDateTime | attendanceList+pageBody1 |
| POST | `/updateAttendance` | 提交考勤 | List\<Attendance\> | 操作消息 |
| POST | `/downloadAttendanceExcel` | 导出考勤Excel | LocalDateTime | Excel 文件下载 |

---

## 统一响应格式约定

### 成功操作响应
```json
{"message": "操作成功！"}
```

### 数据列表响应（以 getStaff 为例）
```json
{
  "staffList": [...],
  "staffList1": [...],
  "pageBody1": {"page": 1, "pages": 3, "pageList": [1,2,3]},
  "positionList": [...]
}
```

### 分页响应结构 (PageBody1)
```json
{
  "page": 1,
  "pages": 3,
  "pageList": [1, 2, 3]
}
```

### 错误响应
```json
{"message": "错误描述", "status": 401/404/500}
```
或 HTTP Status Exception (如 UNAUTHORIZED)

## 迁移不变性保证

以上 **80 个端点** 的以下属性在迁移前后必须完全一致：
- ✅ HTTP 方法 (GET/POST/DELETE/PUT)
- ✅ URL 路径（包括路径参数）
- ✅ 请求 Content-Type (application/json)
- ✅ 请求体 JSON 结构（字段名和类型）
- ✅ 响应 JSON 结构（字段名和类型）
- ✅ HTTP 状态码语义
