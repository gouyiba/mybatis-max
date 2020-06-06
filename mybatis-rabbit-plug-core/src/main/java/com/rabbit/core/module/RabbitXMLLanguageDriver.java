package com.rabbit.core.module;

import com.rabbit.common.exception.MyBatisRabbitPlugException;
import org.apache.ibatis.executor.parameter.ParameterHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.scripting.xmltags.XMLLanguageDriver;

import java.lang.reflect.InvocationTargetException;

public class RabbitXMLLanguageDriver extends XMLLanguageDriver {
    @Override
    public ParameterHandler createParameterHandler(MappedStatement mappedStatement,
                                                   Object parameterObject, BoundSql boundSql) {
        try {
            return new RabbitDefaultParameterHandler(mappedStatement, parameterObject, boundSql);
        } catch (InvocationTargetException e) {
            throw new MyBatisRabbitPlugException("Could not set parameters for parse fill method. Cause: " + e);
        } catch (IllegalAccessException e) {
            throw new MyBatisRabbitPlugException("Could not set parameters for parse fill method. Cause: " + e);
        }
    }
}
