package cloud.gouyiba.test;

import cloud.gouyiba.core.constructor.QueryWrapper;
import cloud.gouyiba.core.constructor.UpdateWrapper;
import cloud.gouyiba.entity.User;
import cloud.gouyiba.enumatioon.Sex;
import cloud.gouyiba.service.UserService;
import cn.hutool.json.JSONUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
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

    /************************************* BaseService中所有CRUD均可操作不同entity，以下均为测试数据 *************************************/

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
        String id = beForById();

        // 设置要修改的字段
        User user = new User(null, "冯六", 30, Sex.MAX);

        // 自定义修改条件
        UpdateWrapper wrapper = new UpdateWrapper();
        wrapper.where("stu_uid", id);
        wrapper.isNotNull("sex");

        Long result = userService.updateObject(user, wrapper);
        log.info(result > 0 ? "updateObject success..." : "updateObject failed...");
    }

    @Test
    public void updateBatchByIdObject() {
        List<String> ids = beForByIdBatch();
        List<User> userList = new ArrayList<>();

        ids.forEach(x -> userList.add(new User(x, "王七", 20, Sex.MAX)));

        // 根据主键批量修改
        Long result = userService.updateBatchByIdObject(userList);
        log.info(result > 0 ? "updateBatchByIdObject success..." : "updateBatchByIdObject failed...");
    }

    @Test
    public void deleteObject() {
        String id = beForById();

        // 逻辑删除
        Long result = userService.deleteObject(id, User.class);
        log.info(result > 0 ? "deleteObject success..." : "deleteObject failed...");

        // 物理删除
        /*Long result = userService.deleteObject("87759a8d385749c3afdda1f6b9222fca", User.class);
        log.info(result > 0 ? "deleteObject success..." : "deleteObject failed...");*/
    }

    @Test
    public void deleteBatchByIdObject() {
        List<Object> delIds = beForByIdBatch().stream().map(x -> x).collect(Collectors.toList());
        // 批量逻辑删除
        Long result = userService.deleteBatchByIdObject(delIds, User.class);
        log.info(result > 0 ? "deleteObject success..." : "deleteObject failed...");

        // 批量物理删除
        /*Long result = userService.deleteBatchByIdObject(delIds, User.class);
        log.info(result > 0 ? "deleteObject success..." : "deleteObject failed...");*/
    }

    @Test
    public void queryObject() {
        String id = beForById();

        // 单实例查询
        User user = userService.queryObject(new QueryWrapper().
                where("sex", 1).
                like("stu_name", "小赵").
                where("stu_uid", id).
                where("stu_age", 20), User.class);
        log.info("query result -> " + JSONUtil.toJsonStr(user));
    }

    @Test
    public void queryObjectList() {
        List<String> ids = beForByIdBatch();

        // 多实例查询
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.
                like("stu_name", "小赵").
                in("stu_uid", ids.toArray()).
                orderBy("id", QueryWrapper.DESC).limit(1, 2);
        List<User> userList = userService.queryObjectList(queryWrapper, User.class);
        log.info("query result -> " + JSONUtil.toJsonStr(userList));
    }

    @Test
    public void queryObjectById() {
        String id = beForById();

        // 主键查询
        User user = userService.queryObjectById(id, User.class);
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

    public String beForById() {
        // 新增，自动生成uuid主键
        User user = new User();
        user.setStuName("小赵");
        user.setStuAge(20);
        user.setSex(Sex.MAX);

        String result = userService.addObject(user);
        return result;
    }

    public List<String> beForByIdBatch() {
        List<String> ids = new ArrayList<>();
        // 目前mybatis-max中继承BaseService后，批量新增暂时无法返回自动生成的主键id，以下数据生成采用单实例新增测试
        for (int i = 0; i < 5; i++) {
            // 新增，自动生成uuid主键
            User user = new User();
            user.setStuName("小赵" + i);
            user.setStuAge(i);
            if (i % 2 == 0) {
                user.setSex(Sex.MAX);
            } else {
                user.setSex(Sex.WOMEN);
            }
            String result = userService.addObject(user);
            if (StringUtils.isNotBlank(result)) ids.add(result);
        }
        return ids;
    }
}
