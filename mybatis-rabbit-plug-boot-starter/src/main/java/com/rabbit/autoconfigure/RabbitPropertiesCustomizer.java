package com.rabbit.autoconfigure;

/**
 * this class by created wuyongfei on 2020/5/5 13:50
 **/
@FunctionalInterface
public interface RabbitPropertiesCustomizer {
    void customize(RabbitProperties properties);
}
