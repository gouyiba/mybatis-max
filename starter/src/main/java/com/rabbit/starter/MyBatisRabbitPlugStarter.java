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

import java.util.ArrayList;
import java.util.List;

@ComponentScan(basePackages = {
        "com.rabbit.starter",
        "com.rabbit.core.service.impl"
})
@MapperScan("com.rabbit.core.mapper")
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
        List<User> users=new ArrayList<>();
        for (int i=0;i<1955;i++){
            User user=new User();
            //user.setStuUid("duxiaoyu"+i);
            user.setStuAge(i);
            user.setStuName("杜晓宇"+i);
            user.setSex(Sex.MAN.getValue());
            users.add(user);
        }
        System.out.println(baseService.addBatchObject(users));
    }

    @GetMapping("/test2")
    public void test2(){
//        User user=new User();
//        user.setStuUid("5e182bf8de7cf5871d904a0e");
//        user.setStuName("马化腾");
//        user.setStuAge(40);
//        System.out.println(baseService.updateObject(user));

        String[] stuid={
//                "5e182bf8de7cf5871d904a0f",
//                "5e182bf8de7cf5871d904a10"
  //              "5e182bf8de7cf5871d904a11"
//                "5e182bf8de7cf5871d904a12",
//                "5e182bf8de7cf5871d904a13",
                "5e182bf8de7cf5871d904a14",
                "5e182bf8de7cf5871d904a15",
                "5e182bf8de7cf5871d904a16",
                "5e182bf8de7cf5871d904a17",
                "5e182bf8de7cf5871d904a18",
                "5e182bf8de7cf5871d904a19",
                "5e182bf8de7cf5871d904a1a"
        };

        List<User> users=new ArrayList<>();
        for (int i=601;i<=2000;i++){
            User user=new User();
            user.setId(i);
            user.setStuName("马化腾");
            user.setStuAge(i);
            users.add(user);
        }
        System.out.println(baseService.updateBatchByIdObject(users));
    }

}