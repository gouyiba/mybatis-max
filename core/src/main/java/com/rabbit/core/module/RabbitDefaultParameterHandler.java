package com.rabbit.core.module;

import com.rabbit.core.annotation.Create;
import com.rabbit.core.annotation.Update;
import com.rabbit.core.bean.TableInfo;
import com.rabbit.core.parse.ParseClass2TableInfo;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.ibatis.executor.ErrorContext;
import org.apache.ibatis.mapping.*;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.scripting.defaults.DefaultParameterHandler;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.TypeException;
import org.apache.ibatis.type.TypeHandler;
import org.apache.ibatis.type.TypeHandlerRegistry;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public class RabbitDefaultParameterHandler extends DefaultParameterHandler {
    private final TypeHandlerRegistry typeHandlerRegistry;
    private final MappedStatement mappedStatement;
    private final Object parameterObject;
    private final BoundSql boundSql;
    private final Configuration configuration;

    public RabbitDefaultParameterHandler(MappedStatement mappedStatement, Object parameterObject, BoundSql boundSql) throws InvocationTargetException, IllegalAccessException {
        super(mappedStatement, parameterObject, boundSql);
        this.mappedStatement = mappedStatement;
        this.configuration = mappedStatement.getConfiguration();
        this.typeHandlerRegistry = mappedStatement.getConfiguration().getTypeHandlerRegistry();
        this.parameterObject = parameterObject;
        this.boundSql = boundSql;

        // 检查执行填充方法
        TableInfo tableInfo = ParseClass2TableInfo.getTableInfo(parameterObject.getClass());
        if (ObjectUtils.isNotEmpty(tableInfo)) {
            // 拿到解析后的填充方法
            Map<Class<? extends Annotation>, Method> fillMethodMap = tableInfo.getFillMethods();

            Method createMethod = fillMethodMap.get(Create.class);
            Method updateMethod = fillMethodMap.get(Update.class);

            /* 根据SQL类型执行对应的填充方法 */

            if (mappedStatement.getSqlCommandType() == SqlCommandType.INSERT && ObjectUtils.isNotEmpty(createMethod)) {
                createMethod.setAccessible(true); // 公开私有
                createMethod.invoke(parameterObject);
            }

            if (mappedStatement.getSqlCommandType() == SqlCommandType.UPDATE && ObjectUtils.isNotEmpty(createMethod)) {
                updateMethod.setAccessible(true); // 公开私有
                updateMethod.invoke(parameterObject);
            }

        }

        // TODO: 主键生成

    }

    @Override
    public Object getParameterObject() {
        return this.parameterObject;
    }

    @Override
    public void setParameters(PreparedStatement ps) {
        ErrorContext.instance().activity("setting parameters").object(this.mappedStatement.getParameterMap().getId());
        List<ParameterMapping> parameterMappings = this.boundSql.getParameterMappings();
        if (parameterMappings != null) {
            for (int i = 0; i < parameterMappings.size(); ++i) {
                ParameterMapping parameterMapping = (ParameterMapping) parameterMappings.get(i);
                if (parameterMapping.getMode() != ParameterMode.OUT) {
                    String propertyName = parameterMapping.getProperty();
                    Object value;
                    if (this.boundSql.hasAdditionalParameter(propertyName)) {
                        value = this.boundSql.getAdditionalParameter(propertyName);
                    } else if (this.parameterObject == null) {
                        value = null;
                    } else if (this.typeHandlerRegistry.hasTypeHandler(this.parameterObject.getClass())) {
                        value = this.parameterObject;
                    } else {
                        MetaObject metaObject = this.configuration.newMetaObject(this.parameterObject);
                        value = metaObject.getValue(propertyName);
                    }

                    TypeHandler typeHandler = parameterMapping.getTypeHandler();
                    JdbcType jdbcType = parameterMapping.getJdbcType();
                    if (value == null && jdbcType == null) {
                        jdbcType = this.configuration.getJdbcTypeForNull();
                    }

                    try {
                        typeHandler.setParameter(ps, i + 1, value, jdbcType);
                    } catch (SQLException | TypeException var10) {
                        throw new TypeException("Could not set parameters for mapping: " + parameterMapping + ". Cause: " + var10, var10);
                    }
                }
            }
        }
    }

}
