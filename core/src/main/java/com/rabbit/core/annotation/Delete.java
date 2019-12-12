package com.rabbit.core.annotation;

import java.lang.annotation.*;

/**
 * 用于删除时字段自动填充策略
 *
 * @author duxiaoyu
 * @since 2019-12-12
 */
@Documented
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Delete {
}
