package com.rabbit.core.constructor;

import cn.hutool.json.JSONUtil;
import com.rabbit.common.exception.MyBatisRabbitPlugException;
import com.rabbit.core.bean.TableInfo;
import com.rabbit.core.enumation.MySqlColumnType;
import com.rabbit.core.enumation.MySqlKeyWord;
import com.rabbit.core.enumation.SqlKey;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 查询条件构造器
 *
 * @param <E>
 * @author duxiaoyu
 * @since 2019-12-12
 */
@Data
public class QueryWrapper<E> extends BaseAbstractWrapper<E> implements Serializable {

    private static final Logger LOGGER = LoggerFactory.getLogger(QueryWrapper.class);

    // query-sql-cache-map

    private Map<String, String> whereSqlMap = new ConcurrentHashMap<>();

    private Map<String, String> joinSqlMap = new ConcurrentHashMap<>();

    private Map<String, Object> valMap = new ConcurrentHashMap<>();

    private Map<String, Object> sqlMap = new ConcurrentHashMap<>();

    private TableInfo tableInfo;

    public QueryWrapper(E clazz) {
        super(clazz);
        this.tableInfo = analysisClazz();
        this.sqlMap.put(SqlKey.TABLE_NAME.getValue(), this.tableInfo.getTableName());
    }

    public QueryWrapper() {
    }


    private int joinNum = 0;

    // final-variable
    private final String VALIDEN = "valMap.";

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
    public QueryWrapper where(String column, Object value) {
        isBlank(column);
        joinNum++;
        MySqlColumnType columnType = getJdbcType(value.getClass());
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
        MySqlColumnType columnType = getJdbcType(value.getClass());
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
        MySqlColumnType columnType = getJdbcType(value.getClass());
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
        MySqlColumnType columnType = getJdbcType(begin.getClass());
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
        MySqlColumnType columnType = getJdbcType(value.getClass());
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
     * @param column
     * @param orderType
     * @return
     */
    public QueryWrapper orderBy(String column, String orderType) {
        isBlank(column);
        sqlMap.put(MySqlKeyWord.ORDER_BY.getValue().replace(" ", ""), String.format(" %s %s %s", MySqlKeyWord.ORDER_BY.getValue(), column, orderType));
        return this;
    }

    /**
     * 结果集分页
     *
     * @param offset
     * @param limit
     * @return
     */
    public QueryWrapper limit(int offset, int limit) {
        sqlMap.put(MySqlKeyWord.LIMIT.getValue(), String.format(" %s %s,%s", MySqlKeyWord.LIMIT.getValue(), offset, limit));
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
            throw new MyBatisRabbitPlugException("表字段为空......");
        }
    }

    /**
     * 获取字段val对应的JdbcType
     *
     * @param clazz
     * @return
     */
    protected MySqlColumnType getJdbcType(Class<?> clazz) {
        if (Integer.class.isAssignableFrom(clazz)) {
            return MySqlColumnType.INTEGER;
        } else if (String.class.isAssignableFrom(clazz)) {
            return MySqlColumnType.VARCHAR;
        } else if (Double.class.isAssignableFrom(clazz)) {
            return MySqlColumnType.DOUBLE;
        } else if (Float.class.isAssignableFrom(clazz)) {
            return MySqlColumnType.FLOAT;
        } else if (BigDecimal.class.isAssignableFrom(clazz)) {
            return MySqlColumnType.DECIMAL;
        } else if (Date.class.isAssignableFrom(clazz)) {
            return MySqlColumnType.DATE;
        } else if (Long.class.isAssignableFrom(clazz)) {
            return MySqlColumnType.BIGINT;
        } else if (Boolean.class.isAssignableFrom(clazz)) {
            return MySqlColumnType.TINYINT;
        } else if (Short.class.isAssignableFrom(clazz)) {
            return MySqlColumnType.SHORT;
        } else if (Character.class.isAssignableFrom(clazz)) {
            return MySqlColumnType.CHAR;
        }
        return null;
    }

    /**
     * sqlMap合并
     *
     * @return
     */
    public Map<String, Object> mergeSqlMap() {
        sqlMap.put(MySqlKeyWord.WHERE.getValue(), whereSqlMap);
        sqlMap.put(MySqlKeyWord.JOINWD.getValue(), joinSqlMap);
        sqlMap.put(MySqlKeyWord.VALUE.getValue(), valMap);
        LOGGER.info("{}: QueryWrapper -> Sql: {}", TAG, JSONUtil.toJsonStr(sqlMap));
        return sqlMap;
    }
}
