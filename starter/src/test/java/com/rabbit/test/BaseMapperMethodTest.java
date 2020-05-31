package com.rabbit.test;

import com.rabbit.core.constructor.DeleteWrapper;
import com.rabbit.core.constructor.UpdateWrapper;
import com.rabbit.entity.Account;
import com.rabbit.entity.User;
import com.rabbit.enumatioon.Sex;
import com.rabbit.mapper.AccountMapper;
import com.rabbit.mapper.UserMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.Assert;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@SpringBootTest
public class BaseMapperMethodTest {

    @Resource
    private AccountMapper accountMapper;

    @Autowired
    private UserMapper userMapper;

    @Test
    public void testInsertMethod() {
        /*Account account = new Account();
        account.setId(UUID.randomUUID().toString());
        account.setUserName("root");
        account.setPass(null);
        account.setCreatedBy(UUID.randomUUID().toString());
        Assert.isTrue(accountMapper.insert(account) > 0, "insert failed.");*/

        /*User user1=new User();
        user1.setStuUid("5e182bf8de7cf5871d904e06");
        user1.setStuName("superAdmin");
        user1.setStuAge(23);
        user1.setSex(Sex.MAX);

        User user2=new User();
        user2.setStuUid("5e182bf8de7cf5871d904e07");
        user2.setStuName("superAdmin13");
        user2.setStuAge(13);
        user2.setSex(Sex.WOMEN);

        User user3=new User();
        user3.setStuUid("5e182bf8de7cf5871d904e08");
        user3.setStuName("superAdmin15");
        user3.setStuAge(15);
        user3.setSex(Sex.MAX);
        List<User> userList= Arrays.asList(user1,user2,user3);*/
        /*int result=userMapper.insert(user1);
        int result2=userMapper.insert(user2);
        int result3=userMapper.insert(user3);*/
        // int result=userMapper.insertBatch(userList);
        // System.out.println("执行结果："+result);

        //int result=userMapper.deleteById("5e182bf8de7cf5871d904dff");
        //int result=userMapper.deleteBatchById(Arrays.asList("5e182bf8de7cf5871d904e00","5e182bf8de7cf5871d904e01","5e182bf8de7cf5871d904e02"));
        //int result=userMapper.delete((DeleteWrapper) new DeleteWrapper().where("stu_uid","5e182bf8de7cf5871d904e04").isNotNull("stu_uid"));

       /* User user=new User();
        user.setStuUid("5e182bf8de7cf5871d904e05");
        user.setStuName("superAdmin13");
        user.setStuAge(13);
        user.setSex(Sex.WOMEN);
        int result=userMapper.updateById(user);*/

       //int result=userMapper.updateBatchById(userList);

       /* User user=new User();
        //user.setStuUid("5e182bf8de7cf5871d904e05");
        user.setStuName("superAdmin1234");
        user.setStuAge(45);
        user.setSex(Sex.MAX);

        UpdateWrapper updateWrapper=new UpdateWrapper();
        updateWrapper.where("stu_uid","5e182bf8de7cf5871d904e05");
        updateWrapper.isNotNull("stu_uid");
        updateWrapper.setEntity(user);
        int result=userMapper.update(updateWrapper);*/
    }
}
