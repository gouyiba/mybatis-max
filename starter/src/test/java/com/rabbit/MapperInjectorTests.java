package com.rabbit;

import cn.hutool.json.JSON;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONUtil;
import com.rabbit.core.constructor.QueryWrapper;
import com.rabbit.core.entity.User;
import com.rabbit.core.mapper.AccountMapper;
import com.rabbit.core.mapper.UserMapper;
import com.rabbit.core.service.BaseService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.util.*;

@SpringBootTest
class MapperInjectorTests {

    @Resource
    private UserMapper userMapper;

    @Resource
    private AccountMapper accountMapper;

    @Autowired
    private BaseService baseService;

    //@Test
    void testMapperInjector() throws Exception {
        // rmp function
        /*List<User> testList1 = userMapper.list();

        System.out.println("size: " + testList1.size());

        testList1.forEach(user -> System.out.println("id: " + user.getId() + " type: " + user.getType()));

        Integer count = userMapper.count();

        System.out.println("size: " + count);

        // mybatis function
        User user = Optional.ofNullable(accountMapper.findById("1"))
                .orElseThrow(() -> new Exception());
        System.out.println("type: " + user.getType());*/
    }

    @Test
    void testBaseService(){

        // 单实例查询
        /*User user=baseService.queryObject(new QueryWrapper().
                where("sex",2).
                like("stu_name","马").
                where("stu_age",1011),User.class);
        System.out.println("query result -> "+JSONUtil.toJsonStr(user));*/

        // 多实例查询 1011,1012,1013,1014,1015,1016,1017,1018
        /*QueryWrapper queryWrapper=new QueryWrapper();
        queryWrapper.
                like("stu_name","马").
                between("stu_age",1011,1015).
                in("stu_age",1011,1012,1013,1014,1015,1016,1017,1018).
                orderBy("stu_age",QueryWrapper.DESC).limit(0,2);
        List<User> userList=baseService.queryObjectList(queryWrapper,User.class);
        System.out.println("query result -> "+JSONUtil.toJsonStr(userList));*/

        // 主键查询
       /* User user=baseService.queryObjectById("5e182bf8de7cf5871d904dff",User.class);
        System.out.println("query result -> "+JSONUtil.toJsonStr(user));*/

        // 自定义sql查询
        /*List<Map<String,Object>> result=baseService.queryCustomSql("SELECT count(1) as `num` FROM t_user");
        System.out.println(JSONUtil.toJsonStr(result.get(0)));*/

        // 批量新增
        /*List<User> users=new ArrayList<>();
        for (int i=0;i<1955;i++){
            User user=new User();
            user.setStuAge(i);
            user.setStuName("马云"+i);
            users.add(user);
        }
        System.out.println(baseService.addBatchObject(users));*/

        // 根据主键批量修改
        /*String[] stuId={
                "5e182bf8de7cf5871d904dff",
                "5e182bf8de7cf5871d904e00",
                "5e182bf8de7cf5871d904e01",
                "5e182bf8de7cf5871d904e02",
                "5e182bf8de7cf5871d904e03"
        };
        List<User> users=new ArrayList<>();
        for (int i=0;i<stuId.length;i++){
            User user=new User();
            user.setStuUid(stuId[i]);
            user.setStuName("马化腾");
            user.setStuAge(111);
            users.add(user);
        }
        System.out.println(baseService.updateBatchByIdObject(users));*/

        // 根据主键批量删除
        /*String[] stuId={
                "5e182bf8de7cf5871d9055da",
                "5e182bf8de7cf5871d9055db",
                "5e182bf8de7cf5871d9055dc",
                "5e182bf8de7cf5871d9055dd",
                "5e182bf8de7cf5871d9055de"
        };
        System.out.println(baseService.deleteBatchByIdObject(Arrays.asList(stuId),User.class));*/

    }
}
