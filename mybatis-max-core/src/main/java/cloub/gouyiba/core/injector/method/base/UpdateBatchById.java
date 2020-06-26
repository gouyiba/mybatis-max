package cloub.gouyiba.core.injector.method.base;

import cloub.gouyiba.core.enumation.MySqlKeyWord;
import cloub.gouyiba.core.enumation.SqlKey;
import cloub.gouyiba.common.utils.SqlScriptUtil;
import cloub.gouyiba.core.bean.TableInfo;
import cloub.gouyiba.core.constructor.DefaultAbstractWrapper;
import cloub.gouyiba.core.injector.RabbitAbstractMethod;
import cloub.gouyiba.core.parse.ParseClass2TableInfo;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.mapping.SqlSource;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * @ClassName UpdateBatchById
 * @ClassExplain: 根据id批量修改
 * @Author Duxiaoyu
 * @Date 2020/5/30 17:36
 * @Since V 1.0
 */
public class UpdateBatchById extends RabbitAbstractMethod {

    @Override
    public void injectMappedStatement(Class<?> mapperClass, Class<?> modelClass) {
        if (ObjectUtils.isEmpty(modelClass)) {
            return;
        }

        TableInfo tableInfo = ParseClass2TableInfo.parseClazzToTableInfo(modelClass);
        Field primaryKey = tableInfo.getPrimaryKey();
        if (Objects.isNull(primaryKey)) {
            return;
        }
        Map<String, Object> sqlMap = new DefaultAbstractWrapper(tableInfo).updateSqlGenerate();
        Map<String, String> sqlValMap = (Map<String, String>) sqlMap.get(SqlKey.UPDATE_VALUE.getValue());
        // 批量修改目标实例参数
        for (String item : sqlValMap.keySet()) {
            sqlValMap.put(item, sqlValMap.get(item).replace("objectMap", "item"));
        }

        // TODO: 第一版全量更新
        StringBuffer sqlVal = new StringBuffer("");
        for (Map.Entry<String, String> item : sqlValMap.entrySet()) {
            sqlVal.append(item.getValue());
        }

        String where = sqlMap.get(SqlKey.UPDATE_WHERE.getValue()).toString();
        where = where.replace("objectMap", "item");
        StringBuffer sql = new StringBuffer("<script>");
        sql.append(SqlScriptUtil.convertForeach("entityList", "item", null, null, ";", null,
                MySqlKeyWord.UPDATE + "\t" + tableInfo.getTableName() + "\t" +
                        SqlScriptUtil.convertTrim("set", null, null, ",", sqlVal.toString()) + where
        ));
        sql.append("</script>");
        SqlSource sqlSource = languageDriver.createSqlSource(configuration, sql.toString(), List.class);
        addUpdateMappedStatement(mapperClass, List.class, StringUtils.uncapitalize(this.getClass().getSimpleName()), sqlSource);
    }
}
