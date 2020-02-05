package com.rabbit.core.constructor;

import java.io.Serializable;

/**
 * 查询条件构造器
 *
 * @param <E>
 * @author duxiaoyu
 * @since 2019-12-12
 */
public class QueryWrapper<E> extends BaseAbstractWrapper<E> implements Serializable {

    /*
    *
    * QueryWrapper 需要构建详细条件拼接生成的设计思路
    * TODO 构建思路待实现...
    *
    *
    *
    * */

    public QueryWrapper(E clazz){
        super(clazz);
    }
}
