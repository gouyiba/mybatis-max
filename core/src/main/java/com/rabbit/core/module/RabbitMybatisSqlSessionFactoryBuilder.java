package com.rabbit.core.module;

import org.apache.ibatis.exceptions.ExceptionFactory;
import org.apache.ibatis.executor.ErrorContext;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.util.Properties;

/**
 * this class by created wuyongfei on 2020/5/10 16:10
 **/
public class RabbitMybatisSqlSessionFactoryBuilder extends SqlSessionFactoryBuilder {
    public RabbitMybatisSqlSessionFactoryBuilder() {
    }

    public SqlSessionFactory build(Reader reader, String environment, Properties properties) {
        SqlSessionFactory factory;
        try {
            RabbitXMLConfigBuilder parser = new RabbitXMLConfigBuilder(reader, environment, properties);
            factory = this.build(parser.parse());
        } catch (Exception var14) {
            throw ExceptionFactory.wrapException("Error building SqlSession.", var14);
        } finally {
            ErrorContext.instance().reset();
            try {
                reader.close();
            } catch (IOException var13) {
                ;
            }
        }
        return factory;
    }

    public SqlSessionFactory build(InputStream inputStream, String environment, Properties properties) {
        SqlSessionFactory factory;
        try {
            RabbitXMLConfigBuilder parser = new RabbitXMLConfigBuilder(inputStream, environment, properties);
            factory = this.build(parser.parse());
        } catch (Exception var14) {
            throw ExceptionFactory.wrapException("Error building SqlSession.", var14);
        } finally {
            ErrorContext.instance().reset();

            try {
                inputStream.close();
            } catch (IOException var13) {
                ;
            }

        }

        return factory;
    }

    public SqlSessionFactory build(Configuration config) {
        RabbitConfiguration configuration = (RabbitConfiguration)config;
        SqlSessionFactory factory = super.build(configuration);
        return factory;
    }
}
