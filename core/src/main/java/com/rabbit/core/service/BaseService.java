package com.rabbit.core.service;

import java.util.Map;

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
     * @param obj bean
     * @return 主键
     */
    String addObject(Object obj);
}
