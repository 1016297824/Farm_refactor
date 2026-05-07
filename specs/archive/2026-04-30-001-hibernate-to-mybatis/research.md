# Research: Hibernate → MyBatis Migration

**Feature**: Hibernate 迁移至 MyBatis
**Date**: 2026-04-30
**Status**: Complete

## R-001: MyBatis 版本选择与 Spring Boot 2.2.5 兼容性

**Decision**: 使用 `mybatis-spring-boot-starter` 2.1.3 + `mybatis` 3.5.4

**Rationale**:
- mybatis-spring-boot-starter 2.1.x 是与 Spring Boot 2.2.x 官方兼容的最新稳定版本线
- mybatis 3.5.4 是该 starter 默认依赖的 MyBatis 核心版本，经过充分验证
- 该组合支持 `@Mapper` 注解扫描、XML mapper 配置、typeAliases 等所需全部功能

**Alternatives Considered**:
| 方案 | 版本 | 结论 |
|------|------|------|
| mybatis-spring-boot-starter 2.2.x | 需要 Spring Boot 2.3+ | ❌ 不兼容 Spring Boot 2.2.5 |
| mybatis-plus 3.x | 封装更多但引入额外抽象层 | ❌ 违反最小变更原则 |
| 手动配置 mybatis-spring | 更灵活但配置繁琐 | ❌ 增加不必要的复杂度 |

**Maven 依赖变更**:
```xml
<!-- 移除 -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-data-jpa</artifactId>
</dependency>
<dependency>
    <groupId>com.fasterxml.jackson.datatype</groupId>
    <artifactId>jackson-datatype-hibernate5</artifactId>
</dependency>

<!-- 添加 -->
<dependency>
    <groupId>org.mybatis.spring.boot</groupId>
    <artifactId>mybatis-spring-boot-starter</artifactId>
    <version>2.1.3</version>
</dependency>
```

---

## R-002: JPA Entity 注解替换策略

**Decision**: Entity 类去除所有 `javax.persistence.*` 注解，保留 Lombok 和 Jackson 注解，转为纯 POJO

**Rationale**:
- MyBatis 不需要 JPA 注解来做 ORM 映射，映射逻辑在 XML 中定义
- 保留 `@Getter/@Setter/@NoArgsConstructor` (Lombok) 用于 POJO 属性访问
- 保留 `@JsonProperty` (Jackson) 用于 JSON 序列化控制（如 password WRITE_ONLY）
- 保留 `@Email` (validation) 如前端有使用 validation

**具体变更模式**:

| 原 JPA 注解 | 替换方案 | 说明 |
|-------------|----------|------|
| `@Entity` | 删除 | MyBatis 用 XML 映射 |
| `@Table(name="xxx")` | 删除 | 表名在 XML namespace/mapper 中指定 |
| `@Column` | 删除 | 字段名在 XML resultType 中自动匹配 |
| `@Id @GeneratedValue(IDENTITY)` | 删除 | 自增主键在 XML useGeneratedKeys 中处理 |
| `@ManyToOne` | 删除 | 改为 POJO 中的普通对象引用字段 |
| `@OneToMany(mappedBy="xxx")` | 删除 | 改为 List\<T\> 字段，按需查询填充 |
| `@JoinColumn` | 删除 | 外键关系在 XML resultMap 中用 association/collection 定义 |

**Entity 变更示例 (Staff.java)**:
```java
// Before (Hibernate/JPA)
@Entity
@Getter @Setter @NoArgsConstructor
public class Staff {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(unique = true)
    private String username;
    @ManyToOne
    private Position position;
    @OneToMany(mappedBy = "staff", cascade = CascadeType.REMOVE)
    private List<Attendance> attendanceList;
}

// After (MyBatis POJO)
@Getter @Setter @NoArgsConstructor
public class Staff {
    private int id;
    private String username;
    private String password;
    private String name;
    private String telNumber;
    private String email;
    private String address;
    private String idCardNo;
    private Position position;           // 普通 POJO 引用，由 resultMap 填充
    private List<Attendance> attendanceList; // 按需查询填充
}
```

---

## R-003: Repository → Mapper 接口转换策略

**Decision**: 每个 JpaRepository 接口转换为 `@Mapper` 注解接口，方法签名保持一致

