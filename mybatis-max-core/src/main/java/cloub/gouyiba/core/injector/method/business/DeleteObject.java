package cloub.gouyiba.core.injector.method.business;


import cloub.gouyiba.common.utils.SqlScriptUtil;
import cloub.gouyiba.core.injector.MybatisMaxAbstractMethod;
import org.apache.ibatis.mapping.SqlSource;

import java.util.Map;

/**
 * @ClassName DeleteObject
 * @ClassExplain: 删除实例
 * @Author Duxiaoyu
 * @Date 2020/5/5 11:06
 * @Since V 1.0
 */
public class DeleteObject extends MybatisMaxAbstractMethod {

    @Override
    public void injectMappedStatement(Class<?> mapperClass, Class<?> modelClass) {
        StringBuffer sql = new StringBuffer("<script>");
        sql.append(SqlScriptUtil.convertIf("sqlMap.DELETE_HEAD!=null and sqlMap.DELETE_HEAD!=''", "${sqlMap.DELETE_HEAD}"));
        sql.append(SqlScriptUtil.convertIf("sqlMap.TABLE_NAME!=null and sqlMap.TABLE_NAME!=''", "${sqlMap.TABLE_NAME}"));
        sql.append(SqlScriptUtil.convertIf("sqlMap.DELETE_WHERE!=null and sqlMap.DELETE_WHERE!=''", "${sqlMap.DELETE_WHERE}"));
        sql.append("\n</script>");
        SqlSource sqlSource = languageDriver.createSqlSource(configuration, sql.toString(), Map.class);
        addDeleteMappedStatement(mapperClass, "deleteObject", sqlSource);
    }
}
