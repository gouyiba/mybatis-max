package com.rabbit;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 开发阶段导入，应用阶段移除
 */
@MapperScan("com.rabbit.mapper")
@SpringBootApplication
public class MyBatisRabbitPlugStarter {

    public static void main(String[] args) {
        SpringApplication.run(MyBatisRabbitPlugStarter.class, args);
    }
}