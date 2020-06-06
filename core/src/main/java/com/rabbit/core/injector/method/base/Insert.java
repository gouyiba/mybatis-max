package com.rabbit.core.injector.method.base;

import cn.hutool.core.lang.Snowflake;
import cn.hutool.core.util.IdUtil;
import com.rabbit.common.utils.StringPool;
import com.rabbit.core.annotation.Id;
import com.rabbit.core.bean.TableFieldInfo;
import com.rabbit.core.bean.TableInfo;
import com.rabbit.core.constructor.DefaultAbstractWrapper;
import com.rabbit.core.enumation.PrimaryKey;
import com.rabbit.core.injector.RabbitAbstractMethod;
import com.rabbit.core.parse.ParseClass2TableInfo;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.builder.MapperBuilderAssistant;
import org.apache.ibatis.builder.StaticSqlSource;
import org.apache.ibatis.executor.keygen.KeyGenerator;
import org.apache.ibatis.executor.keygen.NoKeyGenerator;
import org.apache.ibatis.executor.keygen.SelectKeyGenerator;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.ResultMap;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.mapping.SqlSource;
import org.apache.ibatis.session.Configuration;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;
import java.util.Objects;

public class Insert extends RabbitAbstractMethod {
    @Override
    public void injectMappedStatement(Class<?> mapperClass, Class<?> modelClass) {
        if (ObjectUtils.isEmpty(modelClass)) {
            return;
        }

        TableInfo tableInfo = ParseClass2TableInfo.parseClazzToTableInfo(modelClass);
        Map<String, String> map = new DefaultAbstractWrapper(tableInfo).insertSqlGenerate();

        String keyProperty = null;
        String keyColumn = null;
        KeyGenerator keyGenerator = new NoKeyGenerator();
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
        addInsertMappedStatement(mapperClass, modelClass, StringUtils.uncapitalize(this.getClass().getSimpleName()), sqlSource, keyGenerator, keyProperty, keyColumn);
    }
}
