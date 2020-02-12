package com.rabbit.core.enumation;

import java.io.Serializable;

/**
 * 自定义枚举接口
 *
 * @param <T>
 * @author duxiaoyu
 * @since 2019-12-12
 */
public interface IEnum<T extends Serializable> {

    /**
     * 枚举对应数据库的存储值
     *
     * @return T
     */
    T getValue();

    /**
     * value转enum
     * @param val
     * @return
     */
    /*default Enum valueConvertEnum(T val,Enum eEnum){
        // TODD 待实现
        return null;
    } */
}
