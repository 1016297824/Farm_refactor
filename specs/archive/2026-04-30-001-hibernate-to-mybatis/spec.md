# Feature Specification: Hibernate 迁移至 MyBatis

**Feature Branch**: `001-hibernate-to-mybatis`
**Created**: 2026-04-30
**Status**: Draft
**Input**: User description: "不再使用hibernate框架，而是使用mybatis框架代替；要确保重构后的代码可以和以前一样运行；保证与前端的交互和以前一样；遵循最小变更原则"

## User Scenarios & Testing *(mandatory)*

### User Story 1 - 系统启动与数据库连接正常 (Priority: P1)

作为系统管理员，我希望替换持久化框架后，应用能正常启动并成功连接 MySQL 数据库，所有数据库表结构保持不变。

**Why this priority**: 这是迁移的基础前提——如果应用无法启动或无法连接数据库，后续所有功能都无法验证。

**Independent Test**: 启动 Spring Boot 应用，检查日志中无异常，确认数据库连接成功。

**Acceptance Scenarios**:

1. **Given** 已完成框架替换并配置 MyBatis 数据源, **When** 启动 Spring Boot 应用, **Then** 应用成功启动且日志显示数据库连接正常，无 Hibernate 相关错误
2. **Given** 应用已启动, **When** 检查数据库中的表结构, **Then** 所有 21 张数据表（staff、customer、position、attendance 等）结构完整、字段不变

---

### User Story 2 - 前端 API 接口完全兼容 (Priority: P1)

作为前端用户（顾客/员工/管理员），我希望在框架替换后，所有现有的 API 接口路径、请求参数格式、响应 JSON 结构与之前完全一致。

**Why this priority**: 前端代码不做任何修改，这是用户明确的核心需求——保证前后端交互不变。

**Independent Test**: 对比迁移前后的 API 响应，验证每个接口的请求/响应完全一致。

**Acceptance Scenarios**:

1. **Given** 用户通过前端登录页面操作, **When** 发送登录请求到 `/api/login`, **Then** 返回的用户信息 JSON 结构与迁移前一致（包含 id、username、name、authority 等字段）
2. **Given** 超级管理员访问员工管理页面, **When** 调用 `GET /api/superManager/getStaff`, **Then** 返回的 staffList、pageBody1、positionList 结构与迁移前完全一致
3. **Given** 用户进行增删改查操作（添加员工/删除员工/修改密码等）, **When** 发送对应 POST 请求, **Then** 操作成功且返回消息格式为 `{"message": "xxx"}` 与之前相同
4. **Given** 所有 7 个 Controller 的全部 API 端点, **When** 逐一调用测试, **Then** 每个端点的 HTTP 方法、路径、参数、响应均与迁移前一致

---

### User Story 3 - CRUD 数据操作正确性 (Priority: P1)

作为系统用户，我希望所有数据的增删改查操作在框架替换后结果正确，包括关联查询（如员工-职位、销售-产品等）。

**Why this priority**: 数据操作是系统的核心功能，任何数据错误都是不可接受的。

**Independent Test**: 对每个实体执行完整的 CRUD 操作，验证数据库中的数据正确性。

**Acceptance Scenarios**:

1. **Given** 数据库中有 N 条员工记录, **When** 调用查询所有员工接口, **Then** 返回 N 条完整记录，每条记录的字段值（username、name、telNumber 等）正确
2. **Given** 提交新员工数据, **When** 执行保存操作, **Then** 数据库中新记录被正确插入，自增 ID 生效
3. **Given** 选择一条已有员工记录并修改其职位, **When** 执行更新操作, **Then** 数据库中该员工的 position_id 字段被正确更新
4. **Given** 选择一条员工记录, **When** 执行删除操作, **Then** 该记录从数据库中被移除，关联的考勤记录级联删除
5. **Given** 查询涉及多表关联的操作（如按职位筛选员工、按时间范围查询考勤）, **When** 执行关联查询, **Then** 返回的关联数据（如 staff.position.name）正确加载

---

### User Story 4 - 自定义查询方法等价替换 (Priority: P2)

作为系统用户，希望所有 Repository 中定义的自定义 HQL/JPQL 查询在替换后返回相同的结果集。

**Why this priority**: 项目有大量自定义 `@Query` 注解的 HQL 查询（约 60+ 个），这些是业务逻辑的核心查询。

**Independent Test**: 逐一对比每个自定义查询方法的输入输出。

**Acceptance Scenarios**:

1. **Given** StaffRepository 中定义了 `findByUsername` 查询, **When** 传入用户名参数, **Then** 返回对应的 Staff 对象（含关联 Position 信息），与迁移前结果一致
2. **Given** 各 Repository 中定义了聚合查询（getCount、getMaxNo）, **When** 调用这些方法, **Then** 返回正确的计数值和最大编号
3. **Given** Repository 中定义了条件筛选查询（如 findByPositionAuthorityIsNull、findAllFarmStaff 等）, **When** 调用这些方法, **Then** 返回的过滤结果集与迁移前一致

---

### User Story 5 - Excel 导出与定时任务正常运行 (Priority: P2)

作为管理员，希望考勤/销售报表的 Excel 导出功能和定时任务在框架替换后继续正常工作。

**Why this priority**: 报表导出是管理员的日常重要功能，TimerService 的定时任务影响系统自动化运行。

**Independent Test**: 触发 Excel 导出接口，验证生成的文件内容正确。

**Acceptance Scenarios**:

1. **Given** 管理员选择某月考勤数据并点击导出, **When** 调用下载接口, **Then** 成功生成 Excel 文件，内容（员工工时、工资计算等）与迁移前一致
2. **Given** TimerService 定时任务触发, **When** 执行自动化的数据处理逻辑, **Then** 任务正常完成，无持久化相关异常

