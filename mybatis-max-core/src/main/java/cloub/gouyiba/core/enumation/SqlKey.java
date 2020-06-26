package cloub.gouyiba.core.enumation;

/**
 * sql-key标识
 *
 * @author duxiaoyu
 * @since 2019-12-25
 */
public enum SqlKey implements IEnum<String> {

    // 实际的表名
    TABLE_NAME("TABLE_NAME"),

    /************************** Select ****************************/

    // select
    SELECT_HEAD("SELECT_HEAD"),
    // where
    WHERE_CON("WHERE_CON"),
    // group by
    GROUP_BY_CON("GROUP_BY_CON"),
    // having
    HAVING_CON("HAVING_CON"),
    // order by
    ORDER_BY_CON("ORDER_BY_CON"),

    /************************** Insert ****************************/

    // insert into
    INSERT_HEAD("INSERT_HEAD"),
    // field1,field2,field3......
    INSERT_PARAMETER("INSERT_PARAMETER"),
    // value-keyword
    INSERT_VALUE_KEYWORD("INSERT_VALUE_KEYWORD"),
    // 参数左括号: (
    INSERT_PAM_LEFT_BRA("INSERT_PAM_LEFT_BRA"),
    // 参数右括号: )
    INSERT_PAM_RIGHT_BRA("INSERT_PAM_RIGHT_BRA"),
    // value...
    INSERT_VALUE("INSERT_VALUE"),
    // value左括号: (
    INSERT_VAL_LEFT_BRA("INSERT_VAL_LEFT_BRA"),
    // value右括号: )
    INSERT_VAL_RIGHT_BRA("INSERT_VAL_RIGHT_BRA"),

    /************************** Update ****************************/

    // update
    UPDATE_HEAD("UPDATE_HEAD"),
    // set
    UPDATE_SET("UPDATE_SET"),
    // field1=value,field2=value，field3=value
    UPDATE_VALUE("UPDATE_VALUE"),
    UPDATE_WHERE("UPDATE_WHERE"),

    /************************** Delete ****************************/

    // delete
    DELETE_HEAD("DELETE_HEAD"),
    DELETE_WHERE("DELETE_WHERE");

    private String sqlKey;

    private SqlKey(String sqlKey) {
        this.sqlKey = sqlKey;
    }

    /**
     * 获取value对应枚举
     *
     * @param value
     * @return
     */
    public static SqlKey convert(String value) {
        for (SqlKey sqlKeyItem : SqlKey.values()) {
            if (sqlKeyItem.sqlKey.equals(value)) {
                return sqlKeyItem;
            }
        }
        return null;
    }

    @Override
    public String getValue() {
        return this.sqlKey;
    }

}
