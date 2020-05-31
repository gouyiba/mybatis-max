package com.rabbit.core.injector.method.base;

import com.rabbit.common.utils.SqlScriptUtil;
import com.rabbit.core.bean.TableFieldInfo;
import com.rabbit.core.bean.TableInfo;
import com.rabbit.core.constructor.DeleteWrapper;
import com.rabbit.core.enumation.MySqlKeyWord;
import com.rabbit.core.injector.RabbitAbstractMethod;
import com.rabbit.core.parse.ParseClass2TableInfo;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.ibatis.mapping.SqlSource;

import java.lang.reflect.Field;
import java.util.Map;

/**
 * @ClassName Delete
 * @ClassExplain: 说明
 * @Author Duxiaoyu
 * @Date 2020/5/30 17:31
 * @Since V 1.0
 */
public class Delete extends RabbitAbstractMethod {

    @Override
    public void injectMappedStatement(Class<?> mapperClass, Class<?> modelClass) {
        if (ObjectUtils.isEmpty(modelClass)) {
            return;
        }

        TableInfo tableInfo = ParseClass2TableInfo.parseClazzToTableInfo(modelClass);
        Map<String, TableFieldInfo> fieldInfoMap = tableInfo.getColumnMap();
        Field primaryKey = tableInfo.getPrimaryKey();
        TableFieldInfo columnPK = fieldInfoMap.get(primaryKey.getName());

        StringBuffer sql = new StringBuffer("<script>");
        sql.append(MySqlKeyWord.DELETE.getValue() + " " + MySqlKeyWord.FROM.getValue()+"\t");
        sql.append(tableInfo.getTableName());
        sql.append(SqlScriptUtil.convertWhere(
                SqlScriptUtil.convertIf("sqlMap.IN!=null and sqlMap.IN!=''", SqlScriptUtil.unSafeParam("sqlMap.IN") +
                                SqlScriptUtil.convertForeach("valMap.IN", "item", null, "(", ",", ")", SqlScriptUtil.safeParam("item"))) +
                         SqlScriptUtil.convertIf("sqlMap.NOTIN!=null and sqlMap.NOTIN!=''", SqlScriptUtil.unSafeParam("sqlMap.NOTIN") +
                                SqlScriptUtil.convertForeach("valMap.NOTIN", "item", null, "(", ",", ")", SqlScriptUtil.safeParam("item"))) +
                         SqlScriptUtil.convertIf("whereSqlMap!=null",
                                SqlScriptUtil.convertForeach("whereSqlMap.keys", "item", null, null, null, null, SqlScriptUtil.unSafeParam("whereSqlMap[item]")))
        ));
        sql.append("</script>");
        SqlSource sqlSource = languageDriver.createSqlSource(configuration, sql.toString(), DeleteWrapper.class);
        addDeleteMappedStatement(mapperClass, "delete", sqlSource);
    }
}
