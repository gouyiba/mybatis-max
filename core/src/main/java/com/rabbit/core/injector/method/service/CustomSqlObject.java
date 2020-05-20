package com.rabbit.core.injector.method.service;

import com.rabbit.core.bean.TableInfo;
import com.rabbit.core.injector.RabbitAbstractMethod;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlSource;

import java.util.Map;

/**
 * @ClassName CustomSqlObject
 * @ClassExplain: 自定义sql查询
 * @Author Duxiaoyu
 * @Date 2020/5/5 11:05
 * @Since V 1.0
 */
public class CustomSqlObject extends RabbitAbstractMethod {

    @Override
    public MappedStatement injectMappedStatement(Class<?> mapperClass, Class<?> modelClass, TableInfo tableInfo) {
        StringBuffer sql=new StringBuffer("<script>");
        sql.append("${sql}");
        sql.append("\n</script>");
        SqlSource sqlSource=languageDriver.createSqlSource(configuration,sql.toString(),String.class);
        return addSelectMappedStatementForOther(mapperClass,"customSqlObject",sqlSource, Map.class);
    }
}
