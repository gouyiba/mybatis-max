package com.rabbit.core.util;

import com.rabbit.core.config.RabbitConfig;
import com.rabbit.core.injector.DefaultRabbitSqlInjector;
import com.rabbit.core.injector.IRabbitSqlInjector;
import com.rabbit.core.module.RabbitConfiguration;
import org.apache.ibatis.session.Configuration;

/**
 * this class by created wuyongfei on 2020/5/10 20:52
 **/
public class RabbitConfigUtils {
    public RabbitConfigUtils() {
    }

    public static RabbitConfig defaults() {
        return (new RabbitConfig());
    }

    public static RabbitConfig getGlobalConfig(Configuration configuration) {
        return ((RabbitConfiguration) configuration).getGlobalConfig();
    }

    public static IRabbitSqlInjector getSqlInjector(Configuration configuration) {
        RabbitConfig globalConfiguration = getGlobalConfig(configuration);
        IRabbitSqlInjector sqlInjector = globalConfiguration.getSqlInjector();
        if (sqlInjector == null) {
            sqlInjector = new DefaultRabbitSqlInjector();
            globalConfiguration.setSqlInjector(sqlInjector);
        }

        return sqlInjector;
    }

    public static Class<?> getSuperMapperClass(Configuration configuration) {
        return getGlobalConfig(configuration).getSuperMapperClass();
    }

    public static boolean isSupperMapperChildren(Configuration configuration, Class<?> mapperClass) {
        return getSuperMapperClass(configuration).isAssignableFrom(mapperClass);
    }
}
