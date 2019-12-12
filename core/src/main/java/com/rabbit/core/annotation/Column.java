package com.rabbit.core.annotation;

import com.rabbit.core.enumation.MySqlColumnType;

import java.lang.annotation.*;

/**
 * 字段
 *
 * @author duxiaoyu
 * @since 2019-12-12
 */
@Documented
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Column {

    /**
     * 对应数据库表字段名称
     * @return
     */
    String value() default "";

    /**
     * 数据库字段类型
     * @return
     */
    MySqlColumnType columnType() default MySqlColumnType.VARCHAR;
}
