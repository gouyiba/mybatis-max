package cloub.gouyiba.core.enumation;

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
     * UUID36位
     * type: String
     */
    UUID36("UUID36"),

    /**
     * objectID(mongoDB主键生成策略)
     * type: String
     */
    OBJECTID("OBJECTID"),

    /**
     * 分布式雪花算法主键生成策略
     * type: long
     */
    SNOWFLAKE("SNOWFLAKE"),

    /**
     * 不使用自动生成主键策略（默认值）
     */
    UN_KNOWN("UN_KNOWN");

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
    public static PrimaryKey convert(String value) {
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
