package com.rabbit.core.mapper;


import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 公用Dao接口，用于BaseService
 *
 * @author duxiaoyu
 * @since 2019-12-12
 */
public interface BusinessMapper {

    /**
     * 新增实例
     * @param objectMap 实例
     * @param sqlMap sqlMap
     * @return
     */
    Long addObject(@Param("objectMap") Map<String,Object> objectMap, @Param("sqlMap") Map<String,String> sqlMap);

    /**
     * 批量新增实例
     * @param objectList 实例集合
     * @param sqlMap sqlMap
     * @return
     */
    Long addBatchObject(@Param("objectList") List<Map<String, Object>> objectList , @Param("sqlMap") Map<String,String> sqlMap);

    /**
     * 修改实例
     * @param objectMap 实例
     * @param sqlMap sqlMap
     * @return
     */
    Long updateObject(@Param("objectMap") Map<String,Object> objectMap, @Param("sqlMap") Map<String,Object> sqlMap);

    /**
     * 批量修改实例
     * @param objectList 实例集合
     * @param sqlMap sqlMap
     * @return
     */
    Long updateBatchByIdObject(@Param("objectList") List<Map<String,Object>> objectList,@Param("sqlMap") Map<String,Object> sqlMap);

    /**
     * 删除实例
     * @param objectId 主键
     * @param sqlMap sqlMap
     * @return
     */
    Long deleteObject(@Param("objectId") Object objectId,@Param("sqlMap") Map<String,String> sqlMap);

    /**
     * 批量删除实例
     * @param objectList
     * @param sqlMap
     * @return
     */
    Long deleteBatchByIdObject(@Param("objectList") List<Object> objectList,@Param("sqlMap") Map<String,String> sqlMap);

}
