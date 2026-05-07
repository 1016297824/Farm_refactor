## Context

农场管理系统前端使用 Vue.js，已建立设计系统（myCSS.css），包含完整的 CSS 变量令牌、组件样式（卡片、按钮、输入框、表格、分页等）。

**已完成美化：**
- 设计系统 myCSS.css
- App.vue 主容器
- 登录页面 Login.vue
- 导航栏 SuperManagerNavbar.vue（部分）

**待美化组件/页面（共约 49 个 Vue 文件）：**

| 类别 | 文件数 | 说明 |
|------|--------|------|
| Header/Navbar | 7 | LoginNavbar, CustomerNavbar, FarmStaffNavbar, FarmManagerNavbar, RestaurantStaffNavbar, RestaurantManagerNavbar |
| 公共组件 | 2 | AlertDialog, FooterNavbar |
| 登录相关 | 1 | Register.vue |
| 客户页面 | 7 | Customer, Order, OrderAdd, Password, Purchase, Reserve, ReserveAdd |
| 超管页面 | 6 | SuperManager, StaffShow, StaffAdd, Attendance, ReportForm, PasswordSupermanager |
| 农场管理页面 | 7 | FarmManager, StaffShow1, StaffAdd1, Attendance1, ReportForm1, PasswordFarmManager |
| 餐厅管理页面 | 7 | RestaurantManager, StaffShow2, StaffAdd2, Attendance2, ReportForm2, PasswordRestaurantManager |
| 农场员工页面 | 7 | FarmStaff, Fertilizer, ProductShow, RawMaterial, RestaurantMaterial, Sale, PasswordFarmStaff |
| 餐厅员工页面 | 6 | RestaurantStaff, Repair, RepairComplete, SettleAccounts, PasswordRestaurantStaff |

## Goals / Non-Goals

**Goals:**
- 统一所有页面的视觉风格，与现有设计系统一致
- 美化所有 Navbar 组件，使用与 SuperManagerNavbar 相同的样式模式
- 美化 AlertDialog 为 Toast 通知组件
- 美化 FooterNavbar 统一风格
- 美化所有业务页面视图

**Non-Goals:**
- 不修改后端 Java 代码
- 不修改 API 接口
- 不改变业务逻辑
- 不添加新功能

## Decisions

1. **使用现有设计系统**
   - 所有样式复用 myCSS.css 中定义的 CSS 变量和类
   - 确保新增组件与登录页、导航栏风格一致

2. **Navbar 美化策略**
   - 参照 SuperManagerNavbar 的渐变背景、圆角、阴影样式
   - 为不同角色Navbar 保持一致的布局结构

3. **AlertDialog 升级为 Toast**
   - 从原生的 alert() 改为精美的 Toast 通知
   - 支持成功/警告/错误等不同类型
   - 3秒后自动消失

4. **渐进式美化**
   - 每个页面独立美化，不影响其他页面
   - 使用 `farm-card`、`farm-btn`、`farm-input` 等已有组件类

## Risks / Trade-offs

| Risk | Mitigation |
|------|------------|
| 样式覆盖导致原有功能异常 | 每个文件单独测试后再进行下一个 |
| 部分页面已有内联样式冲突 | 使用 `scoped` + 特定类名隔离 |
| Toast 组件 Bus 事件可能冲突 | AlertDialog 已使用 bus.$on，扩展而非替换 |

## Open Questions

- 是否需要添加动画过渡效果？
- 是否有特定的页面需要特别关注或优先处理？
