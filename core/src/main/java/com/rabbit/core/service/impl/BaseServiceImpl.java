package com.rabbit.core.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.lang.Snowflake;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.json.JSONUtil;
import com.rabbit.common.utils.CollectionUtils;
import com.rabbit.core.annotation.Create;
import com.rabbit.core.annotation.Update;
import com.rabbit.core.bean.TableFieldInfo;
import com.rabbit.core.bean.TableInfo;
import com.rabbit.core.constructor.*;
import com.rabbit.core.enumation.MySqlKeyWord;
import com.rabbit.core.enumation.PrimaryKey;
import com.rabbit.core.enumation.SqlKey;
import com.rabbit.core.mapper.BusinessMapper;
import com.rabbit.core.service.BaseService;
import com.rabbit.core.annotation.FillingStrategy;
import com.rabbit.core.annotation.Id;
import com.rabbit.common.exception.MyBatisRabbitPlugException;
import com.rabbit.common.utils.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 公用Service-Method-Impl
 *
 * @author duxiaoyu
 * @since 2019-12-12
 */
@Service("baseServiceImpl")
public class BaseServiceImpl extends BaseAbstractWrapper implements BaseService {

    private static final Logger LOGGER = LoggerFactory.getLogger(BaseServiceImpl.class);

    @Autowired
    private ApplicationContext applicationContext;

    @Autowired
    private BusinessMapper businessMapper;


    /**
     * 查询实例
     *
     * @param queryWrapper 查询条件构造器
     * @param clazz        实例class
     * @param <T>          类型
     * @return 类型实例
     */
    @Override
    public <T> T queryObject(QueryWrapper queryWrapper, Class<T> clazz) {
        if (Objects.isNull(clazz)) {
            throw new MyBatisRabbitPlugException("queryObject -> clazz is null......");
        }
        copyQueryWrapper(queryWrapper, clazz);
        Map<String, Object> sqlMap = queryWrapper.mergeSqlMap();
        Map<String, Object> objMap = businessMapper.getObject(sqlMap, (Map<String, Object>) sqlMap.get("VALUE"));
        TableInfo tableInfo = getTableInfo(clazz);
        convertEnumVal(tableInfo.getColumnMap(), Arrays.asList(objMap));
        if (CollectionUtils.isEmpty(objMap)) return null;
        T bean = BeanUtil.mapToBean(objMap, clazz, true);
        return bean;
    }

    /**
     * 查询实例集合
     *
     * @param queryWrapper 查询条件构造器
     * @param clazz        实例class
     * @param <T>          类型
     * @return 类型集合实例
     */
    @Override
    public <T> List<T> queryObjectList(QueryWrapper queryWrapper, Class<T> clazz) {
        if (Objects.isNull(clazz)) {
            throw new MyBatisRabbitPlugException("queryObjectList -> clazz is null......");
        }
        copyQueryWrapper(queryWrapper, clazz);
        Map<String, Object> sqlMap = queryWrapper.mergeSqlMap();
        List<Map<String, Object>> objMapList = businessMapper.getObjectList(sqlMap, (Map<String, Object>) sqlMap.get("VALUE"));
        if (CollectionUtils.isEmpty(objMapList)) return Collections.emptyList();

        List<T> beanList = new ArrayList<>();
        TableInfo tableInfo = getTableInfo(clazz);
        convertEnumVal(tableInfo.getColumnMap(), objMapList);
        for (Map<String, Object> item : objMapList) {
            T bean = BeanUtil.mapToBean(item, clazz, true);
            if (!Objects.isNull(bean)) beanList.add(bean);
        }
        return beanList;
    }

