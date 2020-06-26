package cloub.gouyiba.core.injector.method.base;

import cloub.gouyiba.core.bean.TableInfo;
import cloub.gouyiba.core.constructor.DeleteWrapper;
import cloub.gouyiba.core.enumation.MySqlKeyWord;
import cloub.gouyiba.core.injector.MybatisMaxAbstractMethod;
import cloub.gouyiba.core.parse.ParseClass2TableInfo;
import cloub.gouyiba.common.utils.SqlScriptUtil;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.mapping.SqlSource;

/**
 * @ClassName Delete
 * @ClassExplain: 删除
 * @Author Duxiaoyu
 * @Date 2020/5/30 17:31
 * @Since V 1.0
 */
public class Delete extends MybatisMaxAbstractMethod {

    @Override
    public void injectMappedStatement(Class<?> mapperClass, Class<?> modelClass) {
        if (ObjectUtils.isEmpty(modelClass)) {
            return;
        }

        TableInfo tableInfo = ParseClass2TableInfo.parseClazzToTableInfo(modelClass);
        StringBuffer sql = new StringBuffer("<script>");
        sql.append(MySqlKeyWord.DELETE.getValue() + " " + MySqlKeyWord.FROM.getValue() + "\t");
        sql.append(tableInfo.getTableName());
        sql.append(SqlScriptUtil.convertWhere(
                SqlScriptUtil.convertIf("sqlMap.IN!=null and sqlMap.IN!=''", SqlScriptUtil.unSafeParam("sqlMap.IN") +
                        SqlScriptUtil.convertForeach("valMap.IN", "item", null, "(", ",", ")", SqlScriptUtil.safeParam("item"))) +
                        SqlScriptUtil.convertIf("sqlMap.NOTIN!=null and sqlMap.NOTIN!=''", SqlScriptUtil.unSafeParam("sqlMap.NOTIN") +
                                SqlScriptUtil.convertForeach("valMap.NOTIN", "item", null, "(", ",", ")", SqlScriptUtil.safeParam("item"))) +
                        SqlScriptUtil.convertIf("whereSqlMap!=null",
                                SqlScriptUtil.convertForeach("whereSqlMap.keys", "item", null, null, null, null, SqlScriptUtil.unSafeParam("whereSqlMap[item]")))
        ));
        sql.append("</script>");
        SqlSource sqlSource = languageDriver.createSqlSource(configuration, sql.toString(), DeleteWrapper.class);
        addDeleteMappedStatement(mapperClass, // 灵活适配，维护性提高
                StringUtils.uncapitalize(this.getClass().getSimpleName()), sqlSource);
    }
}
