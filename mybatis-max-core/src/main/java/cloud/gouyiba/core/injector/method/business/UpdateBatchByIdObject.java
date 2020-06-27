package cloud.gouyiba.core.injector.method.business;

import cloud.gouyiba.common.utils.SqlScriptUtil;
import cloud.gouyiba.core.injector.MybatisMaxAbstractMethod;
import org.apache.ibatis.mapping.SqlSource;

import java.util.Map;

/**
 * @ClassName UpdateBatchByIdObject
 * @ClassExplain: 批量修改根据主键
 * @Author Duxiaoyu
 * @Date 2020/5/5 11:06
 * @Since V 1.0
 */
public class UpdateBatchByIdObject extends MybatisMaxAbstractMethod {

    @Override
    public void injectMappedStatement(Class<?> mapperClass, Class<?> modelClass) {
        StringBuffer sql = new StringBuffer("<script>");
        sql.append(SqlScriptUtil.convertForeach("objectList", "obj", null, null, ";", null,
                "update ${sqlMap.TABLE_NAME}" +
                        SqlScriptUtil.convertTrim("set", null, null, ",",
                                SqlScriptUtil.convertForeach("sqlMap.UPDATE_VALUE.keys", "item", "i", null, null, null,
                                        SqlScriptUtil.convertIf("obj[item]!=null", "${sqlMap.UPDATE_VALUE[item]}"))) +
                        SqlScriptUtil.convertIf("sqlMap.UPDATE_WHERE!=null and sqlMap.UPDATE_WHERE!=''", "${sqlMap.UPDATE_WHERE}")));
        sql.append("\n</script>");
        SqlSource sqlSource = languageDriver.createSqlSource(configuration, sql.toString(), Map.class);
        addUpdateMappedStatement(mapperClass, Map.class, "updateBatchByIdObject", sqlSource);
    }
}
