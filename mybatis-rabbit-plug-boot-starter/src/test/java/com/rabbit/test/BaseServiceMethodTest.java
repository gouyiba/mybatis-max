package com.rabbit.test;

import cn.hutool.json.JSONUtil;
import com.rabbit.core.constructor.QueryWrapper;
import com.rabbit.core.constructor.UpdateWrapper;
import com.rabbit.entity.User;
import com.rabbit.enumatioon.Sex;
import com.rabbit.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @ClassName BaseServiceMethodTest
 * @Author duxiaoyu
 * @Date 2020/6/9 14:20
 * @Version 1.0
 */
@SpringBootTest
@Slf4j
public class BaseServiceMethodTest {

    @Autowired
    private UserService userService;

    /************************************* BaseService中所有CRUD均可操作不同entity *************************************/

    @Test
    public void addObject() {
        // 新增，自动生成uuid主键
        User user = new User();
        user.setStuName("小王");
        user.setStuAge(20);
        user.setSex(Sex.MAX);

        String result = userService.addObject(user);
        log.info("addObject==>" + result);
    }

    @Test
    public void addBatchObject() {
        // 批量新增，自动生成uuid主键
        List<User> userList = Stream.of(
                new User(null, "王五", 20, Sex.WOMEN),
                new User(null, "张三", 21, Sex.MAX),
                new User(null, "李四", 22, Sex.MAX)
        ).collect(Collectors.toList());

        Long result = userService.addBatchObject(userList);
        log.info(result > 0 ? "addBatchObject success..." : "addBatchObject failed...");
    }

    @Test
    public void updateObject() {
        // 设置要修改的字段
        User user = new User(null, "冯六", 30, Sex.MAX);

        // 自定义修改条件
        UpdateWrapper wrapper = new UpdateWrapper();
        wrapper.where("stu_name", "王五");
        wrapper.where("stu_uid", "d55f5e31d4e447d28168c52d754daf96");
        wrapper.isNotNull("sex");

        Long result = userService.updateObject(user, wrapper);
        log.info(result > 0 ? "updateObject success..." : "updateObject failed...");
    }

    @Test
    public void updateBatchByIdObject() {
        // 根据主键批量修改
        List<User> userList = Stream.of(
                new User("d55f5e31d4e447d28168c52d754daf96", "王七", 20, Sex.WOMEN),
                new User("45e25160086f413aa3bbc5b2919b3c84", "张二", 21, Sex.MAX),
                new User("87759a8d385749c3afdda1f6b9222fca", "李三", 22, Sex.MAX)
        ).collect(Collectors.toList());

        Long result = userService.updateBatchByIdObject(userList);
        log.info(result > 0 ? "updateBatchByIdObject success..." : "updateBatchByIdObject failed...");
    }

    @Test
    public void deleteObject() {
        /*// 逻辑删除
        Long result=userService.deleteObject("87759a8d385749c3afdda1f6b9222fca",User.class);
        log.info(result>0?"deleteObject success...":"deleteObject failed...");*/

        // 物理删除
        Long result = userService.deleteObject("87759a8d385749c3afdda1f6b9222fca", User.class);
        log.info(result > 0 ? "deleteObject success..." : "deleteObject failed...");
    }

    @Test
    public void deleteBatchByIdObject() {
        /*// 批量逻辑删除
        Long result=userService.deleteBatchByIdObject(Arrays.asList("45e25160086f413aa3bbc5b2919b3c84","87759a8d385749c3afdda1f6b9222fca"),User.class);
        log.info(result>0?"deleteObject success...":"deleteObject failed...");*/

        // 批量物理删除
        Long result = userService.deleteBatchByIdObject(Arrays.asList("5e182bf8de7cf5871d904e0c", "5e182bf8de7cf5871d904e0b"), User.class);
        log.info(result > 0 ? "deleteObject success..." : "deleteObject failed...");
    }

    @Test
    public void queryObject() {
        // 单实例查询
        User user = userService.queryObject(new QueryWrapper().
                where("sex", 2).
                where("id", 1025).
                like("stu_name", "北京").
                where("stu_age", 19), User.class);
        log.info("query result -> " + JSONUtil.toJsonStr(user));
    }

    @Test
    public void queryObjectList() {
        // 多实例查询
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.
                like("stu_name", "北京").
                between("id", 4731, 4733).
                in("stu_uid", "5e182bf8de7cf5871d905c87", "5e182bf8de7cf5871d905c88", "5e182bf8de7cf5871d905c89", "5e182bf8de7cf5871d905c8a").
                orderBy("id", QueryWrapper.DESC).limit(0, 2);
        List<User> userList = userService.queryObjectList(queryWrapper, User.class);
        log.info("query result -> " + JSONUtil.toJsonStr(userList));
    }

    @Test
    public void queryObjectById() {
        // 主键查询
        User user = userService.queryObjectById("5e182bf8de7cf5871d905c87", User.class);
        log.info("query result -> " + JSONUtil.toJsonStr(user));
    }

    @Test
    public void queryCustomSql() {
        // 自定义sql查询
        // SELECT count(1) as `num` FROM t_user
        // SELECT now()
        List<Map<String, Object>> result = userService.queryCustomSql("SELECT now()");
        log.info("query result -> " + JSONUtil.toJsonStr(result.get(0)));
    }
}
