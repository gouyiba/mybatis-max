
- 2020.2.17更新
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
    
    
- 2020.2.24更新
    - 修复查询数据为空返回空指针的异常
    - 修复bean中枚举属性转换异常
    - 增加对MySql的数据类型支持
    - 优化查询结果集的处理