## MyBatis-Max

[![Maven Central](https://img.shields.io/maven-central/v/cloud.gouyiba/mybatis-max?color=yellow)](https://search.maven.org/artifact/cloud.gouyiba/mybatis-max/)
[![GitHub release](https://img.shields.io/maven-central/v/cloud.gouyiba/mybatis-max?color=green&label=release)](https://github.com/gouyiba/mybatis-max/releases)
[![License](https://img.shields.io/badge/license-Apache%202-4EB1BA.svg)](https://www.apache.org/licenses/LICENSE-2.0.html)
[![Gitpod Ready-to-Code](https://img.shields.io/badge/Gitpod-Ready--to--Code-blue?logo=gitpod)](https://gitpod.io/#https://github.com/gouyiba/mybatis-max) 
[![Issues](https://img.shields.io/github/issues/gouyiba/mybatis-max)](https://github.com/gouyiba/mybatis-max/issues) 
## What is MyBatis-Max?
Mybatis Max is to simplify and solve the tedious development work of the data layer based on Mybatis, greatly improve the development efficiency, and enable developers to focus on the implementation of the Service business, and do not need to spend too much energy on the development of the data layer.

## Get Step
- Add MyBatis-Plus dependency
    - Maven:
        ```xml
        <dependency>
            <groupId>cloud.gouyiba</groupId>
            <artifactId>mybatis-max-boot-starter</artifactId>
            <version>1.0.5-RELEASE</version>
        </dependency>
        ```
    - Gradle:
        ``` groovy
        compile group: 'cloud.gouyiba', name: 'mybatis-max-boot-starter', version: '1.0.5-RELEASE'
        ```

-   Modify mapper file extends BaseMapper interface
    ```java
    public interface AccountMapper extends BaseMapper<Account> {

    }
    ```
-   Use it
    -   Example Code:
        ```java
        @Resource
        private AccountMapper accountMapper;
        
        public void getMybatisMaxFunction() {
            accountMapper.selectById();
            accountMapper.selectOne();
            accountMapper.insert();
            accountMapper.update();
            accountMapper.updateById();
            accountMapper.delete();
            // accountMapper.method()......
        }
        ```
    -   All the injection methods provided in BaseMapper are accessible through an AccountMapper agent instance

## Dependency
Please see this <a href='https://mvnrepository.com/artifact/cloud.gouyiba/mybatis-max-boot-starter'>[China Maven Download Page]</a> for more repository infos.

## Welcome
We invite interested people to join our gouyiba development team communicate together and let us know by sending an issue.

### *License*

MyBatis-Max is released under the [Apache 2.0 license](license.txt).