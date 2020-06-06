package com.rabbit.core.mapper;

import com.rabbit.core.constructor.DeleteWrapper;
import com.rabbit.core.constructor.QueryWrapper;
import com.rabbit.core.constructor.UpdateWrapper;
import org.apache.ibatis.annotations.Param;

import java.util.Collection;
import java.util.List;

/**
 * 公用Mapper，用于其他Dao接口继承，继承该接口后，即可拥有CRUD功能
 * 半自动化SQL，与Mapper强耦合
 *
 * @param <T> 实体
 * @author duxiaoyu
 * @since 2019-12-12
 */
public interface BaseMapper<T> extends BusinessMapper {
    /**
     * 插入一条记录
     *
     * @param entity 实体对象
     * @return 受影响行数
     */
    int insert(T entity);

    /**
     * 批量插入记录
     *
     * @param entityList 实体集合
     * @return 受影响行数
     */
    int insertBatch(@Param("entityList") List<T> entityList);

    /**
     * 根据 ID 删除
     *
     * @param id 主键ID
     * @return 受影响行数
     */
    int deleteById(Object id);

    /**
     * 根据 DeleteWrapper 条件，删除记录
     *
     * @param wrapper 实体对象封装操作类（可以为 null）
     * @return 受影响行数
     */
    int delete(DeleteWrapper<T> wrapper);

    /**
     * 删除（根据ID 批量删除）
     *
     * @param idList 主键ID列表(不能为 null 以及 empty)
     * @return 受影响行数
     */
    int deleteBatchById(@Param("idList") List<Object> idList);

    /**
     * 根据 ID 修改
     *
     * @param entity 实体对象
     * @return 受影响行数
     */
    int updateById(T entity);

    /**
     * 根据 ID 批量修改
     *
     * @param entityIds 实体集合
     * @return 受影响行数
     */
    int updateBatchById(@Param("entityIds") List<T> entityIds);

    /**
     * 根据 updateWrapper 条件，更新记录
     *
     * @param updateWrapper 条件封装操作类（可以为 null,用于生成 where 语句）
     * @return 受影响行数
     */
    int update(UpdateWrapper<T> updateWrapper);

    /**
     * 根据 ID 查询
     *
     * @param id 主键ID
     * @return 实体类型
     */
    T selectById(Object id);

    /**
     * 查询（根据ID 批量查询）
     *
     * @param idList 主键ID列表(不能为 null 以及 empty)
     * @return 实体集合
     */
    List<T> selectBatchIds(@Param("idList") Collection idList);

    /**
     * 根据 QueryWrapper 条件，查询一条记录
     *
     * @param queryWrapper 实体对象封装操作类（可以为 null）
     * @return 实体类型
     */
    T selectOne(@Param("queryWrapper") QueryWrapper<T> queryWrapper);

    /**
     * 根据 QueryWrapper 条件，查询总记录数
     *
     * @param queryWrapper 实体对象封装操作类（可以为 null
     * @return 总记录数
     */
    Integer selectCount(@Param("queryWrapper") QueryWrapper<T> queryWrapper);

    /**
     * 根据 QueryWrapper 条件，查询记录
     *
     * @param queryWrapper 实体对象封装操作类（可以为 null
     * @return 实体集合
     */
    List<T> selectList(@Param("queryWrapper") QueryWrapper<T> queryWrapper);

    /* 2.0.0 实现 */
    /**
     * 根据 QueryWrapper 条件，查询记录
     *
     * @param queryWrapper 实体对象封装操作类（可以为 null）
     * @return Map集合
     */
    // List<Map<String, Object>> selectMaps(QueryWrapper<T> queryWrapper);

    /**
     * 根据自定义Sql查询记录
     *
     * @param sql 自定义Sql
     * @return Map集合
     */
    // List<Map<String, Object>> selectCustomSql(String sql);

    /**
     * 根据 columnMap 条件，删除记录
     *
     * @param columnMap 表字段 map 对象
     * @return 受影响行数
     */
    // int deleteByMap(Map<String, Object> columnMap);

    /**
     * 根据 updateWrapper 条件，批量更新记录
     *
     * @param entityList    实体对象集合 (set 更新值,可以为 null)
     * @param updateWrapper 条件封装操作类（可以为 null,用于生成 where 语句）
     * @return 受影响行数
     */
    // int updateBatch(List<T> entityList, UpdateWrapper<T> updateWrapper);

    /**
     * 查询（根据 columnMap 条件）
     *
     * @param columnMap 表字段 map 对象
     * @return 实体集合
     */
    // List<T> selectByMap(Map<String, Object> columnMap);
}
