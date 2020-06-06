package com.rabbit.core.injector.method.business;

import com.rabbit.common.utils.SqlScriptUtil;
import com.rabbit.core.injector.RabbitAbstractMethod;
import org.apache.ibatis.mapping.SqlSource;

import java.util.Map;

/**
 * @ClassName UpdateObject
 * @ClassExplain: 修改实例
 * @Author Duxiaoyu
 * @Date 2020/5/5 11:06
 * @Since V 1.0
 */
public class UpdateObject extends RabbitAbstractMethod {

    @Override
    public void injectMappedStatement(Class<?> mapperClass, Class<?> modelClass) {
        StringBuffer sql = new StringBuffer("<script>");
        sql.append("\nupdate ${sqlMap.TABLE_NAME}");
        sql.append(SqlScriptUtil.convertTrim("set", null, null, ",",
                SqlScriptUtil.convertForeach("sqlMap.UPDATE_VALUE.keys", "item", "i", null, null, null,
                        SqlScriptUtil.convertIf("objectMap[item]!=null", "${sqlMap.UPDATE_VALUE[item]}"))) +
                SqlScriptUtil.convertWhere(
                        SqlScriptUtil.convertIf("sqlMap.UPDATE_WHERE.IN!=null and sqlMap.UPDATE_WHERE.IN!=''",
                                "${sqlMap.UPDATE_WHERE.IN}" + SqlScriptUtil.convertForeach("sqlMap.UPDATE_WHERE.VALUE.IN", "item", null, "(", ",", ")", "\n#{item}")) +
                                SqlScriptUtil.convertIf("sqlMap.UPDATE_WHERE.NOTIN!=null and sqlMap.UPDATE_WHERE.NOTIN!=''",
                                        "${sqlMap.UPDATE_WHERE.NOTIN}" + SqlScriptUtil.convertForeach("sqlMap.UPDATE_WHERE.VALUE.NOTIN", "item", null, "(", ",", ")", "\n#{item}")) +
                                SqlScriptUtil.convertIf("sqlMap.UPDATE_WHERE.WHERE!=null", SqlScriptUtil.convertForeach("sqlMap.UPDATE_WHERE.WHERE.keys", "item", "index", null, null, null, "\n${sqlMap.UPDATE_WHERE.WHERE[item]}"))
                ) +
                SqlScriptUtil.convertIf("sqlMap.UPDATE_WHERE_PK!=null and sqlMap.UPDATE_WHERE_PK!=''", "${sqlMap.UPDATE_WHERE_PK}"));
        sql.append("\n</script>");
        SqlSource sqlSource = languageDriver.createSqlSource(configuration, sql.toString(), Map.class);
        addUpdateMappedStatement(mapperClass, Map.class, "updateObject", sqlSource);
    }
}
