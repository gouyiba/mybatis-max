package com.rabbit.core.injector.method.base;

import com.rabbit.core.bean.TableFieldInfo;
import com.rabbit.core.bean.TableInfo;
import com.rabbit.core.injector.RabbitAbstractMethod;
import com.rabbit.core.parse.ParseClass2TableInfo;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.mapping.SqlSource;

import java.util.Map;
import java.util.StringJoiner;

/**
 * 查询单条记录
 * this class by created wuyongfei on 2020/5/31 14:59
 **/
public class SelectOne extends RabbitAbstractMethod {
    @Override
    public void injectMappedStatement(Class<?> mapperClass, Class<?> modelClass) {
        if (ObjectUtils.isEmpty(modelClass)) {
            return;
        }

        TableInfo tableInfo = ParseClass2TableInfo.parseClazzToTableInfo(modelClass);

        Map<String, TableFieldInfo> columnMap = tableInfo.getColumnMap();

        // 拼接SQL字段
        StringJoiner selectedColumnJoin = new StringJoiner(",");
        columnMap.keySet().stream()
                .map(key -> columnMap.get(key).getColumnName())
                .forEach(field -> selectedColumnJoin.add(field));

        String inValueNode = "\n<foreach collection=\"item\" index=\"index\" item=\"value\" open=\"(\" separator=\",\" close=\")\">#{value}</foreach>\n";

        // in value foreach node
        String inNode = String.format("\n<foreach collection=\"queryWrapper.valMap\" index=\"key\" item=\"item\">" +
                "\n<if test=\"key == 'IN'\"> %s" +
                "</if>\n" +
                "</foreach>\n", inValueNode);

        // in foreach node
        String inForeachNode = String.format("<foreach collection=\"queryWrapper.sqlMap\" index=\"key\" item=\"item\">" +
                "\n<if test=\"key == 'IN'\">${item} %s" +
                "</if>\n" +
                "</foreach>", inNode);

        // where foreach node
        String whereForeachNode = "<foreach collection=\"queryWrapper.whereSqlMap\" index=\"key\" item=\"item\">" +
                "\n${item}\n" +
                "</foreach>";

        // 拼接SQL动态WHERE
        String whereNode = String.format("<where>\n" +
                "<if test=\"queryWrapper != null\">\n" +
                "<if test=\"queryWrapper.whereSqlMap != null and queryWrapper.whereSqlMap.size > 0\">\n%s\n</if>" +
                "<if test=\"queryWrapper.sqlMap != null and queryWrapper.sqlMap.size > 0\">\n%s\n</if>" +
                "\n</if>" +
                "\n</where>", whereForeachNode, inForeachNode);

        // 拼接完整SQL
        String selectByIdSql = String.format("<script>\nSELECT %s FROM %s %s\n</script>",
                selectedColumnJoin.toString(), tableInfo.getTableName(), whereNode);

        // dynamic XMLLanguageDriver
        SqlSource sqlSource = languageDriver.createSqlSource(configuration, selectByIdSql, modelClass);

        // 添加到MappedStatement缓存
        addSelectMappedStatementForOther(
                mapperClass,
                // 灵活适配，维护性提高
                StringUtils.uncapitalize(this.getClass().getSimpleName()),
                sqlSource,
                modelClass
        );
    }
}
