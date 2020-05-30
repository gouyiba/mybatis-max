package com.rabbit.core.injector.method.base;

import com.rabbit.core.bean.TableInfo;
import com.rabbit.core.constructor.DefaultAbstractWrapper;
import com.rabbit.core.injector.RabbitAbstractMethod;
import com.rabbit.core.parse.ParseClass2TableInfo;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.ibatis.executor.keygen.NoKeyGenerator;
import org.apache.ibatis.mapping.SqlSource;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Map;

public class Insert extends RabbitAbstractMethod {
    @Override
    public void injectMappedStatement(Class<?> mapperClass, Class<?> modelClass) {
        if (ObjectUtils.isEmpty(modelClass)) {
            return;
        }

        TableInfo tableInfo = ParseClass2TableInfo.parseClazzToTableInfo(modelClass);

        Map<String, String> map = new DefaultAbstractWrapper(tableInfo).insertSqlGenerate();

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

        addInsertMappedStatement(mapperClass, modelClass, "insert", sqlSource, new NoKeyGenerator(),
                tableInfo.getPrimaryKey() != null ? tableInfo.getPrimaryKey().getName() : null,
                tableInfo.getPrimaryKey() != null ? tableInfo.getPrimaryKey().getName() : null);
    }
}
