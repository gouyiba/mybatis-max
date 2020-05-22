package com.rabbit.core.config;

import com.rabbit.core.injector.IRabbitSqlInjector;
import com.rabbit.core.mapper.BaseMapper;

import java.io.Serializable;

/**
 * this class by created wuyongfei on 2020/5/10 20:40
 **/
public class RabbitConfig implements Serializable {
    private IRabbitSqlInjector sqlInjector;
    private Class<?> superMapperClass = BaseMapper.class;

    public IRabbitSqlInjector getSqlInjector() {
        return this.sqlInjector;
    }

    public RabbitConfig setSqlInjector(final IRabbitSqlInjector sqlInjector) {
        this.sqlInjector = sqlInjector;
        return this;
    }

    public Class<?> getSuperMapperClass() {
        return this.superMapperClass;
    }
}