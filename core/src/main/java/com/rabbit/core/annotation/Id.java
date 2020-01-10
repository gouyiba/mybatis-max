package com.rabbit.core.annotation;

import com.rabbit.core.enumation.MySqlColumnType;
import com.rabbit.core.enumation.PrimaryKey;

import java.lang.annotation.*;

/**
 * 主键ID
 *
 * @author duxiaoyu
 * @since 2019-12-12
 */
@Documented
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Id {

    /**
     * 主键的columnName
     * @return
     */
    String value() default "";

    /**
     * 是否是自增字段
     * @return
     */
    boolean isIncrementColumn() default false;

    /**
     * 主键生成策略
     * @return
     */
    PrimaryKey generateType() default PrimaryKey.OBJECTID;

    /**
     * 工作机器ID (0-31) 只有使用分布式雪花算法时才设置该属性，默认为0
     * @return
     */
    int workerId() default 0;

    /**
     * 数据中心ID (0-31) 只有使用分布式雪花算法时才设置该属性，默认为0
     * @return
     */
    int datacenterId() default 0;

    /**
     * 数据库字段类型，默认VARCHAR
     * @return
     */
    MySqlColumnType columnType() default MySqlColumnType.VARCHAR;
}
