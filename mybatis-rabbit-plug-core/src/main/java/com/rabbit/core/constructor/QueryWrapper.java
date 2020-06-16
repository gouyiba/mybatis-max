package com.rabbit.core.constructor;

import cn.hutool.json.JSONUtil;
import com.rabbit.common.exception.MyBatisRabbitPlugException;
import com.rabbit.core.bean.TableInfo;
import com.rabbit.core.enumation.MySqlColumnType;
import com.rabbit.core.enumation.MySqlKeyWord;
import com.rabbit.core.enumation.SqlKey;
import com.rabbit.core.parse.ParseClass2TableInfo;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * QUERY-SQL构造器
 *
 * @param <E>
 * @author duxiaoyu
 * @since 2019-12-12
 */
@Data
@Slf4j
public class QueryWrapper<E> extends BaseAbstractWrapper<E> implements Serializable {

    // query-sql-cache-map

    private Map<String, String> whereSqlMap = new ConcurrentHashMap<>();

    private Map<String, String> joinSqlMap = new ConcurrentHashMap<>();

    private Map<String, Object> valMap = new ConcurrentHashMap<>();

    private Map<String, Object> sqlMap = new ConcurrentHashMap<>();

    private TableInfo tableInfo;

    public QueryWrapper(E clazz) {
        this.tableInfo = ParseClass2TableInfo.parseClazzToTableInfo(clazz.getClass());
        this.sqlMap.put(SqlKey.TABLE_NAME.getValue(), this.tableInfo.getTableName());
    }

    public QueryWrapper() {
    }


    private int joinNum = 0;

    // final-variable
    private final String VALIDEN = "queryWrapper.valMap.";

    // global-final-static-variable
    public static final String ASC = "ASC";

    public static final String DESC = "DESC";

    /**
     * where
     *
     * @param column 表字段
     * @param value
     * @return
     */
    public QueryWrapper where(String column, Object value, String... val) {
        isBlank(column);
        joinNum++;
        // 获取字段val对应的JdbcType
        MySqlColumnType columnType = ParseClass2TableInfo.getColumnType((value == null ? null : value.getClass()));
        whereSqlMap.put(MySqlKeyWord.WHERE.getValue() + joinNum,
                String.format(" %s %s=#{%s,jdbcType=%s}", MySqlKeyWord.AND.getValue(), column, VALIDEN + column + joinNum, columnType.getValue()));
        valMap.put(column + joinNum, value);
        return this;
    }

    /**
     * or
     *
     * @param column 表字段
     * @param value
     * @return
     */
    public QueryWrapper or(String column, Object value) {
        isBlank(column);
        joinNum++;
        MySqlColumnType columnType = ParseClass2TableInfo.getColumnType((value == null ? null : value.getClass()));
        whereSqlMap.put(MySqlKeyWord.WHERE.getValue() + joinNum,
                String.format(" %s %s=#{%s,jdbcType=%s}", MySqlKeyWord.OR.getValue(), column, VALIDEN + column + joinNum, columnType.getValue()));
        valMap.put(column + joinNum, value);
        return this;
    }

    /**
     * 模糊匹配
     *
     * @param column 表字段
     * @param value
     * @return
     */
    public QueryWrapper like(String column, Object value) {
        isBlank(column);
        joinNum++;
        // 获取字段val对应的JdbcType
        MySqlColumnType columnType = ParseClass2TableInfo.getColumnType((value == null ? null : value.getClass()));
        whereSqlMap.put(MySqlKeyWord.WHERE.getValue() + joinNum,
                String.format(" %s %s like concat('%%',#{%s,jdbcType=%s},'%%')", MySqlKeyWord.AND.getValue(), column, VALIDEN + column + joinNum, columnType.getValue()));
        valMap.put(column + joinNum, value);
        return this;
    }

    /**
     * 范围匹配
     *
     * @param column 表字段
     * @param begin  起始值
     * @param end    结束值
     * @return
     */
    public QueryWrapper between(String column, Object begin, Object end) {
        isBlank(column);
        joinNum++;
        // 获取字段val对应的JdbcType
        MySqlColumnType columnType = ParseClass2TableInfo.getColumnType(begin == null ? null : begin.getClass());
        whereSqlMap.put(MySqlKeyWord.WHERE.getValue() + joinNum,
                String.format(" %s %s between #{%s,jdbcType=%s} and #{%s,jdbcType=%s}", MySqlKeyWord.AND.getValue(), column, VALIDEN + column + "1", columnType.getValue(), VALIDEN + column + "2", columnType.getValue()));
        valMap.put(column + "1", begin);
        valMap.put(column + "2", end);
        return this;
    }

