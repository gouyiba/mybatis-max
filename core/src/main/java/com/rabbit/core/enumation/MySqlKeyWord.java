package com.rabbit.core.enumation;

/**
 * MySql数据库关键字枚举
 *
 * @author duxiaoyu
 * @since 2019-12-12
 */
public enum MySqlKeyWord implements IEnum<String> {

    AND("AND"),
    OR("OR"),
    IN("IN"),
    NOT("NOT"),
    LIKE("LIKE"),
    EQ("="),
    NE("<>"),
    GT(">"),
    GE(">="),
    LT("<"),
    LE("<="),
    IS_NULL("IS NULL"),
    IS_NOT_NULL("IS NOT NULL"),
    GROUP_BY("GROUP BY"),
    HAVING("HAVING"),
    ORDER_BY("ORDER BY"),
    EXISTS("EXISTS"),
    BETWEEN("BETWEEN"),
    ASC("ASC"),
    DESC("DESC"),
    SELECT("SELECT"),
    WHERE("WHERE"),
    ALL("*"),
    FROM("FROM"),
    LEFT_JOIN("LEFT JOIN"),
    RIGHT_JOIN("RIGHT JOIN"),
    JOIN("INNER JOIN"),
    ON("ON"),
    INSERT("INSERT INTO"),
    VALUE("VALUE"),
    VALUES("VALUES"),
    UPDATE("UPDATE"),
    SET("SET"),
    DELETE("DELETE"),
    JOINWD("JOINWD"),
    LIMIT("LIMIT");
    private String keyword;

    private MySqlKeyWord(String keyword) {
        this.keyword = keyword;
    }

    /**
     * 获取value对应枚举
     *
     * @param value
     * @return
     */
    public static MySqlKeyWord valueConvertEnum(String value) {
        for (MySqlKeyWord mySqlKeyWord : MySqlKeyWord.values()) {
            if (mySqlKeyWord.keyword.equals(value)) {
                return mySqlKeyWord;
            }
        }
        return null;
    }

    /**
     * 获取枚举对应value
     *
     * @return
     */
    @Override
    public String getValue() {
        return this.keyword;
    }
}
