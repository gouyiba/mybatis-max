package com.rabbit.core.constructor;

import com.rabbit.core.annotation.Column;
import com.rabbit.core.annotation.Id;
import com.rabbit.core.bean.TableFieldInfo;
import com.rabbit.core.bean.TableInfo;
import com.rabbit.core.enumation.MySqlKeyWord;
import com.rabbit.core.enumation.SqlKey;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Field;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 默认条件构造器
 *
 * @author duxiaoyu
 */
public class DefaultAbstractWrapper extends BaseAbstractWrapper {

    private TableInfo tableInfo;

    public DefaultAbstractWrapper(TableInfo tableInfo) {
        this.tableInfo = tableInfo;
    }

    /**
     * 新增 sql 生成: INSERT INTO table VALUES (#{field})
     * 用于新增sql的参数和value的生成
     *
     * @return Map<String, String>
     */
    public Map<String, String> insertSqlGenerate() {
        Map<String, String> sqlMap = new HashMap<>();

        Map<String, TableFieldInfo> fieldInfoMap = this.tableInfo.getColumnMap();

        sqlMap.put(SqlKey.INSERT_HEAD.getValue(), MySqlKeyWord.INSERT.getValue());
        sqlMap.put(SqlKey.TABLE_NAME.getValue(), this.tableInfo.getTableName());
        sqlMap.put(SqlKey.INSERT_PAM_LEFT_BRA.getValue(), "(");

        // 过滤自增字段
        Collection<TableFieldInfo> fieldInfoCollection = fieldInfoMap.values();
        List<String> sqlField = this.filterIncrementColumnField(fieldInfoCollection);

        sqlMap.put(SqlKey.INSERT_PARAMETER.getValue(), String.join(",", sqlField));
        sqlMap.put(SqlKey.INSERT_PAM_RIGHT_BRA.getValue(), ")");

        // 验证value类型，并进行value对应的数据库类型转换
        sqlMap.put(SqlKey.INSERT_VALUE_KEYWORD.getValue(), MySqlKeyWord.VALUE.getValue());
        sqlMap.put(SqlKey.INSERT_VAL_LEFT_BRA.getValue(), "(");

        String sqlValue = this.sqlValueConvert(fieldInfoMap);
        sqlMap.put(SqlKey.INSERT_VALUE.getValue(), sqlValue);

        sqlMap.put(SqlKey.INSERT_VAL_RIGHT_BRA.getValue(), ");");
        return sqlMap;
    }

    /**
     * 修改sql生成 : update tableName set #{field}
     * 用于修改sql的参数和value的生成
     *
     * @return Map<String, Object>
     */
    public Map<String, Object> updateSqlGenerate() {
        Map<String, TableFieldInfo> fieldInfoMap = this.tableInfo.getColumnMap();
        Map<String, Object> sqlMap = new HashMap<>();
        sqlMap.put(SqlKey.TABLE_NAME.getValue(), this.tableInfo.getTableName());

        Field primaryKey = this.tableInfo.getPrimaryKey();
        TableFieldInfo columnPK = null;
        if (Objects.nonNull(primaryKey)) {
            columnPK = fieldInfoMap.get(primaryKey.getName());
        }
        //fieldInfoMap.remove(this.tableInfo.getPrimaryKey().getName());
        Map<String, String> sqlValue = this.sqlValueConvert(fieldInfoMap, primaryKey == null ? "" : primaryKey.getName());
        sqlMap.put(SqlKey.UPDATE_VALUE.getValue(), sqlValue);
        // 根据指定主键修改
        if (Objects.nonNull(columnPK)) {
            String where = String.format("%s %s=#{objectMap.%s,jdbcType=%s}", MySqlKeyWord.WHERE.getValue(), columnPK.getColumnName(), primaryKey.getName(), columnPK.getJdbcType().getValue());
            sqlMap.put(SqlKey.UPDATE_WHERE.getValue(), where);
        }
        return sqlMap;
    }

    /**
     * Java Field to SQL dynamic Field convert -> #{}, #{} ......
     * 将Java字段转换为SQL动态字段
     *
     * @param fieldInfoMap 字段Map
     * @return value 转换后的字符串
     */
    private String sqlValueConvert(Map<String, TableFieldInfo> fieldInfoMap) {
        StringJoiner values = new StringJoiner(",");
        for (Map.Entry<String, TableFieldInfo> item : fieldInfoMap.entrySet()) {
            StringJoiner value = new StringJoiner(",", "#{", "}");

            String propertyName = item.getValue().getPropertyName();
            value.add(String.format("%s", propertyName));

            String jdbcType = item.getValue().getJdbcType().getValue();
            if (StringUtils.isNotBlank(jdbcType)) {
                value.add(String.format("jdbcType=%s", jdbcType));
            }

            // Id注解无TypeHandler属性
            Field field = item.getValue().getField();
            if (field.isAnnotationPresent(Id.class)) {
                values.add(value.toString());
                continue;
            }

            Class<?> typeHandler = item.getValue().getTypeHandler();
            if (typeHandler != Object.class) {
                value.add(String.format("typeHandler=%s", typeHandler.getName()));
            }
            values.add(value.toString());
        }
        return values.toString();
    }

    /**
     * Java Field to SQL dynamic Field convert -> #{}, #{} ......
     * 将Java字段转换为SQL动态字段
     *
     * @param fieldInfoMap 字段Map
     * @param pkName       主键字段
     * @return Map<String, String>
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

    /**
     * 过滤自增字段
     *
     * @param fieldInfoCollection
     */
    public List<String> filterIncrementColumnField(Collection<TableFieldInfo> fieldInfoCollection) {
        return fieldInfoCollection.stream()
                .filter(
                        fieldInfo -> !(fieldInfo.getField().isAnnotationPresent(Id.class) && fieldInfo.getField().getAnnotation(Id.class).isIncrementColumn())
                )
                .map(x -> x.getColumnName()).collect(Collectors.toList());
    }
}