    /**
     * 等于null
     *
     * @param column 表字段
     * @return
     */
    public QueryWrapper isNull(String column) {
        isBlank(column);
        joinNum++;
        whereSqlMap.put(MySqlKeyWord.WHERE.getValue() + joinNum, String.format(" %s %s is null", MySqlKeyWord.AND.getValue(), column));
        return this;
    }

    /**
     * 不等于null
     *
     * @param column
     * @return
     */
    public QueryWrapper isNotNull(String column) {
        isBlank(column);
        joinNum++;
        whereSqlMap.put(MySqlKeyWord.WHERE.getValue() + joinNum, String.format(" %s %s is not null", MySqlKeyWord.AND.getValue(), column));
        return this;
    }

    /**
     * 等于null或者等于''
     *
     * @param column
     * @return
     */
    public QueryWrapper isNullOrEqual(String column) {
        isBlank(column);
        joinNum++;
        whereSqlMap.put(MySqlKeyWord.WHERE.getValue() + joinNum, String.format(" %s %s is null %s %s=''", MySqlKeyWord.AND.getValue(), column, MySqlKeyWord.OR.getValue(), column));
        return this;
    }

    /**
     * 不等于null或者不等于''
     *
     * @param column
     * @return
     */
    public QueryWrapper isNotNullOrEqual(String column) {
        isBlank(column);
        joinNum++;
        whereSqlMap.put(MySqlKeyWord.WHERE.getValue() + joinNum, String.format(" %s %s is not null %s %s<>''", MySqlKeyWord.AND.getValue(), column, MySqlKeyWord.OR.getValue(), column));
        return this;
    }

    /**
     * 不等于某个条件
     *
     * @param column
     * @param value
     * @return
     */
    public QueryWrapper notEqual(String column, Object value) {
        isBlank(column);
        joinNum++;
        // 获取字段val对应的JdbcType
        MySqlColumnType columnType = ParseClass2TableInfo.getColumnType(value == null ? null : value.getClass());
        whereSqlMap.put(MySqlKeyWord.WHERE.getValue() + joinNum,
                String.format(" %s %s<>#{%s,jdbcType=%s}", MySqlKeyWord.AND.getValue(), column, VALIDEN + column + joinNum, columnType.getValue()));
        valMap.put(column + joinNum, value);
        return this;
    }

    /**
     * 匹配多个值
     *
     * @param column
     * @param values
     * @return
     */
    public QueryWrapper in(String column, Object... values) {
        isBlank(column);
        sqlMap.put(MySqlKeyWord.IN.getValue(), String.format(" %s %s %s", MySqlKeyWord.AND.getValue(), column, MySqlKeyWord.IN.getValue()));
        valMap.put(MySqlKeyWord.IN.getValue(), values);
        return this;
    }

    /**
     * 不匹配多个值
     *
     * @param column
     * @param values
     * @return
     */
    public QueryWrapper notIn(String column, Object... values) {
        isBlank(column);
        sqlMap.put(MySqlKeyWord.NOT.getValue() + "IN", String.format(" %s %s %s %s", MySqlKeyWord.AND.getValue(), column, MySqlKeyWord.NOT.getValue(), MySqlKeyWord.IN.getValue()));
        valMap.put(MySqlKeyWord.NOT.getValue() + "IN", values);
        return this;
    }

    /**
     * 大于
     *
     * @param column
     * @param value
     * @return
     */
    public QueryWrapper gt(String column, Object value) {
        isBlank(column);
        joinNum++;
        // 获取字段val对应的JdbcType
        MySqlColumnType columnType = ParseClass2TableInfo.getColumnType((value == null ? null : value.getClass()));
        whereSqlMap.put(MySqlKeyWord.WHERE.getValue() + joinNum,
                String.format(" %s %s>#{%s,jdbcType=%s}", MySqlKeyWord.AND.getValue(), column, VALIDEN + column + joinNum, columnType.getValue()));
        valMap.put(column + joinNum, value);
        return this;
    }

