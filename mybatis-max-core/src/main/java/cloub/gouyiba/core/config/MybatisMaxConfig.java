package cloub.gouyiba.core.config;

import cloub.gouyiba.core.injector.IMybatisMaxSqlInjector;
import cloub.gouyiba.core.mapper.BaseMapper;

import java.io.Serializable;

/**
 * this class by created wuyongfei on 2020/5/10 20:40
 **/
public class MybatisMaxConfig implements Serializable {
    private IMybatisMaxSqlInjector sqlInjector;
    private Class<?> superMapperClass = BaseMapper.class;

    public IMybatisMaxSqlInjector getSqlInjector() {
        return this.sqlInjector;
    }

    public MybatisMaxConfig setSqlInjector(final IMybatisMaxSqlInjector sqlInjector) {
        this.sqlInjector = sqlInjector;
        return this;
    }

    public Class<?> getSuperMapperClass() {
        return this.superMapperClass;
    }
}