**Rationale**:
- MyBatis 的 Mapper 接口与原 Repository 接口方法一一对应，Service 层调用几乎不用改
- 使用 `@Mapper` 注解让 Spring 自动扫描注册
- 自定义 `@Query` HQL 方法改为 XML 中对应的 `<select>` 语句
- `save()`/`delete()`/`findAll()`/`findById()` 等 JPA 内置方法对应标准 SQL

**JPA 内置方法 → MyBatis 映射对照表**:

| JPA 方法 | MyBatis XML 实现 | 说明 |
|----------|------------------|------|
| `save(entity)` | `<insert useGeneratedKeys="true">` | 插入并返回自增 ID |
| `saveAll(list)` | `<insert>` + foreach 批量插入 | 循环调用单条 insert |
| `delete(entity)` | `<delete id="deleteByPrimaryKey">` | 按 ID 删除 |
| `deleteById(id)` | 同上 | 同上 |
| `findAll()` | `<select id="selectAll"> SELECT * FROM table</select>` | 全表查询 |
| `findById(id)` | `<select id="selectByPrimaryKey">` | 主键查询 |
| `count()` / `getCount()` | `<select id="count"> SELECT COUNT(*) </select>` | 计数 |
| `refresh(entity)` | **移除** | MyBatis 无状态，无需 refresh；重新查询即可 |

**Repository → Mapper 接口转换示例 (Staff)**:
```java
// Before: StaffRepository.java
@Repository
public interface StaffRepository extends CustomizedRepository<Staff, Integer> {
    @Query("from Staff s") List<Staff> findAll();
    @Query("select s from Staff s where s.username=:username")
    Staff findByUsername(@Param("username") String username);
    void save(Staff staff);          // inherited from JpaRepository
    void delete(Staff staff);        // inherited from JpaRepository
}

// After: StaffMapper.java
@Mapper
public interface StaffMapper {
    List<Staff> findAll();
    Staff findByUsername(@Param("username") String username);
    List<Staff> findByPositionAuthorityIsNull();
    List<Staff> findByPositionAuthorityManagerStaff();
    int getCount();
    int getMaxNo();
    List<Staff> findAllFarmStaff();
    List<Staff> findAllRestaurantStaff();
    List<Staff> findAllWorked();
    List<Staff> findAllManager();
    int insert(Staff staff);
    int updateByPrimaryKey(Staff staff);
    int deleteByPrimaryKey(@Param("id") int id);
}
```

---

## R-004: 关联关系（Association/Collection）处理策略

**Decision**: 使用 MyBatis `<association>` 和 `<collection>` 在 resultMap 中处理一对一和多对一关系，一对多采用按需单独查询

**Rationale**:
- 项目中的关联关系主要是 **多对一**（Staff→Position、Sale→Product、Sale→SaleNo）和少量 **一对多**（Staff→Attendance、Customer→Reserve）
- 多对一用 `<association>` 嵌套查询或嵌套结果实现，保证查询时关联对象自动填充
- 一对多用 `<collection>` 或在 Service 层按需单独查询（避免 N+1 问题）
- 级联删除 (`CascadeType.REMOVE`) 改为在 delete 操作前显式删除子记录或在 SQL 中用 FOREIGN KEY CASCADE

**关联关系清单及处理方式**:

| 关系 | 类型 | MyBatis 处理方式 |
|------|------|------------------|
| Staff.position | ManyToOne | `<association javaType="Position">` 嵌套 select |
| Staff.attendanceList | OneToMany | Service 层按需查询 AttendanceMapper.findByStaffId |
| Customer.reserveList | OneToMany | Service 层按需查询 ReserveMapper.findByCustomerId |
| Customer.completeList | OneToMany | Service 层按需查询 |
| Sale.saleNo | ManyToOne | `<association javaType="SaleNo">` |
| Sale.product | ManyToOne | `<association javaType="Product">` |

**resultMap 示例 (Staff 带 Position)**:
```xml
<resultMap id="BaseResultMap" type="com.farm.entity.Staff">
    <id column="id" property="id"/>
    <result column="username" property="username"/>
    <result column="password" property="password"/>
    <result column="name" property="name"/>
    <result column="tel_number" property="telNumber"/>
    <result column="email" property="email"/>
    <result column="address" property="address"/>
    <result column="id_card_no" property="idCardNo"/>
    <association column="position_id" property="position"
                 javaType="com.farm.entity.Position"
                 select="com.farm.repository.PositionMapper.selectByPrimaryKey"/>
</resultMap>
```

