package com.rabbit.autoconfigure;

import com.rabbit.core.module.RabbitConfiguration;

/**
 * this class by created wuyongfei on 2020/5/5 13:50
 **/
@FunctionalInterface
public interface RabbitConfigurationCustomizer {
    void customize(RabbitConfiguration configuration);
}
