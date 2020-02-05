package com.rabbit.core.constructor;

import java.io.Serializable;

/**
 * 删除条件构造器
 *
 * @param <E>
 * @author duxiaoyu
 * @since 2019-12-12
 */
public class DeleteWrapper<E> extends QueryWrapper<E> implements Serializable {

    /*
     *
     * UpdateWrapper继承自QueryWrapper，所以拥有QueryWrapper的条件拼接生成能力
     * TODO 构建思路待实现...
     *
     *
     *
     * */

    public DeleteWrapper(E clazz){
        super(clazz);
    }
}
