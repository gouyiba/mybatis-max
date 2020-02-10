package com.rabbit.starter;

import cn.hutool.json.JSONUtil;
import com.rabbit.core.constructor.QueryWrapper;
import com.rabbit.core.service.BaseService;
import com.rabbit.starter.bean.User;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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


    /**
     * 新增测试
     */
    @GetMapping("/test1")
    public void test(){
        List<User> users=new ArrayList<>();
        for (int i=0;i<1955;i++){
            User user=new User();
            //user.setStuUid("duxiaoyu"+i);
            user.setStuAge(i);
            user.setStuName("杜晓宇"+i);
            //user.setSex(Sex.MAN.getValue());
            users.add(user);
        }
        System.out.println(baseService.addBatchObject(users));
    }

    /**
     * 修改测试
     */
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
        for (int i=2011;i<=2020;i++){
            User user=new User();
            user.setId(i);
            user.setStuName("马化腾");
            user.setStuAge(i);
            users.add(user);
        }
        System.out.println(baseService.updateBatchByIdObject(users));
    }

    /**
     * 删除测试
     */
    @GetMapping("/test3")
    public void test3(){
        //System.out.println(baseService.deleteObject("5e182bf8de7cf5871d904a0d",User.class));
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

        List<Object> objects=new ArrayList<>();
        for (int i=1001;i<=1010;i++){
            objects.add(i);
        }
        System.out.println(baseService.deleteBatchByIdObject(objects,User.class));
    }

    /**
     * 查询测试
     */
    @GetMapping("/test4")
    public void test4(){
        // 单实例查询
        /*User user=baseService.queryObject(new QueryWrapper().
                where("sex",0).
                like("stu_name","马").
                where("stu_age",1001),User.class);
        System.out.println("query result -> "+JSONUtil.toJsonStr(user));*/

        // 多实例查询
       /* QueryWrapper queryWrapper=new QueryWrapper();
        queryWrapper.
                where("sex",0).
                like("stu_name","马").
                between("stu_age",1001,1003).
                in("stu_age",1001,1002,1003,1004,1005,1006,1007,1008).
                orderBy("stu_age",QueryWrapper.DESC).limit(0,2);
        List<User> userList=baseService.queryObjectList(queryWrapper,User.class);
        System.out.println("query result -> "+JSONUtil.toJsonStr(userList));*/

       // 主键查询
        /*User user=baseService.queryObjectById(1001,User.class);
        System.out.println("query result -> "+JSONUtil.toJsonStr(user));*/

        // 自定义sql查询
        List<Map<String,Object>> result=baseService.queryCustomSql("SELECT count(1) as `num` FROM t_user");
        System.out.println(JSONUtil.toJsonStr(result.get(0)));
    }

}