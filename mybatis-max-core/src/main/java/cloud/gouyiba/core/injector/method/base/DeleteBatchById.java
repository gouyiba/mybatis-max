package cloud.gouyiba.core.injector.method.base;

import cloud.gouyiba.common.utils.SqlScriptUtil;
import cloud.gouyiba.core.bean.TableFieldInfo;
import cloud.gouyiba.core.bean.TableInfo;
import cloud.gouyiba.core.enumation.MySqlKeyWord;
import cloud.gouyiba.core.injector.MybatisMaxAbstractMethod;
import cloud.gouyiba.core.parse.ParseClass2TableInfo;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.mapping.SqlSource;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * @ClassName DeleteBatchIds
 * @ClassExplain: 根据id批量删除
 * @Author Duxiaoyu
 * @Date 2020/5/30 17:32
 * @Since V 1.0
 */
public class DeleteBatchById extends MybatisMaxAbstractMethod {

    @Override
    public void injectMappedStatement(Class<?> mapperClass, Class<?> modelClass) {
        if (ObjectUtils.isEmpty(modelClass)) {
            return;
        }

        TableInfo tableInfo = ParseClass2TableInfo.parseClazzToTableInfo(modelClass);
        Map<String, TableFieldInfo> fieldInfoMap = tableInfo.getColumnMap();
        Field primaryKey = tableInfo.getPrimaryKey();
        if (Objects.isNull(primaryKey)) {
            return;
        }

        TableFieldInfo columnPK = fieldInfoMap.get(primaryKey.getName());
        String where = String.format("%s %s in ", MySqlKeyWord.WHERE.getValue(), columnPK.getColumnName());

        StringBuffer sql = new StringBuffer("<script>");
        sql.append(MySqlKeyWord.DELETE.getValue() + " " + MySqlKeyWord.FROM.getValue() + "\t");
        sql.append(tableInfo.getTableName() + "\t");
        sql.append(where);
        sql.append(SqlScriptUtil.convertForeach("idList", "item", null, "(", ",", ")", SqlScriptUtil.safeParam("item")));
        sql.append("</script>");
        SqlSource sqlSource = languageDriver.createSqlSource(configuration, sql.toString(), List.class);
        addDeleteMappedStatement(mapperClass, StringUtils.uncapitalize(this.getClass().getSimpleName()), sqlSource);
    }
}
