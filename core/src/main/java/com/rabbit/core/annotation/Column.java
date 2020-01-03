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
     * 是否是数据库表中的字段
     * @return
     */
    boolean isTableColumn() default true;

    /**
     * 是否是数据库表中的自增字段
     * @return
     */
    boolean isIncrementColumn() default false;

    /**
     * 如果有特殊字段的value需要进行类型转换，则需要指定value的类型转换器
     * 如枚举：指定了类型转换器后，在执行sql前，会将字段的value按照指定类型转换器，进行转换成数据库可以保存的value
     * 从数据库返回数据时，可以转换成程序中的类型
     * 默认Object，表示不使用任何转换器
     * @return
     */
    Class<?> typeHandler() default Object.class;

    /**
     * 数据库字段类型，默认VARCHAR
     * @return
     */
    MySqlColumnType columnType() default MySqlColumnType.VARCHAR;
}
