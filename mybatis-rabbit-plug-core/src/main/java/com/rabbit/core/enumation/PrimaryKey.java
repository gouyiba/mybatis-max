package com.rabbit.core.enumation;

/**
 * 数据库主键生成枚举
 *
 * @author duxiaoyu
 * @since 2019-12-12
 */
public enum PrimaryKey implements IEnum<String> {

    /**
     * UUID32位
     * type: String
     */
    UUID32("UUID32"),

    /**
     * UUID64位
     * type: String
     */
    UUID64("UUID64"),

    /**
     * objectID(mongoDB主键生成策略)
     * type: String
     */
    OBJECTID("OBJECTID"),

    /**
     * 分布式雪花算法主键生成策略
     * type: long
     */
    SNOWFLAKE("SNOWFLAKE");

    private String generateType;

    private PrimaryKey(String generateType) {
        this.generateType = generateType;
    }

    /**
     * 获取value对应枚举
     *
     * @param value
     * @return
     */
    public static PrimaryKey valueConvertEnum(String value) {
        for (PrimaryKey primaryKey : PrimaryKey.values()) {
            if (primaryKey.generateType.equals(value)) {
                return primaryKey;
            }
        }
        return null;
    }

    @Override
    public String getValue() {
        return this.generateType;
    }
}
