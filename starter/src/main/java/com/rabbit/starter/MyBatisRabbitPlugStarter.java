package com.rabbit.starter;

import com.rabbit.core.service.BaseService;
import com.rabbit.starter.bean.User;
import com.rabbit.core.enumation.Sex;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@ComponentScan(basePackages = {
        "com.rabbit.starter",
        "com.rabbit.core.service.impl"
})
@MapperScan("com.rabbit.starter.dao")
@SpringBootApplication
@Controller
public class MyBatisRabbitPlugStarter {

    @Autowired
    private BaseService baseService;

    public static void main(String[] args) {
        SpringApplication.run(MyBatisRabbitPlugStarter.class, args);
    }

    @GetMapping("/test1")
    public void test(){
        User user=new User();
        user.setStuAge(23);
        user.setStuName("杜晓宇");
        user.setSex(Sex.MAN.MAN);
        System.out.println(baseService.addObject(user));
    }

}