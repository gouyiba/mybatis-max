package com.rabbit.core.injector.method.base;

import com.rabbit.common.utils.SqlScriptUtil;
import com.rabbit.core.bean.TableInfo;
import com.rabbit.core.constructor.DefaultAbstractWrapper;
import com.rabbit.core.enumation.MySqlKeyWord;
import com.rabbit.core.enumation.SqlKey;
import com.rabbit.core.injector.RabbitAbstractMethod;
import com.rabbit.core.parse.ParseClass2TableInfo;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.mapping.SqlSource;

import java.util.Map;

/**
 * @ClassName Update
 * @ClassExplain: 说明
 * @Author Duxiaoyu
 * @Date 2020/5/30 17:37
 * @Since V 1.0
 */
public class Update extends RabbitAbstractMethod {


    @Override
    public void injectMappedStatement(Class<?> mapperClass, Class<?> modelClass) {
        if (ObjectUtils.isEmpty(modelClass)) {
            return;
        }

        TableInfo tableInfo = ParseClass2TableInfo.parseClazzToTableInfo(modelClass);
        Map<String, Object> sqlMap = new DefaultAbstractWrapper(tableInfo).updateSqlGenerate();
        Map<String, String> sqlValMap = (Map<String, String>) sqlMap.get(SqlKey.UPDATE_VALUE.getValue());
        // 批量修改目标实例参数
        for (String item : sqlValMap.keySet()) {
            sqlValMap.put(item, sqlValMap.get(item).replace("objectMap", "entity"));
        }

        // TODO: 第一版全量更新
        StringBuffer sqlVal = new StringBuffer("");
        for (Map.Entry<String, String> item : sqlValMap.entrySet()) {
            sqlVal.append(SqlScriptUtil.convertIf("entity."+item.getKey()+"!=null",item.getValue()));
        }


        StringBuffer sql = new StringBuffer("<script>");
        sql.append(MySqlKeyWord.UPDATE + "\t");
        sql.append(tableInfo.getTableName() + "\t");
        sql.append(SqlScriptUtil.convertTrim("set", null, null, ",", sqlVal.toString()));
        sql.append(SqlScriptUtil.convertWhere(
                SqlScriptUtil.convertIf("sqlMap.IN!=null and sqlMap.IN!=''", SqlScriptUtil.unSafeParam("sqlMap.IN") +
                        SqlScriptUtil.convertForeach("valMap.IN", "item", null, "(", ",", ")", SqlScriptUtil.safeParam("item"))) +
                        SqlScriptUtil.convertIf("sqlMap.NOTIN!=null and sqlMap.NOTIN!=''", SqlScriptUtil.unSafeParam("sqlMap.NOTIN") +
                                SqlScriptUtil.convertForeach("valMap.NOTIN", "item", null, "(", ",", ")", SqlScriptUtil.safeParam("item"))) +
                        SqlScriptUtil.convertIf("whereSqlMap!=null",
                                SqlScriptUtil.convertForeach("whereSqlMap.keys", "item", null, null, null, null, SqlScriptUtil.unSafeParam("whereSqlMap[item]")))
        ));
        sql.append("</script>");
        SqlSource sqlSource = languageDriver.createSqlSource(configuration, sql.toString(), modelClass);
        addUpdateMappedStatement(mapperClass, modelClass, StringUtils.uncapitalize(this.getClass().getSimpleName()), sqlSource);
    }
}
