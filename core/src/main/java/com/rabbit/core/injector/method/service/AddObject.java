package com.rabbit.core.injector.method.service;

import com.rabbit.common.utils.SqlScriptUtil;
import com.rabbit.core.bean.TableInfo;
import com.rabbit.core.injector.RabbitAbstractMethod;
import org.apache.ibatis.executor.keygen.NoKeyGenerator;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlSource;

import java.util.Map;


/**
 * @ClassName AddObject
 * @ClassExplain: 新增实例
 * @Author Duxiaoyu
 * @Date 2020/5/5 11:05
 * @Since V 1.0
 */
public class AddObject extends RabbitAbstractMethod {

    @Override
    public MappedStatement injectMappedStatement(Class<?> mapperClass, Class<?> modelClass, TableInfo tableInfo) {
        StringBuffer sql=new StringBuffer("<script>");
        sql.append(SqlScriptUtil.convertIf("sqlMap.INSERT_HEAD!=null and sqlMap.INSERT_HEAD!=''","${sqlMap.INSERT_HEAD}"));
        sql.append(SqlScriptUtil.convertIf("sqlMap.TABLE_NAME!=null and sqlMap.TABLE_NAME!=''","${sqlMap.TABLE_NAME}"));
        sql.append(SqlScriptUtil.convertIf("sqlMap.INSERT_PAM_LEFT_BRA!=null and sqlMap.INSERT_PAM_LEFT_BRA!=''"," ${sqlMap.INSERT_PAM_LEFT_BRA}"));
        sql.append(SqlScriptUtil.convertIf("sqlMap.INSERT_PARAMETER!=null and sqlMap.INSERT_PARAMETER!=''","${sqlMap.INSERT_PARAMETER}"));
        sql.append(SqlScriptUtil.convertIf("sqlMap.INSERT_PAM_RIGHT_BRA!=null and sqlMap.INSERT_PAM_RIGHT_BRA!=''","${sqlMap.INSERT_PAM_RIGHT_BRA}"));
        sql.append(SqlScriptUtil.convertIf("sqlMap.INSERT_VALUE_KEYWORD!=null and sqlMap.INSERT_VALUE_KEYWORD!=''","${sqlMap.INSERT_VALUE_KEYWORD}"));
        sql.append(SqlScriptUtil.convertIf("sqlMap.INSERT_VAL_LEFT_BRA!=null and sqlMap.INSERT_VAL_LEFT_BRA!=''","${sqlMap.INSERT_VAL_LEFT_BRA}"));
        sql.append(SqlScriptUtil.convertIf("sqlMap.INSERT_VALUE!=null and sqlMap.INSERT_VALUE!=''","${sqlMap.INSERT_VALUE}"));
        sql.append(SqlScriptUtil.convertIf("sqlMap.INSERT_VAL_RIGHT_BRA!=null and sqlMap.INSERT_VAL_RIGHT_BRA!=''","${sqlMap.INSERT_VAL_RIGHT_BRA}"));
        sql.append("\n</script>");
        SqlSource sqlSource=languageDriver.createSqlSource(configuration,sql.toString(), Map.class);
        // 此处keyColumn主键默认是 ‘id’ 字段
        return addInsertMappedStatement(mapperClass, Map.class,"addObject",sqlSource,new NoKeyGenerator(),"objectMap.tempPrimKey","id");
    }
}
