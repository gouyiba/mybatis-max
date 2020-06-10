package com.rabbit.core.module;

import cn.hutool.core.lang.Snowflake;
import cn.hutool.core.util.IdUtil;
import com.rabbit.common.utils.StringUtils;
import com.rabbit.core.annotation.Create;
import com.rabbit.core.annotation.Id;
import com.rabbit.core.annotation.Update;
import com.rabbit.core.bean.TableFieldInfo;
import com.rabbit.core.bean.TableInfo;
import com.rabbit.core.constructor.DeleteWrapper;
import com.rabbit.core.constructor.UpdateWrapper;
import com.rabbit.core.enumation.PrimaryKey;
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
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

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

        Class<?> parameterClass = null;
        List<Object> objectList = new ArrayList<>();
        if (parameterObject instanceof Map) {
            Map<String, Object> paramMap = (Map<String, Object>) parameterObject;
            for (Map.Entry<String, Object> key : paramMap.entrySet()) {
                if (key.getValue() instanceof List) {
                    objectList = (List<Object>) key.getValue();
                    break;
                } else {
                    objectList.add(key.getValue());
                    break;
                }
            }
            if (objectList.size() > 0) {
                parameterClass = objectList.get(0).getClass();
            }
        } else {
            objectList.add(parameterObject);
            parameterClass = parameterObject.getClass();
        }

        // 检查执行填充方法
        TableInfo tableInfo = ParseClass2TableInfo.getTableInfo(parameterClass);
        if (ObjectUtils.isNotEmpty(tableInfo)) {
            // 拿到解析后的填充方法
            Map<Class<? extends Annotation>, Method> fillMethodMap = tableInfo.getFillMethods();

            Method createMethod = fillMethodMap.get(Create.class);
            Method updateMethod = fillMethodMap.get(Update.class);

            /* 根据SQL类型执行对应的填充方法 */

            if (mappedStatement.getSqlCommandType() == SqlCommandType.INSERT && ObjectUtils.isNotEmpty(createMethod)) {
                createMethod.setAccessible(true); // 公开私有
                for (Object item : objectList) {
                    createMethod.invoke(item);
                }
            }

            if (mappedStatement.getSqlCommandType() == SqlCommandType.UPDATE && ObjectUtils.isNotEmpty(updateMethod)) {
                updateMethod.setAccessible(true); // 公开私有
                for (Object item : objectList) {
                    updateMethod.invoke(item);
                }
            }
        }

        // TODO: 主键生成
        Field primaryKey = Objects.isNull(tableInfo) ? null : tableInfo.getPrimaryKey();
        if (Objects.nonNull(primaryKey) && mappedStatement.getSqlCommandType() == SqlCommandType.INSERT) {
            Id id = primaryKey.getAnnotation(Id.class);
            PrimaryKey pkEnum = id.generateType();
            Object idValue = null;
            if (id.isKeyGenerator() && !id.isIncrementColumn()) {
                for (Object item : objectList) {
                    switch (pkEnum) {
                        case UUID32:
                            idValue = IdUtil.simpleUUID();
                            break;
                        case OBJECTID:
                            idValue = IdUtil.objectId();
                            break;
                        case SNOWFLAKE:
                            // 根据雪花算法生成64bit大小的分布式Long类型id，需要在 @id 中设置workerId和datacenterId
                            Snowflake snowflake = IdUtil.getSnowflake(id.workerId(), id.datacenterId());
                            idValue = snowflake.nextId();
                            break;
                        default:
                            break;
                    }
                    try {
                        // 处理主键生成
                        Map<String, TableFieldInfo> fieldInfoMap = tableInfo.getColumnMap();
                        Method setPrimaryKey = item.getClass().getMethod("set" + StringUtils.capitalize(primaryKey.getName()), fieldInfoMap.get(primaryKey.getName()).getPropertyType());
                        setPrimaryKey.setAccessible(true);
                        setPrimaryKey.invoke(item, idValue);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }

        // 处理BaseMapper中的update和delete方法条件参数问题
        if (mappedStatement.getSqlCommandType() == SqlCommandType.UPDATE || mappedStatement.getSqlCommandType() == SqlCommandType.DELETE) {
            if (parameterObject instanceof UpdateWrapper) {
                UpdateWrapper updateWrapper = (UpdateWrapper) parameterObject;
                updateWrapper.setQueryWrapper(updateWrapper.getValMap());
            } else if (parameterObject instanceof DeleteWrapper) {
                DeleteWrapper deleteWrapper = (DeleteWrapper) parameterObject;
                deleteWrapper.setQueryWrapper(deleteWrapper.getValMap());
            }
        }
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