    /**
     * 按实例主键查询
     *
     * @param Id    主键
     * @param clazz 实例class
     * @param <T>
     * @return 指定类型实例
     */
    @Override
    public <T> T queryObjectById(Object Id, Class<T> clazz) {
        if (Objects.isNull(clazz)) {
            throw new MyBatisRabbitPlugException("queryObjectById -> clazz is null......");
        }
        Object obj = null;
        try {
            obj = clazz.newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        QueryWrapper queryWrapper = new QueryWrapper(obj);
        TableInfo tableInfo = getTableInfo(clazz);
        Field pkField = tableInfo.getPrimaryKey();
        TableFieldInfo tableFieldInfo = tableInfo.getColumnMap().get(pkField.getName());
        queryWrapper.where(tableFieldInfo.getColumnName(), Id);

        Map<String, Object> sqlMap = queryWrapper.mergeSqlMap();
        Map<String, Object> objMap = businessMapper.getObject(sqlMap, (Map<String, Object>) sqlMap.get("VALUE"));

        convertEnumVal(tableInfo.getColumnMap(), Arrays.asList(objMap));
        if (CollectionUtils.isEmpty(objMap)) return null;
        T bean = BeanUtil.mapToBean(objMap, clazz, true);
        return bean;
    }

    /**
     * 自定义sql查询
     *
     * @param sql 自定义sql
     * @return 指定类型实例集合
     */
    @Override
    public List<Map<String, Object>> queryCustomSql(String sql) {
        if (org.apache.commons.lang3.StringUtils.isBlank(sql)) {
            throw new MyBatisRabbitPlugException("queryCustomSql -> sql is null......");
        }
        List<Map<String, Object>> objMapList = businessMapper.customSqlObject(sql);
        if (CollectionUtils.isEmpty(objMapList)) return Collections.emptyList();
        return objMapList;
    }

    /**
     * 删除实例
     *
     * @param objectId 主键
     * @param clazz    实例类型
     * @return 受影响行数
     */
    @Override
    public Long deleteObject(Object objectId, Class<?> clazz) {
        if (Objects.isNull(objectId)) {
            throw new MyBatisRabbitPlugException("主键Id为空......");
        }
        Object obj = null;
        try {
            obj = clazz.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }
        DeleteWrapper deleteWrapper = new DeleteWrapper(obj);
        Map<String, String> sqlMap = deleteWrapper.sqlGenerate();
        LOGGER.info(" ");
        LOGGER.info("{}: begin delete data...", TAG);
        LOGGER.info("{}: sqlMap ==>{}", TAG, JSONUtil.toJsonStr(sqlMap));
        LOGGER.info("{}: objectId ==>{}", TAG, objectId);
        long result = businessMapper.deleteObject(objectId, sqlMap);
        LOGGER.info("{}: end delete data...", TAG);
        LOGGER.info(" ");
        return result;
    }

    /**
     * 批量删除实例
     *
     * @param objectIdList 主键集合
     * @param clazz        实例类型
     * @return 受影响行数
     */
    @Override
    public Long deleteBatchByIdObject(List<Object> objectIdList, Class<?> clazz) {
        if (CollectionUtils.isEmpty(objectIdList)) {
            throw new MyBatisRabbitPlugException("主键集合为空......");
        }
        /*********************************** 记录批量操作开始时间 ***************************************/
        long beginTime = System.currentTimeMillis();
        Object obj = null;
        try {
            obj = clazz.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }

        DeleteWrapper deleteWrapper = new DeleteWrapper(obj);
        Map<String, String> sqlMap = deleteWrapper.sqlGenerate();
        // 修改sql格式
        String where = sqlMap.get(SqlKey.DELETE_WHERE.getValue());
        where = where.substring(0, where.indexOf("=")) + " " + MySqlKeyWord.IN.getValue();
        sqlMap.put(SqlKey.DELETE_WHERE.getValue(), where);

        // 开始批量删除
        LOGGER.info(" ");
        LOGGER.info("{}: begin deleteBatch data...", TAG);
        LOGGER.info("{}: sqlMap ==>{}", TAG, JSONUtil.toJsonStr(sqlMap));
        LOGGER.info("{}: objectIdList ==>{}", TAG, JSONUtil.toJsonStr(objectIdList));
        long result = 0;
        // 批量操作限制: 如果总记录数大于500条，则分批执行，每批执行500条记录
        if (objectIdList.size() > 500) {
            int currentBatch = 1;
            int total = objectIdList.size();
            int batch = total % 500 == 0 ? (total / 500) : (total / 500) + 1;
            int limit = (currentBatch - 1) * 500;
            List<Object> tempList = new LinkedList<>();
            for (int x = 1; x <= batch; x++) {
                if ((limit + 500) > total) {
                    tempList = objectIdList.subList(limit, objectIdList.size());
                } else {
                    tempList = objectIdList.subList(limit, x * 500);
                }
                result += businessMapper.deleteBatchByIdObject(tempList, sqlMap);
                currentBatch++;
                limit = (currentBatch - 1) * 500;
            }
        } else {
            result = businessMapper.deleteBatchByIdObject(objectIdList, sqlMap);
        }
        long endTime = System.currentTimeMillis();
        LOGGER.info("{}: success total: {}", TAG, result);
        LOGGER.info("{}: 本次批量删除共耗时: {}s", TAG, (endTime - beginTime) / 1000f);
        LOGGER.info("{}: end deleteBatch data...", TAG);
        LOGGER.info(" ");
        return result;
    }

    /**
     * 修改实例
     * 目前根据主键进行修改
     *
     * @param obj bean
     * @return 受影响行数
     */
    @Override
    public Long updateObject(Object obj) {
        if (Objects.isNull(obj)) {
            throw new MyBatisRabbitPlugException("bean为空......");
        }
        UpdateWrapper updateWrapper = new UpdateWrapper(obj);
        Map<String, Object> sqlMap = updateWrapper.sqlGenerate();
        TableInfo tableInfo = getTableInfo(obj.getClass());
        Field pk = tableInfo.getPrimaryKey();
        Map<String, Object> beanMap = BeanUtil.beanToMap(obj);
        if (Objects.isNull(beanMap.get(pk.getName()))) {
            throw new MyBatisRabbitPlugException("要修改的bean主键为空......");
        }

        // 自定义字段填充策略
        Class<?> clazz = this.getFillingStrategyClass();
        try {
            this.setFillingStrategyContent(beanMap, clazz, Update.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        LOGGER.info(" ");
        LOGGER.info("{}: begin update data...", TAG);
        LOGGER.info("{}: sqlMap ==>{}", TAG, JSONUtil.toJsonStr(sqlMap));
        LOGGER.info("{}: beanMap ==>{}", TAG, JSONUtil.toJsonStr(beanMap));
        long result = businessMapper.updateObject(beanMap, sqlMap);
        LOGGER.info("{}: end update data...", TAG);
        LOGGER.info(" ");
        return result;
    }

    /**
     * 批量修改实例
     * 目前根据主键进行修改
     *
     * @param objectList 实例集合
     * @param <E>        实例类型
     * @return 受影响行数
     */
    @Override
    public <E> Long updateBatchByIdObject(List<E> objectList) {
        if (CollectionUtils.isEmpty(objectList)) {
            throw new MyBatisRabbitPlugException("修改实例集合为空......");
        }
        /*********************************** 记录批量操作开始时间 ***************************************/
        long beginTime = System.currentTimeMillis();
        UpdateWrapper updateWrapper = new UpdateWrapper(objectList.get(0));
        TableInfo tableInfo = getTableInfo(objectList.get(0).getClass());
        List<Map<String, Object>> objMapList = objectList.stream().map(x -> BeanUtil.beanToMap(x)).collect(Collectors.toList());
        Map<String, Object> sqlMap = updateWrapper.sqlGenerate();
        String where = sqlMap.get(SqlKey.UPDATE_WHERE.getValue()).toString();
        where = where.replace("objectMap", "obj");
        sqlMap.put(SqlKey.UPDATE_WHERE.getValue(), where);
        // 批量修改目标实例参数
        Map<String, String> paramterMap = (Map<String, String>) sqlMap.get(SqlKey.UPDATE_VALUE.getValue());
        for (String item : paramterMap.keySet()) {
            paramterMap.put(item, paramterMap.get(item).replace("objectMap", "obj"));
        }
        // 自定义字段批量填充
        Class<?> clazz = this.getFillingStrategyClass();
        try {
            for (Map<String, Object> item : objMapList) {
                this.setFillingStrategyContent(item, clazz, Update.class);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        // 开始批量修改
        LOGGER.info(" ");
        LOGGER.info("{}: begin updateBatch data...", TAG);
        LOGGER.info("{}: sqlMap ==>{}", TAG, JSONUtil.toJsonStr(sqlMap));
        LOGGER.info("{}: objMap ==>{}", TAG, JSONUtil.toJsonStr(objMapList));
        long result = 0;
        // 批量操作限制: 如果总记录数大于500条，则分批执行，每批执行500条记录
        if (objMapList.size() > 500) {
            int currentBatch = 1;
            int total = objMapList.size();
            int batch = total % 500 == 0 ? (total / 500) : (total / 500) + 1;
            int limit = (currentBatch - 1) * 500;
            List<Map<String, Object>> tempList = new LinkedList<>();
            for (int x = 1; x <= batch; x++) {
                if ((limit + 500) > total) {
                    tempList = objMapList.subList(limit, objMapList.size());
                } else {
                    tempList = objMapList.subList(limit, x * 500);
                }
                result += businessMapper.updateBatchByIdObject(tempList, sqlMap);
                currentBatch++;
                limit = (currentBatch - 1) * 500;
            }
        } else {
            result = businessMapper.updateBatchByIdObject(objMapList, sqlMap);
        }
        long endTime = System.currentTimeMillis();
        LOGGER.info("{}: success total: {}", TAG, result);
        LOGGER.info("{}: 本次批量修改共耗时: {}s", TAG, (endTime - beginTime) / 1000f);
        LOGGER.info("{}: end updateBatch data...", TAG);
        LOGGER.info(" ");
        return result;
    }

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
        String pk = null;
        InsertWrapper insertWrapper = new InsertWrapper(obj);
        Map<String, String> sqlMap = insertWrapper.sqlGenerate();
        TableInfo tableInfo = getTableInfo(obj.getClass());
        Field primaryKey = tableInfo.getPrimaryKey();
        if (Objects.isNull(primaryKey)) {
            throw new MyBatisRabbitPlugException("解析时未获取到主键字段......");
        }
        Map<String, Object> beanMap = BeanUtil.beanToMap(obj);
        // 获取主键
        Id id = primaryKey.getAnnotation(Id.class);
        PrimaryKey pkEnum = id.generateType();
        // 如果主键是自增字段，默认返回自增的value
        if (id.isIncrementColumn()) {
            // 指定临时主键名称 -> 获取新增成功后返回的表默认主键值
            beanMap.put("tempPrimKey", "");
        }

        if (MapUtil.isEmpty(beanMap)) {
            throw new MyBatisRabbitPlugException("beanMap转换失败......");
        }
        // 判断是否指定主键
        if (beanMap.containsKey(primaryKey.getName()) && id.isIncrementColumn() == false) {
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
                // 策略主键
                pk = beanMap.get(primaryKey.getName()).toString();
            } else {
                // 自定义主键
                pk = beanMap.get(primaryKey.getName()).toString();
            }
        } else {
            // 默认表主键
            pk = "default-tab-pk";
        }
        // 自定义字段填充
        Class<?> clazz = this.getFillingStrategyClass();
        try {
            this.setFillingStrategyContent(beanMap, clazz, Create.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        LOGGER.info(" ");
        LOGGER.info("{}: begin add data...", TAG);
        LOGGER.info("{}: sqlMap ==>{}", TAG, JSONUtil.toJsonStr(sqlMap));
        LOGGER.info("{}: beanMap ==>{}", TAG, JSONUtil.toJsonStr(beanMap));
        businessMapper.addObject(beanMap, sqlMap);
        LOGGER.info("{}: end add data...", TAG);
        LOGGER.info(" ");
        if (org.apache.commons.lang3.StringUtils.equals("default-tab-pk", pk))
            pk = beanMap.get("tempPrimKey").toString();
        return pk;
    }


    /**
     * 批量新增实例
     *
     * @param objectList 实例集合
     * @param <E>        实例类型
     * @return 受影响行数
     */
    public <E> Long addBatchObject(List<E> objectList) {
        if (CollectionUtils.isEmpty(objectList)) {
            throw new MyBatisRabbitPlugException("新增实例集合为空......");
        }
        /*********************************** 记录批量操作开始时间 ***************************************/
        long beginTime = System.currentTimeMillis();
        InsertWrapper insertWrapper = new InsertWrapper(objectList.get(0));
        Map<String, String> sqlMap = insertWrapper.sqlGenerate();
        TableInfo tableInfo = getTableInfo(objectList.get(0).getClass());
        Field primaryKey = tableInfo.getPrimaryKey();
        if (Objects.isNull(primaryKey)) {
            throw new MyBatisRabbitPlugException("解析时未获取到主键字段......");
        }
        // 获取主键
        Id id = primaryKey.getAnnotation(Id.class);
        PrimaryKey pkEnum = id.generateType();
        List<Map<String, Object>> objMap = objectList.stream().map(x -> BeanUtil.beanToMap(x)).collect(Collectors.toList());
        if (CollectionUtils.isEmpty(objMap)) {
            throw new MyBatisRabbitPlugException("beanMap转换失败......");
        }

        // 批量设置主键
        for (Map<String, Object> item : objMap) {
            // 如果设置了主键的value，则此处不再进行主键设置
            if (Objects.isNull(item.get(primaryKey.getName())) && id.isIncrementColumn() == false) {
                // 根据主键策略设置主键
                switch (pkEnum) {
                    case UUID32:
                        item.put(primaryKey.getName(), IdUtil.simpleUUID());
                        break;
                    case OBJECTID:
                        item.put(primaryKey.getName(), IdUtil.objectId());
                        break;
                    case SNOWFLAKE:
                        // 根据雪花算法生成64bit大小的分布式Long类型id，需要在 @id 中设置workerId和datacenterId
                        Snowflake snowflake = IdUtil.getSnowflake(id.workerId(), id.datacenterId());
                        item.put(primaryKey.getName(), snowflake.nextId());
                        break;
                }
            }
        }

        // 批量字段填充
        Class<?> clazz = this.getFillingStrategyClass();
        for (Map<String, Object> item : objMap) {
            try {
                this.setFillingStrategyContent(item, clazz, Create.class);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        // 开始批量新增
        LOGGER.info(" ");
        LOGGER.info("{}: begin addBatch data...", TAG);
        LOGGER.info("{}: sqlMap ==>{}", TAG, JSONUtil.toJsonStr(sqlMap));
        LOGGER.info("{}: objMap ==>{}", TAG, JSONUtil.toJsonStr(objMap));
        long result = 0;
        // 批量操作限制: 如果总记录数大于500条，则分批执行，每批执行500条记录
        if (objMap.size() > 500) {
            int currentBatch = 1;
            int total = objMap.size();
            int batch = total % 500 == 0 ? (total / 500) : (total / 500) + 1;
            int limit = (currentBatch - 1) * 500;
            List<Map<String, Object>> tempList = new LinkedList<>();
            for (int x = 1; x <= batch; x++) {
                if ((limit + 500) > total) {
                    tempList = objMap.subList(limit, objMap.size());
                } else {
                    tempList = objMap.subList(limit, x * 500);
                }
                result += businessMapper.addBatchObject(tempList, sqlMap);
                currentBatch++;
                limit = (currentBatch - 1) * 500;
            }
        } else {
            result = businessMapper.addBatchObject(objMap, sqlMap);
        }
        long endTime = System.currentTimeMillis();
        LOGGER.info("{}: success total: {}", TAG, result);
        LOGGER.info("{}: 本次批量新增共耗时: {}s", TAG, (endTime - beginTime) / 1000f);
        LOGGER.info("{}: end addBatch data...", TAG);
        LOGGER.info(" ");
        return result;
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
     * @param beanMap    实例Map
     * @param clazz      公用字段class
     * @param methodType 公用字段赋值方法类型Annotation: create/update/delete
     * @throws Exception
     */
    public void setFillingStrategyContent(Map<String, Object> beanMap, Class<?> clazz, Class<? extends Annotation> methodType) throws Exception {
        if (!Objects.isNull(clazz)) {
            List<Method> methodList = Arrays.asList(clazz.getMethods());
            Method addMethod = methodList.stream().filter(x ->
                    x.isAnnotationPresent(methodType)).findAny().orElseThrow(() ->
                    new MyBatisRabbitPlugException("未找到新增时的自定义字段填充Method......"));
            Object obj = clazz.newInstance();
            addMethod.invoke(obj);
            List<Field> fieldList = Arrays.asList(clazz.getDeclaredFields());
            for (Field item : fieldList) {
                Method getMethod = clazz.getMethod("get" + StringUtils.capitalize(item.getName()));
                Object val = getMethod.invoke(obj);
                if(val!=null){
                    beanMap.put(item.getName(), val);
                }
            }
        }
    }

    /**
     * 复制QueryWapper-sqlMap
     *
     * @param source
     * @return
     */
    public void copyQueryWrapper(QueryWrapper source, Class<?> clazz) {
        Object obj = null;
        try {
            obj = clazz.newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        QueryWrapper query = new QueryWrapper(obj);
        source.getSqlMap().put(SqlKey.TABLE_NAME.getValue(), query.getTableInfo().getTableName());
    }

    /**
     * 枚举转换
     *
     * @param fieldInfoMap
     */
    public void convertEnumVal(Map<String, TableFieldInfo> fieldInfoMap, List<Map<String, Object>> objMapList) {
        Map<String, TableFieldInfo> enumPropertyMap = new HashMap<>();
        // 查找bean中所有枚举属性
        for (Map.Entry<String, TableFieldInfo> item : fieldInfoMap.entrySet()) {
            Class<?> clazz = item.getValue().getPropertyType();
            if (clazz.isEnum()) {
                enumPropertyMap.put(item.getKey(), item.getValue());
            }
        }
        // 转换数据库val对应的枚举name
        for (Map.Entry<String, TableFieldInfo> item : enumPropertyMap.entrySet()) {
            Class<?> enumClass = item.getValue().getPropertyType();
            try {
                Method method = enumClass.getMethod("valueConvertEnum", Integer.class);
                for (Map<String, Object> objMap : objMapList) {
                    Object parameter = objMap.get(item.getValue().getColumnName());
                    // 此处invoke的enum-method必须被static修饰,否则将抛出空指针异常
                    Enum iEnum = (Enum) method.invoke(null, parameter);
                    String enumName = iEnum.name();
                    if (org.apache.commons.lang3.StringUtils.isNotBlank(enumName))
                        objMap.put(item.getValue().getColumnName(), enumName);
                }
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }

        }
    }
}
