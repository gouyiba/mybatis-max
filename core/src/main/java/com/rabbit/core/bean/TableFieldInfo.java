package com.rabbit.core.bean;

import lombok.Data;

import java.io.Serializable;
import java.lang.reflect.Field;

/**
 * 数据库表字段信息
 *
 * @author duxiaoyu
 * @since 2019-12-12
 */
@Data
public class TableFieldInfo implements Serializable {

    /**
     * 属性
     */
    private final Field field;

    /**
     * 属性名
     */
    private final String propertyName;

    /**
     * 数据库表字段名
     */
    private final String columnName;

    /**
     * 属性类型
     */
    private Class<?> propertyType;

    /**
     * 数据库表字段类型
     */
    private final String columnType;
}
