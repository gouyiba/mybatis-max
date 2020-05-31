package com.rabbit.core.injector.method.base;

import cn.hutool.core.lang.Snowflake;
import cn.hutool.core.util.IdUtil;
import com.rabbit.common.exception.MyBatisRabbitPlugException;
import com.rabbit.common.utils.StringPool;
import com.rabbit.core.annotation.Id;
import com.rabbit.core.bean.TableFieldInfo;
import com.rabbit.core.bean.TableInfo;
import com.rabbit.core.constructor.DefaultAbstractWrapper;
import com.rabbit.core.enumation.PrimaryKey;
import com.rabbit.core.injector.RabbitAbstractMethod;
import com.rabbit.core.parse.ParseClass2TableInfo;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.ibatis.builder.MapperBuilderAssistant;
import org.apache.ibatis.builder.StaticSqlSource;
import org.apache.ibatis.executor.keygen.KeyGenerator;
import org.apache.ibatis.executor.keygen.NoKeyGenerator;
import org.apache.ibatis.executor.keygen.SelectKeyGenerator;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.ResultMap;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.mapping.SqlSource;
import org.apache.ibatis.session.Configuration;
import org.springframework.beans.factory.annotation.Autowired;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;
import java.util.Objects;

public class Insert extends RabbitAbstractMethod {
    @Override
    public void injectMappedStatement(Class<?> mapperClass, Class<?> modelClass) {
        if (ObjectUtils.isEmpty(modelClass)) {
            return;
        }

        TableInfo tableInfo = ParseClass2TableInfo.parseClazzToTableInfo(modelClass);

        Map<String, String> map = new DefaultAbstractWrapper(tableInfo).insertSqlGenerate();

        String keyProperty=null;
        String keyColumn=null;
        KeyGenerator keyGenerator=new NoKeyGenerator();
        Field primaryKey = tableInfo.getPrimaryKey();
        if (Objects.isNull(primaryKey)) {
            throw new MyBatisRabbitPlugException("解析时未获取到主键字段......");
        }

        Id id = primaryKey.getAnnotation(Id.class);
        PrimaryKey pkEnum = id.generateType();
        Object idValue=null;
        if(id.isKeyGenerator()&&!id.isIncrementColumn()){
            switch (pkEnum) {
                case UUID32:
                    idValue= IdUtil.simpleUUID();
                    break;
                case OBJECTID:
                    idValue=IdUtil.objectId();
                    break;
                case SNOWFLAKE:
                    // 根据雪花算法生成64bit大小的分布式Long类型id，需要在 @id 中设置workerId和datacenterId
                    Snowflake snowflake = IdUtil.getSnowflake(id.workerId(), id.datacenterId());
                    idValue=snowflake.nextId();
                    break;
                default:
                    break;
            }
            // 处理主键生成
            keyGenerator=this.getKeyGenerator("insert",tableInfo,builderAssistant,idValue.toString());
            Map<String, TableFieldInfo> columnMap=tableInfo.getColumnMap();
            keyProperty=columnMap.get(tableInfo.getPrimaryKey().getName()).getPropertyName();
            keyColumn=columnMap.get(tableInfo.getPrimaryKey().getName()).getColumnName();
        }

        StringBuffer sql = new StringBuffer("<script>");
        sql.append(map.get("INSERT_HEAD") + "\t");
        sql.append(map.get("TABLE_NAME") + "\t");
        sql.append(map.get("INSERT_PAM_LEFT_BRA") + "\t");
        sql.append(map.get("INSERT_PARAMETER") + "\t");
        sql.append(map.get("INSERT_PAM_RIGHT_BRA") + "\t");
        sql.append(map.get("INSERT_VALUE_KEYWORD") + "\t");
        sql.append(map.get("INSERT_VAL_LEFT_BRA") + "\t");
        sql.append(map.get("INSERT_VALUE"));
        sql.append(map.get("INSERT_VAL_RIGHT_BRA"));
        sql.append("</script>");

        SqlSource sqlSource = languageDriver.createSqlSource(configuration, sql.toString(), modelClass);
        addInsertMappedStatement(mapperClass, modelClass, "insert", sqlSource, keyGenerator,keyProperty,keyColumn);
    }

    /**
     * 自动生成主键
     * @param baseStatementId
     * @param tableInfo
     * @param builderAssistant
     * @return
     */
    public SelectKeyGenerator getKeyGenerator(String baseStatementId, TableInfo tableInfo, MapperBuilderAssistant builderAssistant, String idValue){
        Configuration configuration = builderAssistant.getConfiguration();
        String id = builderAssistant.getCurrentNamespace() + StringPool.DOT + baseStatementId + SelectKeyGenerator.SELECT_KEY_SUFFIX;
        Class<?> primaryKeyType=tableInfo.getColumnMap().get(tableInfo.getPrimaryKey().getName()).getPropertyType();
        ResultMap resultMap = new ResultMap.Builder(builderAssistant.getConfiguration(), id, primaryKeyType, new ArrayList<>()).build();
        String selectKeySql="SELECT concat('','"+idValue+"','') as primKey";
        MappedStatement mappedStatement = new MappedStatement.Builder(builderAssistant.getConfiguration(), id,
                new StaticSqlSource(configuration, selectKeySql), SqlCommandType.SELECT)
                .keyProperty(tableInfo.getPrimaryKey().getName())
                .resultMaps(Collections.singletonList(resultMap))
                .build();
        configuration.addMappedStatement(mappedStatement);
        return new SelectKeyGenerator(mappedStatement, true);
    }
}
