package com.rabbit.autoconfigure;

import com.rabbit.core.config.RabbitConfig;
import com.rabbit.core.module.RabbitConfiguration;
import com.rabbit.core.util.RabbitConfigUtils;
import org.apache.ibatis.scripting.LanguageDriver;
import org.apache.ibatis.session.ExecutorType;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;

import java.io.IOException;
import java.util.Optional;
import java.util.Properties;
import java.util.stream.Stream;

/**
 * this class by created wuyongfei on 2020/5/5 16:22
 **/
@ConfigurationProperties(
        prefix = "rabbit-mybatis-plug"
)
public class RabbitProperties {
    public static final String RABBIT_MYBATIS_PLUG_PREFIX = "rabbit-mybatis-plug";
    private static final ResourcePatternResolver resourceResolver = new PathMatchingResourcePatternResolver();
    private String configLocation;
    private String[] mapperLocations = new String[]{"classpath*:/mapper/**/*.xml"};
    private String typeAliasesPackage;
    private Class<?> typeAliasesSuperType;
    private String typeHandlersPackage;
    private boolean checkConfigLocation = false;
    private ExecutorType executorType;
    private Class<? extends LanguageDriver> defaultScriptingLanguageDriver;
    private Properties configurationProperties;
    @NestedConfigurationProperty
    private RabbitConfiguration configuration;
    private RabbitConfig config = RabbitConfigUtils.defaults();
    private boolean banner = true;

    public RabbitProperties() {
    }

    public String getConfigLocation() {
        return this.configLocation;
    }

    public RabbitProperties setConfigLocation(final String configLocation) {
        this.configLocation = configLocation;
        return this;
    }

    public String[] getMapperLocations() {
        return this.mapperLocations;
    }

    public RabbitProperties setMapperLocations(final String[] mapperLocations) {
        this.mapperLocations = mapperLocations;
        return this;
    }

    public String getTypeAliasesPackage() {
        return this.typeAliasesPackage;
    }

    public Class<?> getTypeAliasesSuperType() {
        return this.typeAliasesSuperType;
    }

    public String getTypeHandlersPackage() {
        return this.typeHandlersPackage;
    }

    public boolean isCheckConfigLocation() {
        return this.checkConfigLocation;
    }

    public ExecutorType getExecutorType() {
        return this.executorType;
    }

    public Class<? extends LanguageDriver> getDefaultScriptingLanguageDriver() {
        return this.defaultScriptingLanguageDriver;
    }

    public Properties getConfigurationProperties() {
        return this.configurationProperties;
    }

    public RabbitConfiguration getConfiguration() {
        return this.configuration;
    }

    public RabbitProperties setTypeAliasesPackage(final String typeAliasesPackage) {
        this.typeAliasesPackage = typeAliasesPackage;
        return this;
    }

    public RabbitProperties setTypeAliasesSuperType(final Class<?> typeAliasesSuperType) {
        this.typeAliasesSuperType = typeAliasesSuperType;
        return this;
    }

    public RabbitProperties setTypeHandlersPackage(final String typeHandlersPackage) {
        this.typeHandlersPackage = typeHandlersPackage;
        return this;
    }

    public RabbitProperties setCheckConfigLocation(final boolean checkConfigLocation) {
        this.checkConfigLocation = checkConfigLocation;
        return this;
    }

    public RabbitProperties setExecutorType(final ExecutorType executorType) {
        this.executorType = executorType;
        return this;
    }

    public RabbitProperties setDefaultScriptingLanguageDriver(final Class<? extends LanguageDriver> defaultScriptingLanguageDriver) {
        this.defaultScriptingLanguageDriver = defaultScriptingLanguageDriver;
        return this;
    }

    public RabbitProperties setConfigurationProperties(final Properties configurationProperties) {
        this.configurationProperties = configurationProperties;
        return this;
    }

    public RabbitProperties setConfiguration(final RabbitConfiguration configuration) {
        this.configuration = configuration;
        return this;
    }

    public RabbitConfig getGlobalConfig() {
        return this.config;
    }

    public RabbitProperties setGlobalConfig(final RabbitConfig config) {
        this.config = config;
        return this;
    }

    public boolean isBanner() {
        return this.banner;
    }

    public RabbitProperties setBanner(final boolean banner) {
        this.banner = banner;
        return this;
    }

    public Resource[] resolveMapperLocations() {
        return Stream.of((String[]) Optional.ofNullable(this.mapperLocations)
                .orElse(new String[0]))
                .flatMap((location) -> Stream.of(this.getResources(location))
        ).toArray((x$0) -> new Resource[x$0]);
    }

    private Resource[] getResources(String location) {
        try {
            return resourceResolver.getResources(location);
        } catch (IOException var3) {
            return new Resource[0];
        }
    }
}
