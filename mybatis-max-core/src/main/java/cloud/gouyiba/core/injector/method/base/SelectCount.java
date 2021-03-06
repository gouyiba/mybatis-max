package cloud.gouyiba.core.injector.method.base;

import cloud.gouyiba.core.bean.TableInfo;
import cloud.gouyiba.core.injector.MybatisMaxAbstractMethod;
import cloud.gouyiba.core.parse.ParseClass2TableInfo;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.mapping.SqlSource;

/**
 * 查询总记录数
 * this class by created wuyongfei on 2020/5/31 14:59
 **/
public class SelectCount extends MybatisMaxAbstractMethod {
    @Override
    public void injectMappedStatement(Class<?> mapperClass, Class<?> modelClass) {
        if (ObjectUtils.isEmpty(modelClass)) {
            return;
        }

        TableInfo tableInfo = ParseClass2TableInfo.parseClazzToTableInfo(modelClass);

        // in value foreach node
        String inValueNode = "\n<foreach collection=\"item\" index=\"index\" item=\"value\" open=\"(\" separator=\",\" close=\")\">#{value}</foreach>\n";

        String inNode = String.format("<foreach collection=\"queryWrapper.valMap\" index=\"key\" item=\"item\">" +
                "\n<if test=\"key == 'IN'\"> %s" +
                "</if>\n" +
                "</foreach>", inValueNode);

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
                "</if>" +
                "\n</where>", whereForeachNode, inForeachNode);

        // order by value foreach node
        String orderByForeachNode = String.format("<foreach collection=\"queryWrapper.sqlMap\" index=\"key\" item=\"item\">" +
                "\n<if test=\"key == 'ORDERBY'\"> ${item}" +
                "\n</if>" +
                "\n</foreach>");

        // order by foreach node
        String orderByNode = String.format("<if test=\"queryWrapper != null\">\n" +
                "<if test=\"queryWrapper.sqlMap != null and queryWrapper.sqlMap.size > 0\">\n%s\n</if>" +
                "\n</if>", orderByForeachNode);

        // 拼接完整SQL
        String selectByIdSql = String.format("<script>\nSELECT COUNT(1) FROM %s %s %s\n</script>",
                tableInfo.getTableName(), whereNode, orderByNode);

        // dynamic XMLLanguageDriver
        SqlSource sqlSource = languageDriver.createSqlSource(configuration, selectByIdSql, modelClass);

        // 添加到MappedStatement缓存
        addSelectMappedStatementForOther(
                mapperClass,
                // 灵活适配，维护性提高
                StringUtils.uncapitalize(this.getClass().getSimpleName()),
                sqlSource,
                Integer.class
        );
    }
}
