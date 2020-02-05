package com.rabbit.core.service;

import java.util.List;

/**
 * 公用Service接口，声明基础Service-Method
 *
 * @author duxiaoyu
 * @since 2019-12-12
 */
public interface BaseService {

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
}
