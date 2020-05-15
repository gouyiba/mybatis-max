package com.rabbit.core.mapper;

import com.rabbit.core.entity.User;
import com.rabbit.core.super_mapper.BaseMapper;

/**
 * this class by created wuyongfei on 2020/5/10 13:50
 **/
public interface AccountMapper extends BaseMapper {
    User findById(String id);
}
