package com.rabbit.core.constructor;

import com.rabbit.core.bean.TableInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;

/**
 * add constructor
 *
 * @param <E>
 * @author duxiaoyu
 * @since 2019-12-12
 */
public class InsertWrapper<E extends Serializable> extends BaseAbstractWrapper<E> implements Serializable {

    private static final Logger LOGGER= LoggerFactory.getLogger(InsertWrapper.class);

    /**
     * 解析后的TableInfo
     */
    private TableInfo tableInfo;

    public InsertWrapper(Class<E> clazz){
         super(clazz);
         this.tableInfo=analysisClazz();
    }

}
