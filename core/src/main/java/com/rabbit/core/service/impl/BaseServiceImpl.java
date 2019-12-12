package com.rabbit.core.service.impl;

import com.rabbit.core.enumation.MySqlKeyWord;
import com.rabbit.core.mapper.BaseDao;
import com.rabbit.core.service.BaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 公用Service-Method-Impl
 *
 * @author duxiaoyu
 * @since 2019-12-12
 */
@Service("baseServiceImpl")
public class BaseServiceImpl implements BaseService {

    @Autowired
    private BaseDao baseDao;

}
