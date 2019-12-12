package com.rabbit.core.constructor;

import com.rabbit.core.bean.TableInfo;
import com.rabbit.utils.ClassUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 抽象基类包装器
 *
 * @param <E>
 * @author duxiaoyu
 * @since 2019-12-12
 */
public abstract class BaseAbstractWrapper<E extends Serializable> implements Serializable {


    private static final Logger logger = LoggerFactory.getLogger(BaseAbstractWrapper.class);

    /**
     * 缓存 TableInfo 数据，作为全局缓存
     */
    private static final Map<Class<?>, TableInfo> TABLE_INFO_CACHE = new ConcurrentHashMap<>();


    /***************************************** TableInfo 缓存 ***********************************************/
    /**
     * 添加 TableInfo 缓存
     *
     * @param clazz     bean.class
     * @param tableInfo After initialization tableInfo
     */
    public void addTableInfoCache(Class<?> clazz, TableInfo tableInfo) {
        if(clazz!=null&& !Objects.isNull(tableInfo)){
           TABLE_INFO_CACHE.put(ClassUtils.getUserClass(clazz),tableInfo);
        }
    }

    /**
     * 获取缓存的 TableInfo
     *
     * @param clazz bean.class
     * @return TableInfo
     */
    public TableInfo getTableInfo(Class<?> clazz) {
        return TABLE_INFO_CACHE.get(ClassUtils.getUserClass(clazz));
    }

    /**
     * 获取所有缓存的TableInfo
     *
     * @return Map<Class<?>,TableInfo>
     */
    public Map<Class<?>, TableInfo> getAllTableInfo() {
        return TABLE_INFO_CACHE;
    }

    /**
     * 删除 TableInfo 缓存
     * @param clazz bean.class
     */
    public void removeTableInfoCache(Class<?> clazz){
        TABLE_INFO_CACHE.remove(ClassUtils.getUserClass(clazz));
    }

    /**
     * 清空 TableInfo 缓存
     */
    public void clearTableInfoCache(){
        TABLE_INFO_CACHE.clear();
    }

    /***************************************** TableInfo 缓存 ***********************************************/



    /********************************************** 定义其他Wrapper公有的实现或抽象Method *********************************************************/

    //TODO 待实现 ...


}
