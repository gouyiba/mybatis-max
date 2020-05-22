package com.rabbit.core.service;

import com.rabbit.core.constructor.QueryWrapper;

import java.util.List;
import java.util.Map;

/**
 * $$$$$$$%!;!!$$|;``;
 * $$$$$$$%!;!!$$$$|;``;
 * &$%||%%%|!!!||||%;'.  `!
 * %%%||%%%%||||||||;'``  `!
 * %%%%%%%%%||%|||||!'`.   .'!
 * %%%%%%%%%%%%%%%|%%|;`.      `!
 * $$$%%||%%%|!'.   .::. .`!@:  '!
 * &$%||||!'                     '|
 * %%%%%|'              .````''':;!
 * $$$%%:                .`:;;!!!!
 * $%%|:.         ...  ..````';|
 * $%$%:.``... ...``````..```:|
 * $$%|;'::'``'''':::::'````:|
 * $$$&$|!!;:;;:::;!!!;:'`';|
 * $$$$$$%|!|!;;!!|$&$!:::;|
 * &&$$%%$&%|!;;%&$&$|;::|
 *
 *
 * 顶级Service接口，声明Service-Method并实现，无需编写Mapper和Service，注入即可获得CRUD功能
 *
 * @author duxiaoyu
 * @date 2019-12-12
 */
public interface BaseService {

    /**
     * 查询实例
     *
     * @param queryWrapper 查询条件构造器
     * @param clazz        实例class
     * @param <T>
     * @return 指定类型实例
     */
    <T> T queryObject(QueryWrapper queryWrapper, Class<T> clazz);

    /**
     * 查询实例集合
     *
     * @param queryWrapper 查询条件构造器
     * @param clazz        实例class
     * @param <T>
     * @return 指定类型集合实例
     */
    <T> List<T> queryObjectList(QueryWrapper queryWrapper, Class<T> clazz);

    /**
     * 按实例主键查询
     *
     * @param id    主键
     * @param clazz 实例class
     * @param <T>
     * @return 指定类型实例
     */
    <T> T queryObjectById(Object id, Class<T> clazz);

    /**
     * 自定义sql查询
     *
     * @param sql 自定义sql
     * @return
     */
    List<Map<String, Object>> queryCustomSql(String sql);

    /**
     * 新增实例
     * explain: 新增成功后，返回指定的主键(主键的指定在@Id注解中)
     *
     * @param obj bean
     * @return 主键
     */
    String addObject(Object obj);

    /**
     * 批量新增实例
     *
     * @param objectList 实例集合
     * @param <E>        实例类型
     * @return 受影响行数
     */
    <E> Long addBatchObject(List<E> objectList);

    /**
     * 修改实例
     * 目前根据主键进行修改
     *
     * @param obj bean
     * @return 受影响行数
     */
    Long updateObject(Object obj);

    /**
     * 批量修改实例
     *
     * @param objectList 实例集合
     * @param <E>        实例类型
     * @return 受影响行数
     */
    <E> Long updateBatchByIdObject(List<E> objectList);

    /**
     * 删除实例
     *
     * @param objectId 主键
     * @param clazz    实例类型
     * @return 受影响行数
     */
    Long deleteObject(Object objectId, Class<?> clazz);

    /**
     * 批量删除实例
     *
     * @param objectIdList 主键集合
     * @param clazz        实例类型
     * @return 受影响行数
     */
    Long deleteBatchByIdObject(List<Object> objectIdList, Class<?> clazz);
}
