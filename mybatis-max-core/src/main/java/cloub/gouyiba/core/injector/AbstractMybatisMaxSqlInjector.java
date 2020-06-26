package cloub.gouyiba.core.injector;

import cloub.gouyiba.common.utils.ArrayUtils;
import cloub.gouyiba.common.utils.CollectionUtils;
import org.apache.ibatis.builder.BuilderException;
import org.apache.ibatis.builder.IncompleteElementException;
import org.apache.ibatis.builder.MapperBuilderAssistant;
import org.apache.ibatis.logging.Log;
import org.apache.ibatis.logging.LogFactory;
import org.apache.ibatis.mapping.ResultMap;
import org.apache.ibatis.session.Configuration;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.lang.reflect.WildcardType;
import java.util.ArrayList;
import java.util.List;

/**
 * this class by created wuyongfei on 2020/5/10 20:54
 **/
public abstract class AbstractMybatisMaxSqlInjector implements IMybatisMaxSqlInjector {
    private static final Log logger = LogFactory.getLog(AbstractMybatisMaxSqlInjector.class);

    private Configuration configuration;

    private String currentNamespace;

    public AbstractMybatisMaxSqlInjector() {
    }

    private List<ResultMap> getStatementResultMaps(String resultMap, Class<?> resultType, String statementId) {
        resultMap = this.applyCurrentNamespace(resultMap, true);
        List<ResultMap> resultMaps = new ArrayList();
        if (resultMap != null) {
            String[] resultMapNames = resultMap.split(",");
            String[] var6 = resultMapNames;
            int var7 = resultMapNames.length;

            for (int var8 = 0; var8 < var7; ++var8) {
                String resultMapName = var6[var8];

                try {
                    resultMaps.add(this.configuration.getResultMap(resultMapName.trim()));
                } catch (IllegalArgumentException var11) {
                    throw new IncompleteElementException("Could not find result map '" + resultMapName + "' referenced from '" + statementId + "'", var11);
                }
            }
        } else if (resultType != null) {
            ResultMap inlineResultMap = (new ResultMap.Builder(this.configuration, statementId + "-Inline", resultType, new ArrayList(), (Boolean) null)).build();
            resultMaps.add(inlineResultMap);
        }

        return resultMaps;
    }

    public String applyCurrentNamespace(String base, boolean isReference) {
        if (base == null) {
            return null;
        } else {
            if (isReference) {
                if (base.contains(".")) {
                    return base;
                }
            } else {
                if (base.startsWith(this.currentNamespace + ".")) {
                    return base;
                }

                if (base.contains(".")) {
                    throw new BuilderException("Dots are not allowed in element names, please remove it from " + base);
                }
            }

            return this.currentNamespace + "." + base;
        }
    }

    /**
     * 注入自定义方法
     *
     * @param builderAssistant
     * @param mapperClass
     */
    @Override
    public void inspectInject(MapperBuilderAssistant builderAssistant, Class<?> mapperClass) {
        Class<?> modelClass = extractModelClass(mapperClass);
        List<MybatisMaxAbstractMethod> methodList = this.getMethodList(mapperClass);
        if (CollectionUtils.isNotEmpty(methodList)) {
            methodList.forEach(m -> m.inject(builderAssistant, mapperClass, modelClass));
        } else {
            logger.debug(mapperClass.toString() + ", No effective injection method was found.");
        }
    }

    public abstract List<MybatisMaxAbstractMethod> getMethodList(Class<?> mapperClass);

    /**
     * 提取泛型
     *
     * @param mapperClass
     * @return
     */
    protected Class<?> extractModelClass(Class<?> mapperClass) {
        Type[] types = mapperClass.getGenericInterfaces();
        ParameterizedType target = null;
        Type[] var4 = types;
        int var5 = types.length;

        for (int var6 = 0; var6 < var5; ++var6) {
            Type type = var4[var6];
            if (type instanceof ParameterizedType) {
                Type[] typeArray = ((ParameterizedType) type).getActualTypeArguments();
                if (ArrayUtils.isNotEmpty(typeArray)) {
                    int var10 = typeArray.length;
                    byte var11 = 0;
                    if (var11 < var10) {
                        Type t = typeArray[var11];
                        if (!(t instanceof TypeVariable) && !(t instanceof WildcardType)) {
                            target = (ParameterizedType) type;
                        }
                    }
                }
                break;
            }
        }

        return target == null ? null : (Class) target.getActualTypeArguments()[0];
    }
}
