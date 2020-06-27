package cloud.gouyiba.core.injector;

import org.apache.ibatis.builder.MapperBuilderAssistant;

/**
 * this class by created wuyongfei on 2020/5/10 13:50
 **/
public interface IMybatisMaxSqlInjector {
    void inspectInject(MapperBuilderAssistant builderAssistant, Class<?> mapperClass);
}
