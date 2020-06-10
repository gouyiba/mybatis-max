package com.rabbit.core.typehandler;

import com.rabbit.core.enumation.IEnum;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @ClassName StringEnumTypeHandler
 * @ClassExplain: 用于bean中的枚举属性转换-String类型
 * @Author Duxiaoyu
 * @Date 2020/5/30 16:21
 * @Since V 1.0
 */
public class StringEnumTypeHandler<E extends Enum<?> & IEnum<E>> extends BaseTypeHandler<IEnum> {

    private Class<E> clazz;

    public StringEnumTypeHandler() {}

    public StringEnumTypeHandler(Class<E> enumType) {
        if (enumType == null) {
            throw new IllegalArgumentException("Type argument cannot be null");
        }
        clazz = enumType;
    }

    @Override
    public void setNonNullParameter(PreparedStatement preparedStatement, int i, IEnum iEnum, JdbcType jdbcType) throws SQLException {
        preparedStatement.setString(i, (String) iEnum.getValue());
    }

    @Override
    public IEnum getNullableResult(ResultSet resultSet, String s) throws SQLException {
        return convert(clazz, resultSet.getString(s));
    }

    @Override
    public IEnum getNullableResult(ResultSet resultSet, int i) throws SQLException {
        return convert(clazz, resultSet.getString(i));
    }

    @Override
    public IEnum getNullableResult(CallableStatement callableStatement, int i) throws SQLException {
        return convert(clazz, callableStatement.getString(i));
    }

    private <T extends Enum<?> & IEnum> T convert(Class<T> enumClass, String value) {
        T[] enumConstants = enumClass.getEnumConstants();
        for (T t : enumConstants) {
            if (t.getValue().toString().equals(value)) {
                return t;
            }
        }
        return null;
    }
}
