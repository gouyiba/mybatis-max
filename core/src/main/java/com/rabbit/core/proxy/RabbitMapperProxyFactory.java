package com.rabbit.core.proxy;

import org.apache.ibatis.session.SqlSession;

import java.lang.reflect.Proxy;

/**
 * this class by created wuyongfei on 2020/5/4 16:29
 **/
public class RabbitMapperProxyFactory<T> {
    private final Class<T> mapperInterface;

    public RabbitMapperProxyFactory(Class<T> mapperInterface) {
        this.mapperInterface = mapperInterface;
    }

    protected T newInstance(RabbitMapperProxy<T> mapperProxy) {
        return (T) Proxy.newProxyInstance(mapperInterface.getClassLoader(), new Class[]{mapperInterface}, mapperProxy);
    }

    public T newInstance(SqlSession sqlSession) {
        RabbitMapperProxy<T> mapperProxy = new RabbitMapperProxy<>(sqlSession, mapperInterface);
        return newInstance(mapperProxy);
    }
}
