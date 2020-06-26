package cloub.gouyiba.core.injector.method.base;

import cloub.gouyiba.core.bean.TableFieldInfo;
import cloub.gouyiba.core.bean.TableInfo;
import cloub.gouyiba.core.injector.RabbitAbstractMethod;
import cloub.gouyiba.core.parse.ParseClass2TableInfo;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.mapping.SqlSource;

import java.lang.reflect.Field;
import java.util.Map;
import java.util.StringJoiner;

/**
 * 根据id查询
 * this class by created wuyongfei on 2020/5/31 14:58
 **/
public class SelectById extends RabbitAbstractMethod {
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

        // 拼接完整SQL
        String selectByIdSql = String.format("<script>\nSELECT %s FROM %s WHERE %s = #{id}\n</script>",
                selectedColumnJoin.toString(), tableInfo.getTableName(), columnMap.get(primaryKey.getName()).getColumnName());

        // dynamic XMLLanguageDriver
        SqlSource sqlSource = languageDriver.createSqlSource(configuration, selectByIdSql, Object.class);

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
