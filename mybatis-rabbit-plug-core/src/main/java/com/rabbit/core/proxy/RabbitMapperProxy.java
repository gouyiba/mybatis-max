package com.rabbit.core.proxy;

import cn.hutool.core.map.MapUtil;
import com.rabbit.common.utils.ArrayUtils;
import com.rabbit.core.injector.AbstractRabbitSqlInjector;
import com.rabbit.core.injector.RabbitAbstractMethod;
import com.rabbit.core.injector.method.base.*;
import com.rabbit.core.injector.method.business.*;
import com.rabbit.core.mapper.BaseMapper;
import com.rabbit.core.mapper.BusinessMapper;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.binding.MapperMethod;
import org.apache.ibatis.reflection.ExceptionUtil;
import org.apache.ibatis.session.SqlSession;
import org.springframework.util.CollectionUtils;

import java.lang.reflect.*;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * this class by created wuyongfei on 2020/5/4 15:53
 **/
public class RabbitMapperProxy<T> implements InvocationHandler {
    private final SqlSession sqlSession;
    private final Class<T> mapperInterface;

    public RabbitMapperProxy(SqlSession sqlSession, Class<T> mapperInterface) {
        this.sqlSession = sqlSession;
        this.mapperInterface = mapperInterface;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        Object obj = verifyParameter(method, args);
        if (Objects.nonNull(obj)) {
            return obj;
        }
        try {
            if (Object.class.equals(method.getDeclaringClass())) {
                return method.invoke(this, args);
            }
        } catch (Throwable throwable) {
            throw ExceptionUtil.unwrapThrowable(throwable);
        }
        MapperMethod mapperMethod = new MapperMethod(this.mapperInterface, method, this.sqlSession.getConfiguration());
        return mapperMethod.execute(this.sqlSession, args);
    }

    public Object verifyParameter(Method method, Object[] args) throws Exception {
        Object obj = null;
        if (BaseMapper.class.getName().equals(method.getDeclaringClass().getName())) {
            Class<?> clazz = null;
            Type type = method.getAnnotatedReturnType().getType();
            if (type instanceof Class<?>) {
                clazz = (Class<?>) type;
                if (int.class.isAssignableFrom(clazz)) {
                    obj = 0;
                } else if (Integer.class.isAssignableFrom(clazz)) {
                    obj = Integer.valueOf(0);
                } else if (long.class.isAssignableFrom(clazz)) {
                    obj = 0L;
                } else if (Long.class.isAssignableFrom(clazz)) {
                    obj = Long.valueOf(0L);
                } else if (String.class.isAssignableFrom(clazz)) {
                    obj = new String();
                }
            }
            for (Object item : args) {
                if (item instanceof Map) {
                    if (CollectionUtils.isEmpty((Map) item)) {
                        return obj;
                    }
                } else if (item instanceof Collection) {
                    if (CollectionUtils.isEmpty((Collection) item)) {
                        return obj;
                    }
                } else {
                    if (Objects.isNull(item)) {
                        return obj;
                    }
                }
            }
        }
        return null;
    }
}
