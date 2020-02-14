package com.rabbit.core.typehandler;

import com.rabbit.core.enumation.IEnum;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * 自定义枚举转换器
 * 用于bean中的枚举属性转换-int类型
 * @param <E>
 * @author duxiaoyu
 * @since 2020--2-11
 */
public class IEnumTypeHandler<E extends Enum<?> & IEnum<E>> extends BaseTypeHandler<IEnum> {

    private Class<E> clazz;

    public IEnumTypeHandler(Class<E> enumType) {
        if (enumType == null) {
            throw new IllegalArgumentException("Type argument cannot be null");
        }
        clazz = enumType;
    }

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, IEnum parameter, JdbcType jdbcType)
            throws SQLException {
        ps.setInt(i, (Integer) parameter.getValue());
    }

    @Override
    public IEnum getNullableResult(ResultSet rs, String columnName) throws SQLException {
        return convert(clazz, rs.getInt(columnName));
    }

    @Override
    public IEnum getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        return convert(clazz, rs.getInt(columnIndex));
    }

    @Override
    public IEnum getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        return convert(clazz, cs.getInt(columnIndex));
    }

    private <T extends Enum<?> & IEnum> T convert(Class<T> enumClass, int value) {
        T[] enumConstants = enumClass.getEnumConstants();
        for (T t : enumConstants) {
            if ((Integer) t.getValue() == value) {
                return t;
            }
        }
        return null;
    }
}
