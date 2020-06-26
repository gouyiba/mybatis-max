package cloub.gouyiba.core.constructor;

import cloub.gouyiba.core.annotation.Column;
import cloub.gouyiba.core.enumation.MySqlKeyWord;
import cloub.gouyiba.core.enumation.SqlKey;
import cloub.gouyiba.core.bean.TableFieldInfo;
import cloub.gouyiba.core.bean.TableInfo;
import cloub.gouyiba.core.parse.ParseClass2TableInfo;
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
 * UPDATE-SQL构造器
 *
 * @param <E>
 * @author duxiaoyu
 * @since 2019-12-12
 */
public class UpdateWrapper<E> extends QueryWrapper<E> implements Serializable {

    private static final Logger LOGGER = LoggerFactory.getLogger(UpdateWrapper.class);

    // update-sql-cache-map
    private final Map<String, Object> sqlMap = new ConcurrentHashMap<>();

    private Map<String, Object> queryWrapper = new HashMap<>(16);

    public Map<String, Object> getQueryWrapper() {
        return queryWrapper;
    }

    public void setQueryWrapper(Map<String, Object> valMap) {
        this.queryWrapper.put("valMap", valMap);
    }

    private E entity;

    public E getEntity() {
        return entity;
    }

    public void setEntity(E entity) {
        this.entity = entity;
    }

    /**
     * 解析后的TableInfo
     */
    private TableInfo tableInfo;

    public UpdateWrapper(E clazz) {
        this.tableInfo = ParseClass2TableInfo.parseClazzToTableInfo(clazz.getClass());
    }

    public UpdateWrapper() {
    }

    /**
     * 修改 sql 生成:
     * 用于修改sql的参数和value和条件生成
     *
     * @author duxiaoyu
     * @since 2020-01-28
     */
    public Map<String, Object> sqlGenerate(Map<String, Object> mergeSqlMap) {
        Map<String, TableFieldInfo> fieldInfoMap = this.tableInfo.getColumnMap();

        sqlMap.put(SqlKey.TABLE_NAME.getValue(), this.tableInfo.getTableName());

        Field primaryKey = this.tableInfo.getPrimaryKey();
        TableFieldInfo columnPK = fieldInfoMap.get(primaryKey.getName());

        //fieldInfoMap.remove(this.tableInfo.getPrimaryKey().getName());
        Map<String, String> sqlValue = this.sqlValueConvert(fieldInfoMap, primaryKey.getName());
        sqlMap.put(SqlKey.UPDATE_VALUE.getValue(), sqlValue);
        //String where = String.format("%s %s=#{objectMap.%s,jdbcType=%s}", MySqlKeyWord.WHERE.getValue(), columnPK.getColumnName(), primaryKey.getName(), columnPK.getColumnType().getValue());
        // 替换sql条件目标参数
        if (!Objects.isNull(mergeSqlMap)) {
            // 根据条件修改
            Map<String, String> where = (Map<String, String>) mergeSqlMap.get(MySqlKeyWord.WHERE.getValue());
            for (String item : where.keySet()) {
                where.put(item, where.get(item).replace("queryWrapper.valMap", "sqlMap.UPDATE_WHERE.VALUE"));
            }
            sqlMap.put(SqlKey.UPDATE_WHERE.getValue(), mergeSqlMap);
        } else {
            // 根据指定主键修改
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
}
