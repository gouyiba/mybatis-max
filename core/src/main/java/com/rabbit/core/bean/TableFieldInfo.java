package com.rabbit.core.bean;

import com.rabbit.core.enumation.MySqlColumnType;
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
    private Field field;

    /**
     * 属性名
     */
    private String propertyName;

    /**
     * 数据库表字段名
     */
    private String columnName;

    /**
     * 属性类型
     */
    private Class<?> propertyType;

    /**
     * 数据库表字段类型
     */
    private MySqlColumnType columnType;
}
