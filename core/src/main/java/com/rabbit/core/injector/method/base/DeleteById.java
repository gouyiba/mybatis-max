package com.rabbit.core.injector.method.base;

import com.rabbit.core.bean.TableFieldInfo;
import com.rabbit.core.bean.TableInfo;
import com.rabbit.core.enumation.MySqlKeyWord;
import com.rabbit.core.enumation.SqlKey;
import com.rabbit.core.injector.RabbitAbstractMethod;
import com.rabbit.core.parse.ParseClass2TableInfo;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.ibatis.mapping.SqlSource;

import java.lang.reflect.Field;
import java.util.Map;

/**
 * @ClassName DeleteById
 * @ClassExplain: 说明
 * @Author Duxiaoyu
 * @Date 2020/5/30 17:30
 * @Since V 1.0
 */
public class DeleteById extends RabbitAbstractMethod {

    @Override
    public void injectMappedStatement(Class<?> mapperClass, Class<?> modelClass) {
        if (ObjectUtils.isEmpty(modelClass)) {
            return;
        }

        TableInfo tableInfo= ParseClass2TableInfo.parseClazzToTableInfo(modelClass);
        Map<String, TableFieldInfo> fieldInfoMap = tableInfo.getColumnMap();
        Field primaryKey = tableInfo.getPrimaryKey();
        TableFieldInfo columnPK = fieldInfoMap.get(primaryKey.getName());
        String where = String.format("%s %s=#{%s,jdbcType=%s}", MySqlKeyWord.WHERE.getValue(), columnPK.getColumnName(), "id", columnPK.getJdbcType().getValue());

        StringBuffer sql=new StringBuffer("<script>");
        sql.append(MySqlKeyWord.DELETE.getValue() + " " + MySqlKeyWord.FROM.getValue()+"\t");
        sql.append(tableInfo.getTableName()+"\t");
        sql.append(where);
        sql.append("</script>");
        SqlSource sqlSource=languageDriver.createSqlSource(configuration,sql.toString(), Object.class);
        addDeleteMappedStatement(mapperClass,"deleteById",sqlSource);
    }
}
