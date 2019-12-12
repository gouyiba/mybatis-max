package com.rabbit.core.annotation;

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
}
