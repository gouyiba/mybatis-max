package com.rabbit.core.enumation;

/**
 * 数据库表字段类型枚举
 *
 * @author duxiaoyu
 * @since 2019-12-12
 */
public enum MySqlColumnType implements IEnum<String> {

    TINYINT("TINYINT"),
    BIGINT("BIGINT"),
    INT("INT"),
    FLOAT("FLOAT"),
    DOUBLE("DOUBLE"),
    DECIMAL("DECIMAL"),
    DATE("DATE"),
    DATETIME("DATETIME"),
    TIMESTAMP("TIMESTAMP"),
    CHAR("CHAR"),
    VARCHAR("VARCHAR"),
    TEXT("TEXT"),
    MEDIUMTEXT("MEDIUMTEXT"),
    LONGTEXT("LONGTEXT");

    private String columnType;

    private MySqlColumnType(String columnType) {
        this.columnType = columnType;
    }

    /**
     * 获取value对应枚举
     *
     * @param value
     * @return
     */
    public static MySqlColumnType valueConvertEnum(String value) {
        for (MySqlColumnType mySqlColumnType : MySqlColumnType.values()) {
            if (mySqlColumnType.columnType.equals(value)) {
                return mySqlColumnType;
            }
        }
        return null;
    }

    @Override
    public String getValue() {
        return this.columnType;
    }
}
