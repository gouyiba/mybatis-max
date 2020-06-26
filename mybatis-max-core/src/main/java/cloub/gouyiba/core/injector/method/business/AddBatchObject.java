package cloub.gouyiba.core.injector.method.business;


import cloub.gouyiba.core.enumation.MySqlKeyWord;
import cloub.gouyiba.common.utils.SqlScriptUtil;
import cloub.gouyiba.core.injector.MybatisMaxAbstractMethod;
import org.apache.ibatis.executor.keygen.NoKeyGenerator;
import org.apache.ibatis.mapping.SqlSource;

import java.util.Map;

/**
 * @ClassName AddBatchObject
 * @ClassExplain: 批量新增实例
 * @Author Duxiaoyu
 * @Date 2020/5/5 11:05
 * @Since V 1.0
 */
public class AddBatchObject extends MybatisMaxAbstractMethod {

    @Override
    public void injectMappedStatement(Class<?> mapperClass, Class<?> modelClass) {
        StringBuffer sql = new StringBuffer("<script>");
        sql.append(SqlScriptUtil.convertIf("sqlMap.INSERT_HEAD!=null and sqlMap.INSERT_HEAD!=''", "${sqlMap.INSERT_HEAD}"));
        sql.append(SqlScriptUtil.convertIf("sqlMap.TABLE_NAME!=null and sqlMap.TABLE_NAME!=''", "${sqlMap.TABLE_NAME}"));
        sql.append(SqlScriptUtil.convertIf("sqlMap.INSERT_PAM_LEFT_BRA!=null and sqlMap.INSERT_PAM_LEFT_BRA!=''", " ${sqlMap.INSERT_PAM_LEFT_BRA}"));
        sql.append(SqlScriptUtil.convertIf("sqlMap.INSERT_PARAMETER!=null and sqlMap.INSERT_PARAMETER!=''", "${sqlMap.INSERT_PARAMETER}"));
        sql.append(SqlScriptUtil.convertIf("sqlMap.INSERT_PAM_RIGHT_BRA!=null and sqlMap.INSERT_PAM_RIGHT_BRA!=''", "${sqlMap.INSERT_PAM_RIGHT_BRA}"));
        sql.append("\n" + MySqlKeyWord.VALUES.getValue());
        sql.append(SqlScriptUtil.convertForeach("objectList", "objectMap", null, null, ",", null,
                SqlScriptUtil.convertIf("sqlMap.INSERT_VALUE!=null and sqlMap.INSERT_VALUE!=''", "(${sqlMap.INSERT_VALUE})")));
        sql.append("\n</script>");
        SqlSource sqlSource = languageDriver.createSqlSource(configuration, sql.toString(), Map.class);
        addInsertMappedStatement(mapperClass, Map.class, "addBatchObject", sqlSource, new NoKeyGenerator(), null, null);
    }
}