---

### Edge Cases

- 当数据库中存在大量数据（如超过 1000 条员工记录）时，分页查询是否仍然正常工作？
- 当执行关联查询时，若关联对象为 null（如员工未分配职位），是否正确处理而不抛出异常？
- 当并发请求同时写入同一张表时，事务是否仍然正确隔离？
- 当字段值为空字符串或 null 时，MyBatis 的处理是否与 Hibernate 行为一致？
- `@Transactional` 标注的服务方法在 MyBatis 下是否仍能正确管理事务边界？

## Requirements *(mandatory)*

### Functional Requirements

- **FR-001**: 系统 MUST 将持久层框架从 Hibernate (spring-boot-starter-data-jpa) 替换为 MyBatis (mybatis-spring-boot-starter)，同时移除 JPA/Hibernate 相关依赖
- **FR-002**: 系统 MUST 保持所有 21 个 Entity 类的数据模型不变，将 JPA 注解（@Entity、@Table、@Column、@Id、@GeneratedValue、@ManyToOne、@OneToMany 等）替换为等价的 POJO + MyBatis 映射方式
- **FR-003**: 系统 MUST 将所有 21 个 Repository 接口从继承 JpaRepository 改为 MyBatis Mapper 接口，约 60+ 个自定义 @Query HQL 查询转换为等价的 MyBatis XML SQL 或注解
- **FR-004**: 系统 MUST 保持所有 21 个 Service 类的业务逻辑不变，仅调整对 Repository 层的调用方式以适配新接口
- **FR-005**: 系统 MUST 保持所有 7 个 Controller 类的 API 端点定义（路径、方法、参数、响应格式）完全不变
- **FR-006**: 系统 MUST 保持 application.properties 中除 JPA/Hibernate 配置外的其他配置项不变，新增 MyBatis 相关配置（mapper-location、type-aliases-package 等）
- **FR-007**: 系统 MUST 保持 pom.xml 中非 JPA 相关依赖不变，移除 spring-boot-starter-data-jpa 和 jackson-datatype-hibernate5，添加 mybatis-spring-boot-starter
- **FR-008**: 系统 MUST 正确处理 Entity 间的关联关系（Staff↔Position、Customer↔Reserve、Sale↔Product 等），确保关联查询结果与原 Hibernate 懒加载/急加载行为一致
- **FR-009**: 系统 MUST 保持 CustomizedRepository.refresh() 功能的等价实现（如有使用）
- **FR-010**: 系统 MUST 保持 @Transactional 事务管理的正确性，确保写操作的原子性和一致性
- **FR-011**: 系统 MUST 保持 InitService 初始化数据和 TimerService 定时任务的正常运行
- **FR-012**: 系统 MUST 保持 Excel 导出组件（ExcelComponent）和加密组件（EncryptorComponent）的功能不受影响

### Key Entities

- **Staff（员工）**: 核心实体，包含 id、username、password、name、telNumber、email、address、idCardNo 字段，多对一关联 Position，一对多关联 Attendance。涉及最多自定义查询方法（10 个）
- **Position（职位）**: 定义员工角色（SuperManager、FarmManager、FarmStaff、RestaurantManager、RestaurantStaff、Customer），含 basicSalary 和 authority 字段
- **Customer（顾客）**: 包含 id、username、password、name 字段，一对多关联 Reserve 和 OrderingComplete
- **Sale（销售）**: 关联 SaleNo 和 Product，用于农场销售记录，含 amount 和 price 字段
- **Attendance（考勤）**: 记录员工每日工时，关联 Staff，支持按时间范围查询和 Excel 导出
- **Ordering / OrderingComplete（订单）**: 顾客点餐订单及已完成订单
- **Purchase / Reserve（采购/预约）**: 农场采购记录和顾客预约记录
- **Product / Produce / Fertilizer（农产品/生产/肥料）**: 农场生产相关实体
- **DiningTable / Menu / RestaurantMaterial / Repair / Consumption（餐厅相关）**: 餐厅运营相关实体
- **SaleNo（销售编号）**: 销售单号管理实体

## Success Criteria *(mandatory)*

### Measurable Outcomes

- **SC-001**: 应用启动后零框架相关错误，数据库连接成功率 100%
- **SC-002**: 全部 7 个 Controller 的所有 API 端点（约 50+ 个）请求/响应格式与迁移前完全一致，通过逐接口对比验证
- **SC-003**: 全部 21 个实体的 CRUD 操作数据正确率 100%，包括 60+ 个自定义查询方法结果一致性验证
- **SC-004**: 前端所有页面功能（登录、员工管理、考勤、销售、预约、报表导出等）无需任何修改即可正常使用
- **SC-005**: Excel 报表导出功能输出内容与迁移前完全一致
- **SC-006**: 变更范围严格限定在持久层（Entity、Repository、Mapper XML、配置文件），Controller 和 Service 层变更量最小化

## Assumptions

- 数据库 schema 已由原 Hibernate ddl-auto=update 创建完毕，迁移后不再需要自动建表，MyBatis 连接的是同一套已有表结构
- 前端 Vue.js 代码不参与本次重构，仅需保证后端 API 契约不变
- MySQL 数据库版本和连接配置保持不变（localhost:3306/farm）
- Java 版本保持 11 不变，Spring Boot 版本保持 2.2.5.RELEASE 不变
- Lombok 注解（@Getter、@Setter、@NoArgsConstructor）在 Entity 中继续使用
- Jackson JSON 序列化配置（non_null、WRITE_ONLY password）保持不变
- Spring Security Crypto 密码编码功能不受持久层替换影响
- 最小变更原则：优先选择 Mapper XML 方式而非大幅重写 Service/Controller 逻辑
