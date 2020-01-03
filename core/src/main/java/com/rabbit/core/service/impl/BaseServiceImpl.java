package com.rabbit.core.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.lang.Snowflake;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.json.JSONUtil;
import com.rabbit.core.annotation.Create;
import com.rabbit.core.bean.TableInfo;
import com.rabbit.core.enumation.PrimaryKey;
import com.rabbit.core.mapper.BusinessMapper;
import com.rabbit.core.service.BaseService;
import com.rabbit.core.annotation.FillingStrategy;
import com.rabbit.core.annotation.Id;
import com.rabbit.core.constructor.BaseAbstractWrapper;
import com.rabbit.core.constructor.InsertWrapper;
import com.rabbit.common.exception.MyBatisRabbitPlugException;
import com.rabbit.common.utils.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * 公用Service-Method-Impl
 *
 * @author duxiaoyu
 * @since 2019-12-12
 */
@Service("baseServiceImpl")
public class BaseServiceImpl extends BaseAbstractWrapper implements BaseService {

    private static final Logger LOGGER= LoggerFactory.getLogger(BaseServiceImpl.class);

    @Autowired
    private ApplicationContext applicationContext;

    @Autowired
    private BusinessMapper businessMapper;

    /**
     * 新增实例
     * explain: 新增成功后，返回指定的主键(主键的指定在@Id注解中)
     *
     * @param obj 实例
     * @return 主键
     */
    @Override
    public String addObject(Object obj) {
        if (Objects.isNull(obj)) {
            throw new MyBatisRabbitPlugException("bean为空......");
        }
        InsertWrapper insertWrapper = new InsertWrapper(obj);
        Map<String, String> sqlMap = insertWrapper.sqlGenerate(obj);
        TableInfo tableInfo = getTableInfo(obj.getClass());
        Field primaryKey = tableInfo.getPrimaryKey();
        if (Objects.isNull(primaryKey)) {
            throw new MyBatisRabbitPlugException("解析时未获取到主键字段......");
        }
        Map<String, Object> beanMap = BeanUtil.beanToMap(obj);
        // 获取主键
        Id id = primaryKey.getAnnotation(Id.class);
        PrimaryKey pkEnum = id.generateType();

        // 指定临时主键名称 -> 获取新增成功后返回的表默认主键值
        beanMap.put("tempPrimKey","");

        if (MapUtil.isEmpty(beanMap)) {
            throw new MyBatisRabbitPlugException("beanMap转换失败......");
        }
        // 判断是否指定主键
        if (beanMap.containsKey(primaryKey.getName())) {
            // 如果设置了主键的value，则此处不再进行主键设置
            if (Objects.isNull(beanMap.get(primaryKey.getName()))) {
                // 根据主键策略设置主键
                switch (pkEnum) {
                    case UUID32:
                        beanMap.put(primaryKey.getName(), IdUtil.simpleUUID());
                        break;
                    case OBJECTID:
                        beanMap.put(primaryKey.getName(), IdUtil.objectId());
                        break;
                    case SNOWFLAKE:
                        // 根据雪花算法生成64bit大小的分布式Long类型id，需要在 @id 中设置workerId和datacenterId
                        Snowflake snowflake = IdUtil.getSnowflake(id.workerId(), id.datacenterId());
                        beanMap.put(primaryKey.getName(), snowflake.nextId());
                        break;
                }
            }
        }
        // 自定义字段填充策略
        Class<?> clazz = this.getFillingStrategyClass();
        try {
            this.setFillingStrategyContent(beanMap, clazz);
        }catch (Exception e){
            e.printStackTrace();
        }
        LOGGER.info("{}:{}",TAG, JSONUtil.toJsonStr(sqlMap));
        LOGGER.info("{}:{}",TAG, JSONUtil.toJsonStr(beanMap));
        businessMapper.addObject(beanMap,sqlMap);
        return beanMap.get("tempPrimKey").toString();
    }

    /**
     * 获取自定义字段填充Class
     *
     * @return
     */
    public Class<?> getFillingStrategyClass() {
        List<String> classNameList = Arrays.asList(applicationContext.getBeanDefinitionNames());
        for (String item : classNameList) {
            Object obj = applicationContext.getBean(item);
            if (obj.getClass().isAnnotationPresent(FillingStrategy.class)) {
                return obj.getClass();
            }
        }
        return null;
    }

    /**
     * 设置自定义字段内容
     *
     * @param beanMap 实例
     */
    public void setFillingStrategyContent(Map<String, Object> beanMap, Class<?> clazz) throws Exception {
        if (!Objects.isNull(clazz)) {
            List<Method> methodList = Arrays.asList(clazz.getMethods());
            Method addMethod = methodList.stream().filter(x -> x.isAnnotationPresent(Create.class)).findAny().orElseThrow(()->
                    new MyBatisRabbitPlugException("未找到新增时的自定义字段填充Method......"));
            Object obj = clazz.newInstance();
            addMethod.invoke(obj);
            List<Field> fieldList = Arrays.asList(clazz.getDeclaredFields());
            for(Field item:fieldList){
                Method getMethod = clazz.getMethod("get" + StringUtils.capitalize(item.getName()));
                Object val = getMethod.invoke(obj);
                beanMap.put(item.getName(), val);
            }
        }
    }

}
