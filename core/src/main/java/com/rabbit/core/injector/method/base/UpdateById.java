package com.rabbit.core.injector.method.base;

import com.rabbit.common.utils.SqlScriptUtil;
import com.rabbit.core.bean.TableInfo;
import com.rabbit.core.constructor.DefaultAbstractWrapper;
import com.rabbit.core.enumation.MySqlKeyWord;
import com.rabbit.core.enumation.SqlKey;
import com.rabbit.core.injector.RabbitAbstractMethod;
import com.rabbit.core.parse.ParseClass2TableInfo;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.ibatis.mapping.SqlSource;

import java.util.Map;

/**
 * @ClassName UpdateById
 * @ClassExplain: 说明
 * @Author Duxiaoyu
 * @Date 2020/5/30 17:33
 * @Since V 1.0
 */
public class UpdateById extends RabbitAbstractMethod {

    @Override
    public void injectMappedStatement(Class<?> mapperClass, Class<?> modelClass) {
        if (ObjectUtils.isEmpty(modelClass)) {
            return;
        }
        TableInfo tableInfo= ParseClass2TableInfo.parseClazzToTableInfo(modelClass);
        Map<String,Object> sqlMap=new DefaultAbstractWrapper(tableInfo).updateSqlGenerate();
        Map<String,String> sqlValMap=(Map<String, String>) sqlMap.get(SqlKey.UPDATE_VALUE.getValue());
        // 批量修改目标实例参数
        for (String item : sqlValMap.keySet()) {
            sqlValMap.put(item, sqlValMap.get(item).replace("objectMap.", ""));
        }
        StringBuffer sqlVal=new StringBuffer("");
        for (Map.Entry<String,String> item:sqlValMap.entrySet()){
            sqlVal.append(SqlScriptUtil.convertIf(item.getKey()+"!=null",item.getValue()));
        }

        String where=sqlMap.get(SqlKey.UPDATE_WHERE.getValue()).toString();
        where=where.replace("objectMap.","");
        StringBuffer sql=new StringBuffer("<script>");
        sql.append(MySqlKeyWord.UPDATE+"\t");
        sql.append(tableInfo.getTableName()+"\t");
        sql.append(SqlScriptUtil.convertTrim("set",null,null,",",sqlVal.toString()));
        sql.append("\t"+where);
        sql.append("</script>");
        SqlSource sqlSource = languageDriver.createSqlSource(configuration, sql.toString(), modelClass);
        addUpdateMappedStatement(mapperClass,modelClass, "updateById", sqlSource);
    }
}
