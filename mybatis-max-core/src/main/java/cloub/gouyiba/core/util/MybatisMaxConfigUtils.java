package cloub.gouyiba.core.util;

import cloub.gouyiba.core.injector.DefaultMybatisMaxSqlInjector;
import cloub.gouyiba.core.injector.IMybatisMaxSqlInjector;
import cloub.gouyiba.core.module.MybatisMaxConfiguration;
import cloub.gouyiba.core.config.MybatisMaxConfig;
import org.apache.ibatis.session.Configuration;

/**
 * this class by created wuyongfei on 2020/5/10 20:52
 **/
public class MybatisMaxConfigUtils {
    public MybatisMaxConfigUtils() {
    }

    public static MybatisMaxConfig defaults() {
        return (new MybatisMaxConfig());
    }

    public static MybatisMaxConfig getGlobalConfig(Configuration configuration) {
        return ((MybatisMaxConfiguration) configuration).getGlobalConfig();
    }

    public static IMybatisMaxSqlInjector getSqlInjector(Configuration configuration) {
        MybatisMaxConfig globalConfiguration = getGlobalConfig(configuration);
        IMybatisMaxSqlInjector sqlInjector = globalConfiguration.getSqlInjector();
        if (sqlInjector == null) {
            sqlInjector = new DefaultMybatisMaxSqlInjector();
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
