package com.rabbit.core.mapper;

/**
 * 公用Mapper，用于其他Dao接口继承，继承该接口后，即可拥有CRUD功能
 * 半自动化SQL，与Mapper强耦合
 *
 * @param <T> 实体
 * @author duxiaoyu
 * @since 2019-12-12
 */
public interface BaseMapper<T> extends BusinessMapper {
}
