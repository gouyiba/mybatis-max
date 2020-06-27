package cloud.gouyiba.core.module;

import cloud.gouyiba.common.exception.MyBatisMaxException;
import org.apache.ibatis.executor.parameter.ParameterHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.scripting.xmltags.XMLLanguageDriver;

import java.lang.reflect.InvocationTargetException;

public class MybatisMaxXMLLanguageDriver extends XMLLanguageDriver {
    @Override
    public ParameterHandler createParameterHandler(MappedStatement mappedStatement,
                                                   Object parameterObject, BoundSql boundSql) {
        try {
            return new MybatisMaxDefaultParameterHandler(mappedStatement, parameterObject, boundSql);
        } catch (InvocationTargetException e) {
            throw new MyBatisMaxException("Could not set parameters for parse fill method. Cause: " + e);
        } catch (IllegalAccessException e) {
            throw new MyBatisMaxException("Could not set parameters for parse fill method. Cause: " + e);
        }
    }
}
