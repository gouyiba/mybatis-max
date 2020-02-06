package com.rabbit.core.bean;

import com.rabbit.core.annotation.Column;
import lombok.Data;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 数据库表信息
 *
 * @author duxiaoyu
 * @since 2019-12-12
 */
@Data
public class TableInfo implements Serializable {

    /**
     * 每张表默认主键字段为id
     */
    //@Id(generateType = PrimaryKey.SNOWFLAKE,workerId = 1,datacenterId = 1)
    private Integer id;

    /**
     * 数据库表名称
     */
    private String tableName;

    /**
     * 指定的主键字段
     */
    private Field primaryKey;

    /**
     * 数据库表字段信息
     * Key: entity中的属性名 Value: TableFieldInfo
     */
    @Column(isTableColumn = false)
    private Map<String,TableFieldInfo> columnMap=new ConcurrentHashMap<>();

}
