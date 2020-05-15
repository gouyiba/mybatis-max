package com.rabbit.core.injector;

import com.rabbit.core.entity.User;
import org.apache.ibatis.builder.BuilderException;
import org.apache.ibatis.builder.IncompleteElementException;
import org.apache.ibatis.builder.MapperBuilderAssistant;
import org.apache.ibatis.logging.Log;
import org.apache.ibatis.logging.LogFactory;
import org.apache.ibatis.mapping.*;
import org.apache.ibatis.session.Configuration;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * this class by created wuyongfei on 2020/5/10 20:54
 **/
public abstract class AbstractRabbitSqlInjector implements IRabbitSqlInjector {
    private static final Log logger = LogFactory.getLog(AbstractRabbitSqlInjector.class);

    private Configuration configuration;

    private String currentNamespace;

    public AbstractRabbitSqlInjector() {
    }

    private List<ResultMap> getStatementResultMaps(String resultMap, Class<?> resultType, String statementId) {
        resultMap = this.applyCurrentNamespace(resultMap, true);
        List<ResultMap> resultMaps = new ArrayList();
        if (resultMap != null) {
            String[] resultMapNames = resultMap.split(",");
            String[] var6 = resultMapNames;
            int var7 = resultMapNames.length;

            for(int var8 = 0; var8 < var7; ++var8) {
                String resultMapName = var6[var8];

                try {
                    resultMaps.add(this.configuration.getResultMap(resultMapName.trim()));
                } catch (IllegalArgumentException var11) {
                    throw new IncompleteElementException("Could not find result map '" + resultMapName + "' referenced from '" + statementId + "'", var11);
                }
            }
        } else if (resultType != null) {
            ResultMap inlineResultMap = (new ResultMap.Builder(this.configuration, statementId + "-Inline", resultType, new ArrayList(), (Boolean)null)).build();
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

    public void inspectInject(MapperBuilderAssistant builderAssistant, Class<?> mapperClass) {
        Class<?> modelClass = this.extractModelClass(mapperClass);
        configuration = builderAssistant.getConfiguration();
        currentNamespace = mapperClass.getName();
        String sql = "<script>select * from test</script>";
        SqlSource sqlSource = builderAssistant.getConfiguration().getDefaultScriptingLanguageInstance().createSqlSource(builderAssistant.getConfiguration(), sql, modelClass);
        MappedStatement.Builder statementBuilder = (new MappedStatement.
                Builder(builderAssistant.getConfiguration(),
                mapperClass.getName() + "." + "list",
                sqlSource,
                SqlCommandType.SELECT)).resource("com/example/mybatisdemo/mapper/UserMapper.java (best guess)")
                .fetchSize(null)
                .timeout(null)
                .statementType(StatementType.STATEMENT)
                .keyGenerator(null)
                .keyProperty(null)
                .keyColumn(null)
                .databaseId(null)
                .lang(builderAssistant.getConfiguration().getDefaultScriptingLanguageInstance())
                .resultOrdered(false)
                .resultSets(null)
                .resultMaps(this.getStatementResultMaps(null, User.class, mapperClass.getName() + "." + "list"))
                .resultSetType(null)
                .flushCacheRequired(false)
                .cache(null);

        MappedStatement statement = statementBuilder.build();
//        builderAssistant.addMappedStatement(mapperClass.getName() + "." + "list", sqlSource, StatementType.PREPARED, SqlCommandType.SELECT,
//                null, null, null, null, null, mapperClass.getClass(),
//                null, false, true, false, null, null, null,
//                builderAssistant.getConfiguration().getDatabaseId(), builderAssistant.getConfiguration().getDefaultScriptingLanguageInstance(), null);
        configuration.addMappedStatement(statement);

        String sql1 = "<script>select count(*) from test</script>";
        SqlSource sqlSource1 = builderAssistant.getConfiguration().getDefaultScriptingLanguageInstance().createSqlSource(builderAssistant.getConfiguration(), sql1, modelClass);
        MappedStatement.Builder statementBuilder1 = (new MappedStatement.
                Builder(builderAssistant.getConfiguration(),
                mapperClass.getName() + "." + "count",
                sqlSource1,
                SqlCommandType.SELECT)).resource("com/example/mybatisdemo/mapper/UserMapper.java (best guess)")
                .fetchSize(null)
                .timeout(null)
                .statementType(StatementType.STATEMENT)
                .keyGenerator(null)
                .keyProperty(null)
                .keyColumn(null)
                .databaseId(null)
                .lang(builderAssistant.getConfiguration().getDefaultScriptingLanguageInstance())
                .resultOrdered(false)
                .resultSets(null)
                .resultMaps(this.getStatementResultMaps(null, Integer.class, mapperClass.getName() + "." + "list"))
                .resultSetType(null)
                .flushCacheRequired(false)
                .cache(null);

        MappedStatement statement1 = statementBuilder1.build();
//        builderAssistant.addMappedStatement(mapperClass.getName() + "." + "list", sqlSource, StatementType.PREPARED, SqlCommandType.SELECT,
//                null, null, null, null, null, mapperClass.getClass(),
//                null, false, true, false, null, null, null,
//                builderAssistant.getConfiguration().getDatabaseId(), builderAssistant.getConfiguration().getDefaultScriptingLanguageInstance(), null);
        configuration.addMappedStatement(statement1);
/*        if (modelClass != null) {
            String className = mapperClass.toString();
            Set<String> mapperRegistryCache = GlobalConfigUtils.getMapperRegistryCache(builderAssistant.getConfiguration());
            if (!mapperRegistryCache.contains(className)) {
                *//*List<AbstractMethod> methodList = this.getMethodList(mapperClass);
                if (CollectionUtils.isNotEmpty(methodList)) {
                    TableInfo tableInfo = TableInfoHelper.initTableInfo(builderAssistant, modelClass);
                    methodList.forEach((m) -> {
                        m.inject(builderAssistant, mapperClass, modelClass, tableInfo);
                    });
                } else {
                    logger.debug(mapperClass.toString() + ", No effective injection method was found.");
//                }
*//*
                mapperRegistryCache.add(className);
            }
        }*/
    }

//    public abstract List<AbstractMethod> getMethodList(Class<?> mapperClass);

    protected Class<?> extractModelClass(Class<?> mapperClass) {
        Type[] types = mapperClass.getGenericInterfaces();
        ParameterizedType target = null;
        Type[] var4 = types;
        int var5 = types.length;

        for(int var6 = 0; var6 < var5; ++var6) {
            Type type = var4[var6];
            if (type instanceof ParameterizedType) {
                Type[] typeArray = ((ParameterizedType)type).getActualTypeArguments();
               /* if (ArrayUtils.isNotEmpty(typeArray)) {
                    int var10 = typeArray.length;
                    byte var11 = 0;
                    if (var11 < var10) {
                        Type t = typeArray[var11];
                        if (!(t instanceof TypeVariable) && !(t instanceof WildcardType)) {
                            target = (ParameterizedType)type;
                        }
                    }
                }*/
                break;
            }
        }

        return target == null ? null : (Class)target.getActualTypeArguments()[0];
    }
}
