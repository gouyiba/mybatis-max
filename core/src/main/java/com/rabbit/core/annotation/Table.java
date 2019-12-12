package com.rabbit.core.annotation;

import java.lang.annotation.*;

/**
 * 数据库表
 *
 * @author duxiaoyu
 * @since 2019-12-12
 */
@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Table {

    /**
     * 数据库表名称
     * @return
     */
    String value() default "";
}
