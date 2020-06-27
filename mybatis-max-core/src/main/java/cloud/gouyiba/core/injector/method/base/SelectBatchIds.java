package cloud.gouyiba.core.injector.method.base;

import cloud.gouyiba.core.bean.TableFieldInfo;
import cloud.gouyiba.core.bean.TableInfo;
import cloud.gouyiba.core.injector.MybatisMaxAbstractMethod;
import cloud.gouyiba.core.parse.ParseClass2TableInfo;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.mapping.SqlSource;

import java.lang.reflect.Field;
import java.util.Map;
import java.util.StringJoiner;

/**
 * 根据id批量查询
 * this class by created wuyongfei on 2020/5/31 14:58
 **/
public class SelectBatchIds extends MybatisMaxAbstractMethod {
    @Override
    public void injectMappedStatement(Class<?> mapperClass, Class<?> modelClass) {
        if (ObjectUtils.isEmpty(modelClass)) {
            return;
        }

        TableInfo tableInfo = ParseClass2TableInfo.parseClazzToTableInfo(modelClass);

        // 无 @Id 默认不注入该方法
        Field primaryKey = tableInfo.getPrimaryKey();
        if (ObjectUtils.isEmpty(primaryKey)) {
            return;
        }

        Map<String, TableFieldInfo> columnMap = tableInfo.getColumnMap();

        // 拼接SQL字段
        StringJoiner selectedColumnJoin = new StringJoiner(",");
        columnMap.keySet().stream()
                .map(key -> columnMap.get(key).getColumnName())
                .forEach(field -> selectedColumnJoin.add(field));

        // 拼接SQL动态值
        String foreachNode =
                "<foreach collection=\"idList\" index=\"index\" item=\"id\" open=\"(\" separator=\",\" close=\")\">" +
                        "\n#{id}\n" +
                        "</foreach>";

        // 拼接完整SQL
        String selectByIdsSql = String.format(
                "<script>\nSELECT %s FROM %s WHERE %s IN %s\n</script>",
                selectedColumnJoin.toString(), tableInfo.getTableName(), columnMap.get(primaryKey.getName()).getColumnName(), foreachNode);

        // dynamic XMLLanguageDriver
        SqlSource sqlSource = languageDriver.createSqlSource(configuration, selectByIdsSql, modelClass);

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
