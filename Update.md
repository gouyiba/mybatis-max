
- 2019.12.12 更新
    - 创建mrp基础模块common并提供对应的层级目录
    - 增加common模块下的util实现
    - 增加starter模块作为mrp执行测试基础模块
    - 新增mrp-core核心模块，构建基本思路体系，具体设计实现待完成 。。。

- 2019.12.14 更新
    - 增加core模块的相关层级目录结构
    - 增加数据库注解和实现
    - 增加数据库标识
    - 完成bean的扫描和bean.class解析
    - 将解析结果做全局缓存处理

- 2019.12.27 更新
    - 增加sql-key标识，增加MySql数据类型处理
    - 增加主键自动填充策略
        ```
        PrimaryKey generateType() default PrimaryKey.OBJECTID;
        默认填充为24位的OBJECTID
        目前提供的主键填充策略:
        1. OBJECTID
        2. UUID32
        3. SNOWFLAKE(雪花算法，需要额外设置工作机器ID和数据中心ID)
        ```
    - 增加公用Dao接口
    - 新增构造器的相关逻辑实现
    - BaseService的insert业务逻辑初步完成
    - 优化bean.class的解析步骤

- 2019.12.31 更新
    - 对common模块下的公用包做统一的命名
    - 优化BaseAbstractWrapper内部业务逻辑
    - 增加公用字段填充策略
        ```
        @FillingStrategy: 填充策略类标识
        1. 自定义公用Class，使用@FillingStrategy进行标注该类为公用字段填充类
        2. 通过@Component将该类注入Spring
        3. 定义公用私有字段并提供get/set-method
        3. 在类中自定义Method通过@Create/@Update分别进行标注并实现公用字段的默认填充
        ```
     - 完善新增业务逻辑
  
- 2020.01.03 更新
    - 完善主键填充策略
    - 完善新增构造器逻辑实现

- 2020.01.10 更新
    - 增加对数据的批量新增 
    - 测试mrp整个新增业务流程
    
- 2020.02.05 更新
    - 增加update构造器的相关逻辑实现
    - BaseService的update业务逻辑初步完成
    - 增加对数据的批量修改
    - 优化批量操作逻辑
    
- 2020.02.06 更新
    - 增加delete构造器的相关逻辑实现
    - BaseService的delete业务逻辑初步完成
    - 增加对数据的批量删除
    
- 2020.02.09 更新
    - 增加QueryWarpper构造器的业务逻辑实现
    - 优化BaseAbstractWrapper的解析处理
    - BaseService的query业务逻辑初步完成
    
- 2020.02.10 更新
    - 增加主键查询相关实现
    - 增加自定义查询相关实现
    - 优化QueryWarpper构造器内部逻辑
    
- 2020.02.12 更新
    - 完善bean中对枚举属性的转换
    - 提供IEnumTypeHandler枚举转换器
    - 优化insert/select对枚举属性的影响
   
- 2020.02.14 更新
    - 修复对bean.class解析时出现的数据类型不匹配
    - 优化update构造器的逻辑实现
    - 测试mrp相关业务
    
- 2020.02.17 更新
    - 增加逻辑删除的相关实现
        ```
        新增 @Delete注解用于实现逻辑删除功能:
        @Delete共有两个属性:physicsDel/value
        physicsDel: true/false，默认为true表示物理删除，相反false表示逻辑删除
        value: 逻辑删除默认值(目前只支持int类型)
        
        # 使用示例:
        @Delete(physicsDel = false,value = 1)
        private int delFlag;        
        ```
    - 变更BaseAbstractWrapper的相关业务逻辑实现
    - 优化公共字段自动填充策略
    - 优化QueryWrapper、UpdateWrapper、InsertWrapper内部业务逻辑
    
- 2020.02.24 更新
    - 修复查询数据为空返回空指针的异常
    - 修复bean中枚举属性转换异常
    - 增加对MySql的数据类型支持
    - 优化查询结果集的处理
    
- 2020.04.02 更新
    - 修复构造器中关于BaseBean合并异常
    
- 2020.05.06 更新
    - 依赖整理
    
- 2020.05.15 更新
    - 替换Mybatis底层执行实现
    
- 2020.05.20 更新
    - 注入BaseService自定义方法
    
- 2020.05.21 更新
    - 优化批量操作业务逻辑
