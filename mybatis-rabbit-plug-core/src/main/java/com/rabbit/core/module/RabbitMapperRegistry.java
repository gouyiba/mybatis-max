package com.rabbit.core.module;

import com.rabbit.core.proxy.RabbitMapperProxyFactory;
import org.apache.ibatis.binding.BindingException;
import org.apache.ibatis.binding.MapperRegistry;
import org.apache.ibatis.session.SqlSession;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * this class by created wuyongfei on 2020/5/4 16:28
 **/
public class RabbitMapperRegistry extends MapperRegistry {
    private final RabbitConfiguration config;
    private final Map<Class<?>, RabbitMapperProxyFactory<?>> knownMappers = new HashMap<>();

    public RabbitMapperRegistry(RabbitConfiguration config) {
        super(config);
        this.config = config;
    }

    @Override
    public <T> T getMapper(Class<T> type, SqlSession sqlSession) {
        final RabbitMapperProxyFactory<T> mapperProxyFactory = (RabbitMapperProxyFactory<T>) this.knownMappers.get(type);
        if (mapperProxyFactory == null) {
            throw new BindingException("Type " + type + " is not known to the MybatisPlusMapperRegistry.");
        }
        try {
            return mapperProxyFactory.newInstance(sqlSession);
        } catch (Exception e) {
            throw new BindingException("Error getting mapper instance. Cause: " + e, e);
        }
    }

    @Override
    public <T> boolean hasMapper(Class<T> type) {
        return this.knownMappers.containsKey(type);
    }

    @Override
    public <T> void addMapper(Class<T> type) {
        if (type.isInterface()) {
            if (hasMapper(type)) {
                throw new BindingException("Type " + type + " is already known to the MapperRegistry.");
            }
            boolean loadCompleted = false;
            try {
                this.knownMappers.put(type, new RabbitMapperProxyFactory<>(type));
                RabbitMapperAnnotationBuilder parser = new RabbitMapperAnnotationBuilder(config, type);
                parser.parse();
                loadCompleted = true;
            } finally {
                if (!loadCompleted) {
                    this.knownMappers.remove(type);
                }
            }
        }
    }

    @Override
    public Collection<Class<?>> getMappers() {
        return Collections.unmodifiableCollection(this.knownMappers.keySet());
    }
}
