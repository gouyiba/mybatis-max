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
import java.util.Objects;
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
        this.tableInfo=getTableInfo();
    }

    public UpdateWrapper(){}

    /**
     * 修改 sql 生成:
     * 用于修改sql的参数和value和条件生成
     *
     * @author duxiaoyu
     * @since 2020-01-28
     */
    public Map<String, Object> sqlGenerate(Map<String,Object> mergeSqlMap) {
        Map<String, TableFieldInfo> fieldInfoMap = this.tableInfo.getColumnMap();

        sqlMap.put(SqlKey.TABLE_NAME.getValue(), this.tableInfo.getTableName());

        Field primaryKey = this.tableInfo.getPrimaryKey();
        TableFieldInfo columnPK = fieldInfoMap.get(primaryKey.getName());

        //fieldInfoMap.remove(this.tableInfo.getPrimaryKey().getName());
        Map<String, String> sqlValue = this.sqlValueConvert(fieldInfoMap,primaryKey.getName());
        sqlMap.put(SqlKey.UPDATE_VALUE.getValue(), sqlValue);
        //String where = String.format("%s %s=#{objectMap.%s,jdbcType=%s}", MySqlKeyWord.WHERE.getValue(), columnPK.getColumnName(), primaryKey.getName(), columnPK.getColumnType().getValue());
        // 替换sql条件目标参数
        if(!Objects.isNull(mergeSqlMap)){
            // 根据条件修改
            Map<String,String> where=(Map<String, String>) mergeSqlMap.get(MySqlKeyWord.WHERE.getValue());
            for (String item:where.keySet()){
                where.put(item,where.get(item).replace("valMap","sqlMap.UPDATE_WHERE.VALUE"));
            }
            sqlMap.put(SqlKey.UPDATE_WHERE.getValue(), mergeSqlMap);
        }else {
            // 根据指定主键修改
            String where = String.format("%s %s=#{objectMap.%s,jdbcType=%s}", MySqlKeyWord.WHERE.getValue(), columnPK.getColumnName(), primaryKey.getName(), columnPK.getJdbcType().getValue());
            sqlMap.put(SqlKey.UPDATE_WHERE.getValue(), where);
        }
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
                String columnType = item.getValue().getJdbcType().getValue();
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
}
