package com.rabbit.mapper;

import com.rabbit.core.mapper.BaseMapper;
import com.rabbit.entity.User;

/**
 * this class by created wuyongfei on 2020/5/10 13:50
 **/
public interface AccountMapper extends BaseMapper {
    User findById(String id);
}
