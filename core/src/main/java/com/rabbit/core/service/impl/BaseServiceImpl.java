package com.rabbit.core.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.lang.Snowflake;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.json.JSONUtil;
import com.rabbit.core.annotation.Id;
import com.rabbit.core.bean.TableInfo;
import com.rabbit.core.bean.User;
import com.rabbit.core.constructor.BaseAbstractWrapper;
import com.rabbit.core.constructor.InsertWrapper;
import com.rabbit.core.enumation.MySqlKeyWord;
import com.rabbit.core.enumation.PrimaryKey;
import com.rabbit.core.enumation.Sex;
import com.rabbit.core.mapper.BaseDao;
import com.rabbit.core.service.BaseService;
import com.rabbit.exception.MyBatisRabbitPlugException;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.lang.reflect.Field;
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

    @Autowired
    private BaseDao baseDao;

    /**
     * 新增实例
     * explain: 新增成功后，返回指定的主键(主键的指定在@Id注解中)
     * @param obj 实例
     * @return 主键
     */
    @Override
    public String addObject(Object obj) {
        if(Objects.isNull(obj)){
            throw new MyBatisRabbitPlugException("bean为空......");
        }
        InsertWrapper insertWrapper=new InsertWrapper(obj);
        Map<String,String> sqlMap=insertWrapper.sqlGenerate(obj);
        TableInfo tableInfo=getTableInfo(obj.getClass());
        Field primaryKey=tableInfo.getPrimaryKey();
        if(Objects.isNull(primaryKey)){
            throw new MyBatisRabbitPlugException("解析时未获取到主键字段......");
        }
        Map<String,Object> beanMap= BeanUtil.beanToMap(obj);
        // 获取主键
        Id id=primaryKey.getAnnotation(Id.class);
        PrimaryKey pkEnum=id.generateType();

        if(MapUtil.isEmpty(beanMap)){
            throw new MyBatisRabbitPlugException("beanMap转换失败......");
        }
        // 判断是否已经设置主键
        if(beanMap.containsKey(primaryKey.getName())){
            if(Objects.isNull(beanMap.get(primaryKey.getName()))){
                // 按照主键策略设置主键
                switch (pkEnum){
                    case UUID32:
                        beanMap.put(primaryKey.getName(), IdUtil.simpleUUID());
                        break;
                    case OBJECTID:
                        beanMap.put(primaryKey.getName(),IdUtil.objectId());
                        break;
                    case SNOWFLAKE:
                        // 根据雪花算法生成64bit大小的分布式Long类型id，需要在 @id 中设置workerId和datacenterId
                        Snowflake snowflake=IdUtil.getSnowflake(id.workerId(),id.datacenterId());
                        beanMap.put(primaryKey.getName(),snowflake.nextId());
                        break;
                }
            }
        }else {
            // 如果没有指定主键，默认主键是id，也有可能主键是其他字段，但是没有指定
            // 如果主键是id那么类型就是Integer，从1开始依次递增，每次递增前会查询表中最后一条记录的id，并加1
            // TODO 待实现，以上只是暂时思考
        }
        System.out.println("-------------------------------------");
        System.out.println(JSONUtil.toJsonStr(beanMap));
        System.out.println(JSONUtil.toJsonStr(sqlMap));
        //baseDao.addObject(beanMap,sqlMap);
        return beanMap.get(primaryKey.getName()).toString();
    }

    public static void main(String[] args) {
        BaseService baseService=new BaseServiceImpl();
        User user=new User();
        user.setStuAge(23);
        user.setStuName("杜晓宇");
        user.setSex(Sex.MAN);
        System.out.println(baseService.addObject(user));
    }
}
