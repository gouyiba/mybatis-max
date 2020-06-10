package com.rabbit.core.module;

import com.rabbit.core.config.RabbitConfig;
import com.rabbit.core.util.RabbitConfigUtils;
import org.apache.ibatis.builder.xml.XMLMapperBuilder;
import org.apache.ibatis.cache.Cache;
import org.apache.ibatis.executor.ErrorContext;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.io.VFS;
import org.apache.ibatis.mapping.DatabaseIdProvider;
import org.apache.ibatis.mapping.Environment;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.reflection.factory.ObjectFactory;
import org.apache.ibatis.reflection.wrapper.ObjectWrapperFactory;
import org.apache.ibatis.scripting.LanguageDriver;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.transaction.TransactionFactory;
import org.apache.ibatis.type.TypeHandler;
import org.mybatis.logging.Logger;
import org.mybatis.logging.LoggerFactory;
import org.mybatis.spring.transaction.SpringManagedTransactionFactory;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.core.NestedIOException;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.core.type.ClassMetadata;
import org.springframework.core.type.classreading.CachingMetadataReaderFactory;
import org.springframework.core.type.classreading.MetadataReaderFactory;
import org.springframework.jdbc.datasource.TransactionAwareDataSourceProxy;
import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;
import org.springframework.util.StringUtils;

import javax.sql.DataSource;
import java.io.IOException;
import java.lang.reflect.Modifier;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Optional;
import java.util.Properties;
import java.util.Set;
import java.util.stream.Stream;

import static org.springframework.util.StringUtils.hasLength;
import static org.springframework.util.StringUtils.isEmpty;

/**
 * this class by created wuyongfei on 2020/5/10 01:29
 **/
public class RabbitSqlSessionFactoryBean implements FactoryBean<SqlSessionFactory>, InitializingBean, ApplicationListener<ApplicationEvent> {
    private static final Logger LOGGER = LoggerFactory.getLogger(RabbitSqlSessionFactoryBean.class);
    private static final ResourcePatternResolver RESOURCE_PATTERN_RESOLVER = new PathMatchingResourcePatternResolver();
    private static final MetadataReaderFactory METADATA_READER_FACTORY = new CachingMetadataReaderFactory();
    private Resource configLocation;
    private RabbitConfiguration configuration;
    private Resource[] mapperLocations;
    private DataSource dataSource;
    private TransactionFactory transactionFactory;
    private Properties configurationProperties;
    private SqlSessionFactory sqlSessionFactory;
    private boolean failFast;
    private Interceptor[] plugins;
    private TypeHandler<?>[] typeHandlers;
    private String typeHandlersPackage;
    private Class<?>[] typeAliases;
    private String typeAliasesPackage;
    private Class<?> typeAliasesSuperType;
    private LanguageDriver[] scriptingLanguageDrivers;
    private Class<? extends LanguageDriver> defaultScriptingLanguageDriver;
    private DatabaseIdProvider databaseIdProvider;
    private Class<? extends VFS> vfs;
    private Cache cache;
    private ObjectFactory objectFactory;
    private ObjectWrapperFactory objectWrapperFactory;
    private RabbitConfig globalConfig;
    private boolean banner = true;

    public RabbitSqlSessionFactoryBean() {
    }

    public void setObjectFactory(ObjectFactory objectFactory) {
        this.objectFactory = objectFactory;
    }

    public void setObjectWrapperFactory(ObjectWrapperFactory objectWrapperFactory) {
        this.objectWrapperFactory = objectWrapperFactory;
    }

    public DatabaseIdProvider getDatabaseIdProvider() {
        return this.databaseIdProvider;
    }

    public void setDatabaseIdProvider(DatabaseIdProvider databaseIdProvider) {
        this.databaseIdProvider = databaseIdProvider;
    }

    public Class<? extends VFS> getVfs() {
        return this.vfs;
    }

    public void setVfs(Class<? extends VFS> vfs) {
        this.vfs = vfs;
    }

    public Cache getCache() {
        return this.cache;
    }

    public void setCache(Cache cache) {
        this.cache = cache;
    }

    public void setPlugins(Interceptor... plugins) {
        this.plugins = plugins;
    }

    public void setTypeAliasesPackage(String typeAliasesPackage) {
        this.typeAliasesPackage = typeAliasesPackage;
    }

