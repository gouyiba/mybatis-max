package com.rabbit;

import cn.hutool.json.JSONUtil;
import com.rabbit.core.constructor.QueryWrapper;
import com.rabbit.core.constructor.UpdateWrapper;
import com.rabbit.entity.User;
import com.rabbit.enumatioon.Sex;
import com.rabbit.mapper.AccountMapper;
import com.rabbit.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

@SpringBootTest
class MapperInjectorTests {

    @Autowired
    private UserService userService;

    @Resource
    private AccountMapper accountMapper;

    @Test
    void testBaseService() {

        User user=new User();
        user.setSex(Sex.WOMEN);
        user.setStuName("罗志祥");
        user.setStuAge(40);

        UpdateWrapper wrapper=new UpdateWrapper();
        wrapper.where("stu_uid","5e182bf8de7cf5871d904dff").
                where("stu_name","马化腾").
                where("stu_age",111);
        long result=userService.updateObject(wrapper);
        if(result>0){
            System.out.println("修改成功");
        }
        /*accountMapper.findById("1");
        // 单实例查询
        User user = userService.queryObject(new QueryWrapper().
                where("sex", 1).
                like("stu_name", "马").
                where("stu_age", 111), User.class);
        System.out.println("query result -> " + JSONUtil.toJsonStr(user));*/

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
    }
}
