package com.rabbit.core.injector.method.service;

import com.rabbit.common.utils.SqlScriptUtil;
import com.rabbit.core.bean.TableInfo;
import com.rabbit.core.injector.RabbitAbstractMethod;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlSource;

import java.util.Map;

/**
 * @ClassName DeleteBatchByIdObject
 * @ClassExplain: 批量删除根据主键
 * @Author Duxiaoyu
 * @Date 2020/5/5 11:07
 * @Since V 1.0
 */
public class DeleteBatchByIdObject extends RabbitAbstractMethod {

    @Override
    public MappedStatement injectMappedStatement(Class<?> mapperClass, Class<?> modelClass, TableInfo tableInfo) {
        StringBuffer sql=new StringBuffer("<script>");
        sql.append(SqlScriptUtil.convertIf("sqlMap.DELETE_HEAD!=null and sqlMap.DELETE_HEAD!=''","${sqlMap.DELETE_HEAD}"));
        sql.append(SqlScriptUtil.convertIf("sqlMap.TABLE_NAME!=null and sqlMap.TABLE_NAME!=''","${sqlMap.TABLE_NAME}"));
        sql.append(SqlScriptUtil.convertIf("sqlMap.DELETE_WHERE!=null and sqlMap.DELETE_WHERE!=''","${sqlMap.DELETE_WHERE}"));
        sql.append(SqlScriptUtil.convertForeach("objectList","item",null,"(",",",")","#{item}"));
        sql.append("\n</script>");
        SqlSource sqlSource=languageDriver.createSqlSource(configuration,sql.toString(), Map.class);
        return addDeleteMappedStatement(mapperClass,"deleteBatchByIdObject",sqlSource);
    }
}