    public void setTypeAliasesSuperType(Class<?> typeAliasesSuperType) {
        this.typeAliasesSuperType = typeAliasesSuperType;
    }

    public void setTypeHandlersPackage(String typeHandlersPackage) {
        this.typeHandlersPackage = typeHandlersPackage;
    }

    public void setTypeHandlers(TypeHandler... typeHandlers) {
        this.typeHandlers = typeHandlers;
    }

    public void setTypeAliases(Class... typeAliases) {
        this.typeAliases = typeAliases;
    }

    public void setFailFast(boolean failFast) {
        this.failFast = failFast;
    }

    public void setConfigLocation(Resource configLocation) {
        this.configLocation = configLocation;
    }

    public void setConfiguration(RabbitConfiguration configuration) {
        this.configuration = configuration;
    }

    public void setMapperLocations(Resource... mapperLocations) {
        this.mapperLocations = mapperLocations;
    }

    public void setConfigurationProperties(Properties sqlSessionFactoryProperties) {
        this.configurationProperties = sqlSessionFactoryProperties;
    }

    public void setDataSource(DataSource dataSource) {
        if (dataSource instanceof TransactionAwareDataSourceProxy) {
            this.dataSource = ((TransactionAwareDataSourceProxy) dataSource).getTargetDataSource();
        } else {
            this.dataSource = dataSource;
        }

    }

    public void setTransactionFactory(TransactionFactory transactionFactory) {
        this.transactionFactory = transactionFactory;
    }

    public void setScriptingLanguageDrivers(LanguageDriver... scriptingLanguageDrivers) {
        this.scriptingLanguageDrivers = scriptingLanguageDrivers;
    }

    public void setDefaultScriptingLanguageDriver(Class<? extends LanguageDriver> defaultScriptingLanguageDriver) {
        this.defaultScriptingLanguageDriver = defaultScriptingLanguageDriver;
    }


    public boolean isBanner() {
        return this.banner;
    }

    public void setBanner(final boolean banner) {
        this.banner = banner;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        Assert.notNull(this.dataSource, "Property 'dataSource' is required");
        Assert.state(this.configuration == null && this.configLocation == null || this.configuration == null || this.configLocation == null, "Property 'configuration' and 'configLocation' can not specified with together");
        this.sqlSessionFactory = this.buildSqlSessionFactory();
    }

