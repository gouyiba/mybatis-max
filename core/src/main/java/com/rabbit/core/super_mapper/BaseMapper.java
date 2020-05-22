package com.rabbit.core.super_mapper;

/**
 * 公用Mappeer，用于其他Dao接口继承，继承该接口后，即可拥有CRUD功能
 *
 * @param <T> 泛型
 * @author duxiaoyu
 * @since 2019-12-12
 */
public interface BaseMapper<T> {
    int insert(T entity);
}
