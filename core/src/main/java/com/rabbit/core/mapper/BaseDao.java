package com.rabbit.core.mapper;

import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;

import java.util.Map;

/**
 * 公用Dao接口，用于BaseService
 *
 * @author duxiaoyu
 * @since 2019-12-12
 */
public interface BaseDao {

    /**
     * 新增实例
     * explain: 新增成功后，返回指定的主键(主键的指定在@Id注解中)
     * @param objectMap 实例参数
     * @return 主键
     */
    String addObject(@Param("objectMap") Map<String,Object> objectMap, @Param("sqlMap") Map<String,String> sqlMap);

}