    protected SqlSessionFactory buildSqlSessionFactory() throws Exception {

        final RabbitConfiguration targetConfiguration;

        RabbitXMLConfigBuilder xmlConfigBuilder = null;
        if (this.configuration != null) {
            targetConfiguration = this.configuration;
            if (targetConfiguration.getVariables() == null) {
                targetConfiguration.setVariables(this.configurationProperties);
            } else if (this.configurationProperties != null) {
                targetConfiguration.getVariables().putAll(this.configurationProperties);
            }
        } else if (this.configLocation != null) {
            xmlConfigBuilder = new RabbitXMLConfigBuilder(this.configLocation.getInputStream(), null, this.configurationProperties);
            targetConfiguration = xmlConfigBuilder.getConfiguration();
        } else {
            LOGGER.debug(() -> "Property 'configuration' or 'configLocation' not specified, using default MyBatis Configuration");
            targetConfiguration = new RabbitConfiguration();
            Optional.ofNullable(this.configurationProperties).ifPresent(targetConfiguration::setVariables);
        }

        this.globalConfig = Optional.ofNullable(this.globalConfig).orElseGet(RabbitConfigUtils::defaults);

        targetConfiguration.setGlobalConfig(this.globalConfig);

        Optional.ofNullable(this.objectFactory).ifPresent(targetConfiguration::setObjectFactory);
        Optional.ofNullable(this.objectWrapperFactory).ifPresent(targetConfiguration::setObjectWrapperFactory);
        Optional.ofNullable(this.vfs).ifPresent(targetConfiguration::setVfsImpl);

        if (hasLength(this.typeAliasesPackage)) {
            scanClasses(this.typeAliasesPackage, this.typeAliasesSuperType).stream()
                    .filter(clazz -> !clazz.isAnonymousClass()).filter(clazz -> !clazz.isInterface())
                    .filter(clazz -> !clazz.isMemberClass()).forEach(targetConfiguration.getTypeAliasRegistry()::registerAlias);
        }

        if (!isEmpty(this.typeAliases)) {
            Stream.of(this.typeAliases).forEach(typeAlias -> {
                targetConfiguration.getTypeAliasRegistry().registerAlias(typeAlias);
                LOGGER.debug(() -> "Registered type alias: '" + typeAlias + "'");
            });
        }

        if (!isEmpty(this.plugins)) {
            Stream.of(this.plugins).forEach(plugin -> {
                targetConfiguration.addInterceptor(plugin);
                LOGGER.debug(() -> "Registered plugin: '" + plugin + "'");
            });
        }

        if (hasLength(this.typeHandlersPackage)) {
            scanClasses(this.typeHandlersPackage, TypeHandler.class).stream().filter(clazz -> !clazz.isAnonymousClass())
                    .filter(clazz -> !clazz.isInterface()).filter(clazz -> !Modifier.isAbstract(clazz.getModifiers()))
                    .filter(clazz -> ClassUtils.getConstructorIfAvailable(clazz) != null)
                    .forEach(targetConfiguration.getTypeHandlerRegistry()::register);
        }

        if (!isEmpty(this.typeHandlers)) {
            Stream.of(this.typeHandlers).forEach(typeHandler -> {
                targetConfiguration.getTypeHandlerRegistry().register(typeHandler);
                LOGGER.debug(() -> "Registered type handler: '" + typeHandler + "'");
            });
        }

        if (!isEmpty(this.scriptingLanguageDrivers)) {
            Stream.of(this.scriptingLanguageDrivers).forEach(languageDriver -> {
                targetConfiguration.getLanguageRegistry().register(languageDriver);
                LOGGER.debug(() -> "Registered scripting language driver: '" + languageDriver + "'");
            });
        }

        Optional.ofNullable(this.defaultScriptingLanguageDriver).ifPresent(targetConfiguration::setDefaultScriptingLanguage);

        if (this.databaseIdProvider != null) {//fix #64 set databaseId before parse mapper xmls
            try {
                targetConfiguration.setDatabaseId(this.databaseIdProvider.getDatabaseId(this.dataSource));
            } catch (SQLException e) {
                throw new NestedIOException("Failed getting a databaseId", e);
            }
        }

        Optional.ofNullable(this.cache).ifPresent(targetConfiguration::addCache);

        if (xmlConfigBuilder != null) {
            try {
                xmlConfigBuilder.parse();
                LOGGER.debug(() -> "Parsed configuration file: '" + this.configLocation + "'");
            } catch (Exception ex) {
                throw new NestedIOException("Failed to parse config resource: " + this.configLocation, ex);
            } finally {
                ErrorContext.instance().reset();
            }
        }

        targetConfiguration.setEnvironment(new Environment(RabbitSqlSessionFactoryBean.class.getSimpleName(),
                this.transactionFactory == null ? new SpringManagedTransactionFactory() : this.transactionFactory,
                this.dataSource));

        if (this.mapperLocations != null) {
            if (this.mapperLocations.length == 0) {
                LOGGER.warn(() -> "Property 'mapperLocations' was specified but matching resources are not found.");
            } else {
                for (Resource mapperLocation : this.mapperLocations) {
                    if (mapperLocation == null) {
                        continue;
                    }
                    try {
                        XMLMapperBuilder xmlMapperBuilder = new XMLMapperBuilder(mapperLocation.getInputStream(),
                                targetConfiguration, mapperLocation.toString(), targetConfiguration.getSqlFragments());
                        xmlMapperBuilder.parse();
                    } catch (Exception e) {
                        throw new NestedIOException("Failed to parse mapping resource: '" + mapperLocation + "'", e);
                    } finally {
                        ErrorContext.instance().reset();
                    }
                    LOGGER.debug(() -> "Parsed mapper file: '" + mapperLocation + "'");
                }
            }
        } else {
            LOGGER.debug(() -> "Property 'mapperLocations' was not specified.");
        }
        final SqlSessionFactory sqlSessionFactory = new RabbitMybatisSqlSessionFactoryBuilder().build(targetConfiguration);

        if (isBanner()) {
            /*System.out.println("\n            _     _     _ _                          _           _   _                  _             ");
            System.out.println("           | |   | |   (_) |                        | |         | | (_)                | |            ");
            System.out.println("  _ __ __ _| |__ | |__  _| |_ ______ _ __ ___  _   _| |__   __ _| |_ _ ___ ______ _ __ | |_   _  __ _ ");
            System.out.println(" | '__/ _` | '_ \\| '_ \\| | __|______| '_ ` _ \\| | | | '_ \\ / _` | __| / __|______| '_ \\| | | | |/ _` |");
            System.out.println(" | | | (_| | |_) | |_) | | |_       | | | | | | |_| | |_) | (_| | |_| \\__ \\      | |_) | | |_| | (_| |");
            System.out.println(" |_|  \\__,_|_.__/|_.__/|_|\\__|      |_| |_| |_|\\__, |_.__/ \\__,_|\\__|_|___/      | .__/|_|\\__,_|\\__, |");
            System.out.println("                                                __/ |                            | |             __/ |");
            System.out.println("                                               |___/                             |_|            |___/ ");*/

            System.out.println("                 _           _   _                      _     _     _ _                _             \n" +
                    "                | |         | | (_)                    | |   | |   (_) |              | |            \n" +
                    " _ __ ___  _   _| |__   __ _| |_ _ ___ ______ _ __ __ _| |__ | |__  _| |_ ______ _ __ | |_   _  __ _ \n" +
                    "| '_ ` _ \\| | | | '_ \\ / _` | __| / __|______| '__/ _` | '_ \\| '_ \\| | __|______| '_ \\| | | | |/ _` |\n" +
                    "| | | | | | |_| | |_) | (_| | |_| \\__ \\      | | | (_| | |_) | |_) | | |_       | |_) | | |_| | (_| |\n" +
                    "|_| |_| |_|\\__, |_.__/ \\__,_|\\__|_|___/      |_|  \\__,_|_.__/|_.__/|_|\\__|      | .__/|_|\\__,_|\\__, |\n" +
                    "            __/ |                                                               | |             __/ |\n" +
                    "           |___/                                                                |_|            |___/ \n");

//            System.out.println(Ansi.ansi().fg(Ansi.Color.GREEN).a(":: rabbit-mybatis-plug ::") + ("   (v" + MybatisPlusVersion.getVersion() + ")\n"));
            System.out.println(" (v " + RabbitMybatisPlugVersion.getVersion() + ")\n");
        }

        return sqlSessionFactory;
    }

