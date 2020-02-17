package com.rabbit.core.constructor;

import com.rabbit.core.bean.TableFieldInfo;
import com.rabbit.core.bean.TableInfo;
import com.rabbit.core.enumation.MySqlKeyWord;
import com.rabbit.core.enumation.SqlKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 删除条件构造器
 *
 * @param <E>
 * @author duxiaoyu
 * @since 2019-12-12
 */
public class DeleteWrapper<E> extends QueryWrapper<E> implements Serializable {

    private static final Logger LOGGER = LoggerFactory.getLogger(DeleteWrapper.class);

    // delete-sql-cache-map
    private final Map<String, String> sqlMap = new ConcurrentHashMap<>();

    /**
     * 解析后的TableInfo
     */
    private TableInfo tableInfo;

    /*
     *
     * DeleteWrapper继承自QueryWrapper，所以拥有QueryWrapper的条件拼接生成能力
     * TODO 构建思路待实现...
     *
     *
     *
     * */

    public DeleteWrapper(E clazz) {
        super(clazz);
        this.tableInfo=getTableInfo();
    }

    /**
     * 删除 sql 生成:
     * 用于删除sql的参数和value的生成
     *
     * @author duxiaoyu
     * @since 2020-02-06
     */
    public Map<String, String> sqlGenerate() {
        Map<String, TableFieldInfo> fieldInfoMap = this.tableInfo.getColumnMap();
        Field primaryKey = this.tableInfo.getPrimaryKey();
        TableFieldInfo columnPK = fieldInfoMap.get(primaryKey.getName());
        sqlMap.put(SqlKey.DELETE_HEAD.getValue(), MySqlKeyWord.DELETE.getValue() + " " + MySqlKeyWord.FROM.getValue());
        sqlMap.put(SqlKey.TABLE_NAME.getValue(), this.tableInfo.getTableName());
        String where = String.format("%s %s=#{%s,jdbcType=%s}", MySqlKeyWord.WHERE.getValue(), columnPK.getColumnName(), "objectId", columnPK.getColumnType().getValue());
        sqlMap.put(SqlKey.DELETE_WHERE.getValue(), where);
        return sqlMap;
    }
}
