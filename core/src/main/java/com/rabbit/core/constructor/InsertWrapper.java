package com.rabbit.core.constructor;

import com.rabbit.common.utils.CollectionUtils;
import com.rabbit.core.annotation.Id;
import com.rabbit.core.bean.TableFieldInfo;
import com.rabbit.core.bean.TableInfo;
import com.rabbit.core.annotation.Column;
import com.rabbit.core.enumation.MySqlKeyWord;
import com.rabbit.core.enumation.SqlKey;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * 新增 constructor
 *
 * @param <E>
 * @author duxiaoyu
 * @since 2019-12-12
 */
public class InsertWrapper<E> extends BaseAbstractWrapper<E> implements Serializable {

    private static final Logger LOGGER = LoggerFactory.getLogger(InsertWrapper.class);

    // insert-sql-cache-map
    private final Map<String, String> sqlMap = new ConcurrentHashMap<>();

    /**
     * 解析后的TableInfo
     */
    private TableInfo tableInfo;

    private TableInfo baseBean;

    public InsertWrapper(E clazz) {
        this.baseBean = analysisBaseBean();
        this.tableInfo = analysisClazz(clazz.getClass());
    }

    /**
     * 新增 sql 生成:
     * 用于新增sql的参数和value的生成
     *
     * @author duxiaoyu
     * @since 2019-12-25
     */
    public Map<String, String> sqlGenerate() {
        Map<String, TableFieldInfo> fieldInfoMap = this.tableInfo.getColumnMap();
        Map<String,TableFieldInfo> baseBeanFieldMap=null;
        if (this.baseBean!=null){
            baseBeanFieldMap=Optional.ofNullable(this.baseBean.getColumnMap()).orElse(null);
        }


        if(CollectionUtils.isNotEmpty(baseBeanFieldMap)){
            fieldInfoMap.putAll(baseBeanFieldMap);
        }

        sqlMap.put(SqlKey.INSERT_HEAD.getValue(), MySqlKeyWord.INSERT.getValue());
        sqlMap.put(SqlKey.TABLE_NAME.getValue(), this.tableInfo.getTableName());
        sqlMap.put(SqlKey.INSERT_PAM_LEFT_BRA.getValue(), "(");

        // 过滤自增字段
        Collection<TableFieldInfo> fieldInfoCollection = fieldInfoMap.values();
        this.filterIncrementColumnField(fieldInfoCollection);
        List<String> sqlField = fieldInfoCollection.stream().map(x -> x.getColumnName()).collect(Collectors.toList());

        sqlMap.put(SqlKey.INSERT_PARAMETER.getValue(), String.join(",", sqlField));
        sqlMap.put(SqlKey.INSERT_PAM_RIGHT_BRA.getValue(), ")");

        // 验证value类型，并进行value对应的数据库类型转换
        sqlMap.put(SqlKey.INSERT_VALUE_KEYWORD.getValue(), MySqlKeyWord.VALUE.getValue());
        String sqlValue = this.sqlValueConvert(fieldInfoMap);
        sqlMap.put(SqlKey.INSERT_VAL_LEFT_BRA.getValue(), "(");
        sqlMap.put(SqlKey.INSERT_VALUE.getValue(), sqlValue);
        sqlMap.put(SqlKey.INSERT_VAL_RIGHT_BRA.getValue(), ");");
        return sqlMap;
    }

    /**
     * 新增时 sql-value-format 格式化:
     * 根据属性和数据库字段类型进行value类型格式转换
     * 需要考虑sql注入风险: 考虑使用 #{} 进行赋值操作，sql的value会生成: #{stuid,jdbcType=BIGINT} 格式的sql
     * 字段value进行转换时，如: 字段是枚举类型，如果指定了typeHandler，则使用指定的typeHandler进行转换，未指定，则使用默认的typeHandler进行转换
     * 将最终的完整sql交给mybatis进行预编译，避免sql的注入风险
     *
     * @param fieldInfoMap 字段Map
     * @return value 转换后的字符串
     * @author duxiaoyu
     * @since 2019-12-25
     */
    private String sqlValueConvert(Map<String, TableFieldInfo> fieldInfoMap) {
        StringJoiner sj = new StringJoiner(",");
        for (Map.Entry<String, TableFieldInfo> item : fieldInfoMap.entrySet()) {
            Field field = item.getValue().getField();
            String typeHandler = "";
            if (field.isAnnotationPresent(Column.class)) {
                Column column = field.getAnnotation(Column.class);
                Class<?> typeHandlerClass = column.typeHandler();
                if(!StringUtils.equals("Object",typeHandlerClass.getSimpleName())){
                    typeHandler = typeHandlerClass.getName();
                }
            }
            String propertyName = item.getValue().getPropertyName();
            String columnType = item.getValue().getColumnType().getValue();
            if (StringUtils.isNotBlank(typeHandler)) {
                sj.add(String.format("#{objectMap.%s,typeHandler=%s}", propertyName, typeHandler));
            } else {
                sj.add(String.format("#{objectMap.%s,jdbcType=%s}", propertyName, columnType));
            }
        }
        return sj.toString();
    }

    /**
     * 过滤自增字段
     *
     * @param fieldInfoCollection
     */
    public void filterIncrementColumnField(Collection<TableFieldInfo> fieldInfoCollection) {
        Iterator<TableFieldInfo> fieldInfoIterator = fieldInfoCollection.iterator();
        while (fieldInfoIterator.hasNext()) {
            TableFieldInfo tableFieldInfo = fieldInfoIterator.next();
            if (tableFieldInfo.getField().isAnnotationPresent(Id.class)) {
                Id id = tableFieldInfo.getField().getAnnotation(Id.class);
                if (id.isIncrementColumn()) {
                    fieldInfoIterator.remove();
                }
            }
        }
    }
}