---

## R-005: 事务管理兼容性

**Decision**: Spring 的 `@Transactional` 注解在 MyBatis 下继续正常工作，无需额外配置

**Rationale**:
- MyBatis-Spring 整合后，使用的是 Spring 的 `DataSourceTransactionManager`
- 与 JPA 使用的 `JpaTransactionManager` 不同底层，但对应用层 `@Transactional` 语义完全一致
- `@Transactional` 注解在 Service 层的方法上保持不变
- 唯一变化：`application.properties` 中不再需要 JPA 事务相关配置

**注意事项**:
- Service 层的 `@Transactional` 标注不需要任何修改
- 多步操作（如先删子表再删主表）在同一事务方法中仍能保证原子性
- `spring-boot-starter-jdbc` 依赖保留（MyBatis 底层需要 JDBC）

---

## R-006: 配置文件迁移

**Decision**: `application.properties` 中移除 JPA/Hibernate 配置段，新增 MyBatis 配置段

**具体变更**:

```properties
# ===== 移除 =====
##########jpa
spring.jpa.show-sql=true
spring.jpa.hibernate.ddl-auto=update
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQL8Dialect

# ===== 新增 ##########mybatis
mybatis.mapper-locations=classpath:mapper/*.xml
mybatis.type-aliases-package=com.farm.entity
mybatis.configuration.map-underscore-to-camel-case=true
mybatis.configuration.log-impl=org.apache.ibatis.logging.stdout.StdOutImpl

# ===== 保留不变 =====
##########datasource (数据源配置不变)
##########log (日志配置不变)
##########jackson (JSON 序列化不变)
##########crypto (加密配置不变)
```

**关键配置解释**:
- `mapper-locations`: 指定 Mapper XML 文件位置
- `type-aliases-package`: 自动将包下类注册为类型别名，XML 中可直接用 `Staff` 而非全限定名
- `map-underscore-to-camel-case`: 数据库下划线命名（如 tel_number）自动映射到驼峰（telNumber）
- `log-impl`: SQL 日志输出（替代原 JPA 的 show-sql）

---

## R-007: CustomizedRepository 与 EntityManager 处理

**Decision**: 删除 `CustomizedRepository`、`CustomizedRepositoryImpl` 及其 `refresh()` 方法

**Rationale**:
- `CustomizedRepository` 继承 `JpaRepository` 并扩展了 `refresh()` 方法
- `refresh()` 底层调用 `EntityManager.refresh()` 强制从数据库刷新实体状态
- MyBatis 是无状态的，每次查询都直接从数据库获取最新数据，不存在脏缓存问题
- 因此 `refresh()` 功能天然不需要，该接口和实现类可以整体移除

**影响范围**:
- 删除文件: `CustomizedRepository.java`, `CustomizedRepositoryImpl.java`
- 所有 Mapper 接口不再继承任何基类接口（纯 `@Mapper` 接口）
- 如果有代码调用了 `repository.refresh(entity)`，改为 `mapper.selectByPrimaryKey(entity.getId())`

---

## R-008: InitService 与 TimerService 适配

**Decision**: 两个特殊 Service 仅需将注入的 Repository 改为对应的 Mapper

**Rationale**:
- `InitService`: 调用 `staffRepository.saveAll(staffList)` → 改为循环调用 `staffMapper.insert(staff)` 或使用 MyBatis batch
- `TimerService`: 定时任务中使用的 Repository 查询方法只需改注入类型和方法来源
- 业务逻辑完全不涉及 JPA 特性，纯 CRUD 操作

---

## R-009: 分页查询处理

**Decision**: 当前项目分页逻辑在 Controller/Service 层手动实现（subList 截取），不依赖 JPA Pageable，因此无需引入 MyBatis-PageHelper

**Rationale**:
- 分析发现项目的分页是"内存分页"模式：先查全量 list，再 subList(pageSize*page, pageSize*(page+1))
- 这种模式在 MyBatis 下完全适用，因为返回的就是 `List<T>`
- 无需引入额外的分页插件，保持最小变更
