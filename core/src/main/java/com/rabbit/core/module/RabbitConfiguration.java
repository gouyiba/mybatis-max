package com.rabbit.core.module;

import com.rabbit.core.config.RabbitConfig;
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
public class RabbitConfiguration extends Configuration {
    private static final Log logger = LogFactory.getLog(RabbitConfiguration.class);
    RabbitMapperRegistry mybatisMapperRegistry = new RabbitMapperRegistry(this);
    private RabbitConfig globalConfig;

    public RabbitConfiguration(Environment environment) {
        this();
        this.environment = environment;
    }

    public RabbitConfiguration() {
        this.mybatisMapperRegistry = new RabbitMapperRegistry(this);
        this.mapUnderscoreToCamelCase = true;
        this.languageRegistry.setDefaultDriverClass(RabbitXMLLanguageDriver.class);
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
            driver = RabbitXMLLanguageDriver.class;
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

    public void setGlobalConfig(final RabbitConfig globalConfig) {
        this.globalConfig = globalConfig;
    }

    public RabbitConfig getGlobalConfig() {
        return this.globalConfig;
    }
}
