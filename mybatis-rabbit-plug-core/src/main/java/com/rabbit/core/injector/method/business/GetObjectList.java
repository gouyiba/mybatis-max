package com.rabbit.core.injector.method.business;

import com.rabbit.common.utils.SqlScriptUtil;
import com.rabbit.core.injector.RabbitAbstractMethod;
import org.apache.ibatis.mapping.SqlSource;

import java.util.Map;

/**
 * @ClassName GetObjectList
 * @ClassExplain: 查询多个实例
 * @Author Duxiaoyu
 * @Date 2020/5/5 11:05
 * @Since V 1.0
 */
public class GetObjectList extends RabbitAbstractMethod {

    @Override
    public void injectMappedStatement(Class<?> mapperClass, Class<?> modelClass) {
        StringBuffer sql = new StringBuffer("<script>");
        sql.append("\nselect");
        sql.append(SqlScriptUtil.convertIf("sqlMap.setColumn!=null and sqlMap.setColumn!=''", "${sqlMap.setColumn}"));
        sql.append(SqlScriptUtil.convertIf("sqlMap.setColumn==null or sqlMap.setColumn==''", "*"));
        sql.append("\nfrom ${sqlMap.TABLE_NAME}");
        sql.append(SqlScriptUtil.convertWhere(
                SqlScriptUtil.convertIf("sqlMap.IN!=null and sqlMap.IN!=''",
                        "${sqlMap.IN}" + SqlScriptUtil.convertForeach("valMap.IN", "item", null, "(", ",", ")", "\n#{item}")) +
                        SqlScriptUtil.convertIf("sqlMap.NOTIN!=null and sqlMap.NOTIN!=''",
                                "${sqlMap.NOTIN}" + SqlScriptUtil.convertForeach("valMap.NOTIN", "item", null, "(", ",", ")", "\n#{item}")) +
                        SqlScriptUtil.convertIf("sqlMap.WHERE!=null", SqlScriptUtil.convertForeach("sqlMap.WHERE.keys", "item", "index", null, null, null, "\n${sqlMap.WHERE[item]}"))
        ));
        sql.append(SqlScriptUtil.convertIf("sqlMap.ORDERBY!=null and sqlMap.ORDERBY!=''","${sqlMap.ORDERBY}"));
        sql.append(SqlScriptUtil.convertIf("sqlMap.LIMIT!=null and sqlMap.LIMIT!=''","${sqlMap.LIMIT}"));
        sql.append("\n</script>");
        SqlSource sqlSource=languageDriver.createSqlSource(configuration,sql.toString(), Map.class);
        addSelectMappedStatementForOther(mapperClass,"getObjectList",sqlSource,Map.class);
    }
}
