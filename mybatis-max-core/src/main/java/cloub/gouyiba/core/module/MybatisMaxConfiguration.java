package cloub.gouyiba.core.module;

import cloub.gouyiba.core.config.MybatisMaxConfig;
import cloub.gouyiba.core.typehandler.IEnumTypeHandler;
import cloub.gouyiba.core.typehandler.StringEnumTypeHandler;
import org.apache.ibatis.binding.MapperRegistry;
import org.apache.ibatis.executor.*;
import org.apache.ibatis.logging.Log;
import org.apache.ibatis.logging.LogFactory;
import org.apache.ibatis.mapping.Environment;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.scripting.LanguageDriver;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.transaction.Transaction;

/**
 * this class by created wuyongfei on 2020/5/10 16:26
 **/
public class MybatisMaxConfiguration extends Configuration {
    private static final Log logger = LogFactory.getLog(MybatisMaxConfiguration.class);
    MybatisMaxMapperRegistry mybatisMapperRegistry = new MybatisMaxMapperRegistry(this);
    private MybatisMaxConfig globalConfig;

    public MybatisMaxConfiguration(Environment environment) {
        this();
        this.environment = environment;
    }

    public MybatisMaxConfiguration() {
        this.mybatisMapperRegistry = new MybatisMaxMapperRegistry(this);
        this.mapUnderscoreToCamelCase = true;
        this.languageRegistry.setDefaultDriverClass(MybatisMaxXMLLanguageDriver.class);
        this.typeHandlerRegistry.register(IEnumTypeHandler.class);
        this.typeHandlerRegistry.register(StringEnumTypeHandler.class);
    }

    @Override
    public void addMappedStatement(MappedStatement ms) {
        logger.debug("addMappedStatement: " + ms.getId());
        if (this.mappedStatements.containsKey(ms.getId())) {
            logger.error("mapper[" + ms.getId() + "] is ignored, because it exists, maybe from xml file");
            return;
        }
        super.addMappedStatement(ms);
    }

    @Override
    public MapperRegistry getMapperRegistry() {
        return this.mybatisMapperRegistry;
    }

    @Override
    public <T> void addMapper(Class<T> type) {
        logger.debug("addMapper: " + type.getName());
        this.mybatisMapperRegistry.addMapper(type);
    }

    @Override
    public void addMappers(String packageName, Class<?> superType) {
        this.mybatisMapperRegistry.addMappers(packageName, superType);
    }

    @Override
    public void addMappers(String packageName) {
        this.mybatisMapperRegistry.addMappers(packageName);
    }

    @Override
    public <T> T getMapper(Class<T> type, SqlSession sqlSession) {
        return this.mybatisMapperRegistry.getMapper(type, sqlSession);
    }

    @Override
    public boolean hasMapper(Class<?> type) {
        return this.mybatisMapperRegistry.hasMapper(type);
    }

    @Override
    public void setDefaultScriptingLanguage(Class<? extends LanguageDriver> driver) {
        if (driver == null) {
            driver = MybatisMaxXMLLanguageDriver.class;
        }
        getLanguageRegistry().setDefaultDriverClass(driver);
    }

    @Override
    public Executor newExecutor(Transaction transaction, ExecutorType executorType) {
        executorType = executorType == null ? this.defaultExecutorType : executorType;
        executorType = executorType == null ? ExecutorType.SIMPLE : executorType;
        Object executor;
        if (ExecutorType.BATCH == executorType) {
            executor = new BatchExecutor(this, transaction);
        } else if (ExecutorType.REUSE == executorType) {
            executor = new ReuseExecutor(this, transaction);
        } else {
            executor = new SimpleExecutor(this, transaction);
        }
        if (this.cacheEnabled) {
            executor = new CachingExecutor((Executor) executor);
        }
        return (Executor) this.interceptorChain.pluginAll(executor);
    }

    public void setGlobalConfig(final MybatisMaxConfig globalConfig) {
        this.globalConfig = globalConfig;
    }

    public MybatisMaxConfig getGlobalConfig() {
        return this.globalConfig;
    }
}