    @Override
    public SqlSessionFactory getObject() throws Exception {
        if (this.sqlSessionFactory == null) {
            this.afterPropertiesSet();
        }

        return this.sqlSessionFactory;
    }

    @Override
    public Class<? extends SqlSessionFactory> getObjectType() {
        return this.sqlSessionFactory == null ? SqlSessionFactory.class : this.sqlSessionFactory.getClass();
    }

    @Override
    public boolean isSingleton() {
        return true;
    }

    @Override
    public void onApplicationEvent(ApplicationEvent event) {
        if (this.failFast && event instanceof ContextRefreshedEvent) {
            this.sqlSessionFactory.getConfiguration().getMappedStatementNames();
        }

    }

    private Set<Class<?>> scanClasses(String packagePatterns, Class<?> assignableType) throws IOException {
        Set<Class<?>> classes = new HashSet();
        String[] packagePatternArray = StringUtils.tokenizeToStringArray(packagePatterns, ",; \t\n");
        String[] var5 = packagePatternArray;
        int var6 = packagePatternArray.length;

        for (int var7 = 0; var7 < var6; ++var7) {
            String packagePattern = var5[var7];
            Resource[] resources = RESOURCE_PATTERN_RESOLVER.getResources("classpath*:" + ClassUtils.convertClassNameToResourcePath(packagePattern) + "/**/*.class");
            Resource[] var10 = resources;
            int var11 = resources.length;

            for (int var12 = 0; var12 < var11; ++var12) {
                Resource resource = var10[var12];

                try {
                    ClassMetadata classMetadata = METADATA_READER_FACTORY.getMetadataReader(resource).getClassMetadata();
                    Class<?> clazz = Resources.classForName(classMetadata.getClassName());
                    if (assignableType == null || assignableType.isAssignableFrom(clazz)) {
                        classes.add(clazz);
                    }
                } catch (Throwable var16) {
                    LOGGER.warn(() -> {
                        return "Cannot load the '" + resource + "'. Cause by " + var16.toString();
                    });
                }
            }
        }

        return classes;
    }

    public void setGlobalConfig(final RabbitConfig globalConfig) {
        this.globalConfig = globalConfig;
    }
}
