package cloub.gouyiba.autoconfigure;

import cloub.gouyiba.core.module.MybatisMaxConfiguration;

/**
 * this class by created wuyongfei on 2020/5/5 13:50
 **/
@FunctionalInterface
public interface MybatisMaxConfigurationCustomizer {
    void customize(MybatisMaxConfiguration configuration);
}