    /**
     * 小于
     *
     * @param column
     * @param value
     * @return
     */
    public QueryWrapper lt(String column, Object value) {
        isBlank(column);
        joinNum++;
        // 获取字段val对应的JdbcType
        MySqlColumnType columnType = ParseClass2TableInfo.getColumnType((value == null ? null : value.getClass()));
        whereSqlMap.put(MySqlKeyWord.WHERE.getValue() + joinNum,
                String.format(" %s %s<#{%s,jdbcType=%s}", MySqlKeyWord.AND.getValue(), column, VALIDEN + column + joinNum, columnType.getValue()));
        valMap.put(column + joinNum, value);
        return this;
    }

    /**
     * 大于等于
     *
     * @param column
     * @param value
     * @return
     */
    public QueryWrapper ge(String column, Object value) {
        isBlank(column);
        joinNum++;
        // 获取字段val对应的JdbcType
        MySqlColumnType columnType = ParseClass2TableInfo.getColumnType((value == null ? null : value.getClass()));
        whereSqlMap.put(MySqlKeyWord.WHERE.getValue() + joinNum,
                String.format(" %s %s>=#{%s,jdbcType=%s}", MySqlKeyWord.AND.getValue(), column, VALIDEN + column + joinNum, columnType.getValue()));
        valMap.put(column + joinNum, value);
        return this;
    }

    /**
     * 小于等于
     *
     * @param column
     * @param value
     * @return
     */
    public QueryWrapper le(String column, Object value) {
        isBlank(column);
        joinNum++;
        // 获取字段val对应的JdbcType
        MySqlColumnType columnType = ParseClass2TableInfo.getColumnType((value == null ? null : value.getClass()));
        whereSqlMap.put(MySqlKeyWord.WHERE.getValue() + joinNum,
                String.format(" %s %s<=#{%s,jdbcType=%s}", MySqlKeyWord.AND.getValue(), column, VALIDEN + column + joinNum, columnType.getValue()));
        valMap.put(column + joinNum, value);
        return this;
    }

    /**
     * 设置结果集字段
     *
     * @param columns
     * @return
     */
    public QueryWrapper setColumn(String columns) {
        isBlank(columns);
        sqlMap.put("setColumn", columns);
        return this;
    }

    /**
     * 结果集排序
     *
     * @param columns
     * @param orderType
     * @return
     */
    public QueryWrapper orderBy(String columns, String orderType) {
        isBlank(columns);
        sqlMap.put(MySqlKeyWord.ORDER_BY.getValue().replace(" ", ""), String.format(" %s %s %s", MySqlKeyWord.ORDER_BY.getValue(), columns, orderType));
        return this;
    }

    /**
     * 结果集分页
     *
     * @param page
     * @param limit
     * @return
     */
    public QueryWrapper limit(int page, int limit) {
        sqlMap.put(MySqlKeyWord.LIMIT.getValue(), String.format(" %s %s,%s", MySqlKeyWord.LIMIT.getValue(), (page - 1) * limit, limit));
        return this;
    }

    /************************************************** 连接查询暂时不实现 ***********************************************************/

    public QueryWrapper leftJoin(String tableName, String on) {
        return this;
    }

    public QueryWrapper rightJoin(String tableName, String on) {
        return this;
    }

    public QueryWrapper innerJoin(String tableName, String on) {
        return this;
    }

    /************************************************** 连接查询暂时不实现 ***********************************************************/

    private void isBlank(String column) {
        if (StringUtils.isBlank(column)) {
            throw new MyBatisRabbitPlugException("要查询的字段为空......");
        }
    }

    /**
     * sqlMap合并
     *
     * @return Map<String, Object>
     */
    public Map<String, Object> mergeSqlMap() {
        sqlMap.put(MySqlKeyWord.WHERE.getValue(), whereSqlMap);
        sqlMap.put(MySqlKeyWord.JOINWD.getValue(), joinSqlMap);
        sqlMap.put(MySqlKeyWord.VALUE.getValue(), valMap);
        log.info("{}: QueryWrapper -> Sql: {}", TAG, JSONUtil.toJsonStr(sqlMap));
        return sqlMap;
    }
}
