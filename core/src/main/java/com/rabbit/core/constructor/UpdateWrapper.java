package com.rabbit.core.constructor;

import com.rabbit.core.annotation.Column;
import com.rabbit.core.bean.TableFieldInfo;
import com.rabbit.core.bean.TableInfo;
import com.rabbit.core.enumation.MySqlKeyWord;
import com.rabbit.core.enumation.SqlKey;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 修改条件构造器
 *
 * @param <E>
 * @author duxiaoyu
 * @since 2019-12-12
 */
public class UpdateWrapper<E> extends QueryWrapper<E> implements Serializable {

    private static final Logger LOGGER = LoggerFactory.getLogger(UpdateWrapper.class);

    // update-sql-cache-map
    private final Map<String, Object> sqlMap = new ConcurrentHashMap<>();

    /**
     * 解析后的TableInfo
     */
    private TableInfo tableInfo;

    public UpdateWrapper(E clazz) {
        super(clazz);
        this.tableInfo = analysisClazz();
    }

    /**
     * 修改 sql 生成:
     * 用于修改sql的参数和value和条件生成
     *
     * @author duxiaoyu
     * @since 2020-01-28
     */
    public Map<String, Object> sqlGenerate() {
        Map<String, TableFieldInfo> fieldInfoMap = this.tableInfo.getColumnMap();
        sqlMap.put(SqlKey.TABLE_NAME.getValue(), this.tableInfo.getTableName());

        Field primaryKey = this.tableInfo.getPrimaryKey();
        TableFieldInfo columnPK = fieldInfoMap.get(primaryKey.getName());

        //fieldInfoMap.remove(this.tableInfo.getPrimaryKey().getName());
        Map<String, String> sqlValue = this.sqlValueConvert(fieldInfoMap,primaryKey.getName());
        sqlMap.put(SqlKey.UPDATE_VALUE.getValue(), sqlValue);
        String where = String.format("%s %s=#{objectMap.%s,jdbcType=%s}", MySqlKeyWord.WHERE.getValue(), columnPK.getColumnName(), primaryKey.getName(), columnPK.getColumnType().getValue());
        sqlMap.put(SqlKey.UPDATE_WHERE.getValue(), where);
        return sqlMap;
    }

    /**
     * 修改时 sql-value-format 格式化:
     * 根据属性和数据库字段类型进行value类型格式转换
     * 需要考虑sql注入风险: 考虑使用 #{} 进行赋值操作，sql的value会生成: #{stuid,jdbcType=BIGINT} 格式的sql
     * 字段value进行转换时，如: 字段是枚举类型，如果指定了typeHandler，则使用指定的typeHandler进行转换，未指定，则使用默认的typeHandler进行转换
     * 将最终的完整sql交给mybatis进行预编译，避免sql的注入风险
     * 同时，在修改时考虑到非空值，所以会自动生成动态sql的非空验证
     *
     * @param fieldInfoMap 字段Map
     * @return value 转换后的字符串
     * @author duxiaoyu
     * @since 2020-01-28
     */
    private Map<String, String> sqlValueConvert(Map<String, TableFieldInfo> fieldInfoMap, String pkName) {
        Map<String, String> paramterMap = new HashMap<>();
        for (Map.Entry<String, TableFieldInfo> item : fieldInfoMap.entrySet()) {
            // 主键字段不参与修改
            if (!StringUtils.equals(pkName, item.getKey())) {
                Field field = item.getValue().getField();
                String typeHandler = "";
                if (field.isAnnotationPresent(Column.class)) {
                    Column column = field.getAnnotation(Column.class);
                    Class<?> typeHandlerClass = column.typeHandler();
                    if (!StringUtils.equals("Object", typeHandlerClass.getSimpleName())) {
                        typeHandler = typeHandlerClass.getName();
                    }
                }
                String propertyName = item.getValue().getPropertyName();
                String columnType = item.getValue().getColumnType().getValue();
                String columnName = item.getValue().getColumnName();
                if (StringUtils.isNotBlank(typeHandler)) {
                    paramterMap.put(propertyName, String.format("%s=#{objectMap.%s,typeHandler=%s},", columnName, propertyName, typeHandler));
                } else {
                    paramterMap.put(propertyName, String.format("%s=#{objectMap.%s,jdbcType=%s},", columnName, propertyName, columnType));
                }
            }
        }
        return paramterMap;
    }

    /*
     * UpdateWrapper继承自QueryWrapper，所以拥有QueryWrapper的条件拼接生成能力
     * 目前的思路想法:
     * 在UpdateWrapper中定义修改时，所需要的Method实现，以及Sql的生成拼接规则
     *
     * 使用方法(暂时拟定):
     * User user=new User();
     * user.setId(10010);
     * user.setName("admin");
     * user.setAge(20);
     *
     * 默认按照 @Id 注解标注的主键来修改指定数据
     * UpdateWrapper<User> updateWrapper=new UpdateWrapper<User>();
     * updateWrapper.setEntity(user); 设置要修改的对象
     * 修改时可以传入对象，如上User，生成的Sql是:
     * update set user name=#{name,jdbcType=VARCHAR},age=#{age,jdbcType=INT} where id=#{id,jdbcType=INT}
     *
     *
     * 也可以不传入对象，直接指定条件修改数据，实例化UpdateWrapper，指定泛型并在构造函数中传入指定的bean.class
     * UpdateWrapper<User> updateWrapper=new UpdateWrapper<User>(User.class);
     * Map<String,Object> map=new HashMap<>();
     * map.put("phone","166xxx95036");
     * updateWrapper.setMap(map); 设置要修改的字段value
     * 设置修改条件
     * updateWrapper.where("age",20);
     * updateWrapper.where("sex","男");
     * 如上生成的Sql是:
     * update set user name=#{phone,jdbcType=VARCHAR} where age=#{age,jdbcType=INT} and sex=#{sex,jdbcType=INT}
     *
     * TODO 构建思路待实现 ...
     *
     * 第一步先实现根据主键修改
     * 第二步根据Map条件实现多条件修改
     * 第三步根据QueryWrapper实现条件修改
     * 第四步实现批量修改的重载(根据主键和多条件)
     * */
}
