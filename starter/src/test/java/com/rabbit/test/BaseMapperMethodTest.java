package com.rabbit.test;

import com.rabbit.core.constructor.QueryWrapper;
import com.rabbit.entity.Account;
import com.rabbit.mapper.AccountMapper;
import com.rabbit.mapper.UserMapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.Assert;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@SpringBootTest
@Slf4j
public class BaseMapperMethodTest {

    @Resource
    private AccountMapper accountMapper;

    @Autowired
    private UserMapper userMapper;

    private static List<String> SYS_UUID_CONTAINER = new ArrayList<>();

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

    @Test
    public void testSelectByIdMethod() {
        insert();
        Account accountObj = accountMapper.selectById(SYS_UUID_CONTAINER.get(0));
        Assert.isTrue(!ObjectUtils.isEmpty(accountObj), "select by id failed.");
        log.info(accountObj.toString());
    }

    @Test
    public void testSelectByIdsMethod() {
        insert();
        insert();
        List<Account> accountListByIds = accountMapper.selectBatchIds(SYS_UUID_CONTAINER);
        Assert.isTrue(accountListByIds.size() > 0, "select by ids failed.");
        log.info(accountListByIds.toString());
    }

    @Test
    public void testSelectCountMethod() {
        insert();
        // where
        Integer whereCount = accountMapper.selectCount(new QueryWrapper<>().where("1", 1));
        Assert.isTrue(whereCount > 0, "select count failed.");
        // like
        Integer likeCount = accountMapper.selectCount(new QueryWrapper<>().like("1", 1));
        Assert.isTrue(likeCount > 0, "select count failed.");
        // or
        Integer orCount = accountMapper.selectCount(new QueryWrapper<>().or("1", 1).or("2", 2));
        Assert.isTrue(orCount > 0, "select count failed.");
        // in
        Integer inCount = accountMapper.selectCount(new QueryWrapper<>().in("id", 1, 2, 3));
        Assert.isTrue(inCount > 0, "select count failed.");
    }

    @Test
    public void testSelectListMethod() {
        insert();
        // empty param
        List<Account> emptyParamAccountList = accountMapper.selectList(null);
        Assert.isTrue(emptyParamAccountList.size() > 0, "select list failed.");
        // param
        List<Account> paramAccountList = accountMapper.selectList(new QueryWrapper<>().where("1", 1));
        Assert.isTrue(paramAccountList.size() > 0, "select list failed.");
    }

    @Test
    public void testSelectOneMethod() {
        insert();
        Account paramAccount = accountMapper.selectOne(new QueryWrapper<>().where("id", SYS_UUID_CONTAINER.get(0)));
        Assert.isTrue(!ObjectUtils.isEmpty(paramAccount), "select one failed.");
    }

    @Test
    public void testFillMethodExecute() {
        insert();
        Account account = accountMapper.selectById(SYS_UUID_CONTAINER.get(0));
        accountMapper.updateById(account);
        log.info("init updatedOn值：{}", account.getUpdatedOn());
        accountMapper.updateById(account);
        log.info("last updatedOn值：{}", account.getUpdatedOn());
    }

    public void insert() {
        String id = UUID.randomUUID().toString();
        Account account = new Account();
        account.setId(id);
        account.setUserName("root");
        account.setCreatedBy(UUID.randomUUID().toString());
        Assert.isTrue(accountMapper.insert(account) > 0, "insert failed.");
        SYS_UUID_CONTAINER.add(id);
    }
}
