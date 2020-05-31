package com.rabbit.test;

import com.rabbit.core.constructor.QueryWrapper;
import com.rabbit.entity.Account;
import com.rabbit.mapper.AccountMapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.Assert;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

@SpringBootTest
@Slf4j
public class BaseMapperMethodTest {

    @Resource
    private AccountMapper accountMapper;

    private static List<String> SYS_UUID_CONTAINER = new ArrayList<>();

    @Test
    public void testInsertMethod() {
        String id = UUID.randomUUID().toString();
        Account account = new Account();
        account.setId(id);
        account.setUserName("root_" + new Random().nextInt(10));
        account.setPass(null);
        account.setCreatedBy(UUID.randomUUID().toString());
        Assert.isTrue(accountMapper.insert(account) > 0, "insert failed.");
        SYS_UUID_CONTAINER.add(id);
    }

    @Test
    public void testSelectByIdMethod() {
        testInsertMethod();
        Account accountObj = accountMapper.selectById(SYS_UUID_CONTAINER.get(0));
        Assert.isTrue(!ObjectUtils.isEmpty(accountObj), "select by id failed.");
        log.info(accountObj.toString());
    }

    @Test
    public void testSelectByIdsMethod() {
        testInsertMethod();
        testInsertMethod();
        List<Account> accountListByIds = accountMapper.selectBatchIds(SYS_UUID_CONTAINER);
        Assert.isTrue(accountListByIds.size() == 2, "select by ids failed.");
        log.info(accountListByIds.toString());
    }

    @Test
    public void testSelectCountMethod() {
        testInsertMethod();
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
        testInsertMethod();
        // empty param
        List<Account> emptyParamAccountList = accountMapper.selectList(null);
        Assert.isTrue(emptyParamAccountList.size() > 0, "select list failed.");
        // param
        List<Account> paramAccountList = accountMapper.selectList(new QueryWrapper<>().where("1", 2));
        Assert.isTrue(paramAccountList.size() > 0, "select list failed.");
    }

    @Test
    public void testSelectOneMethod() {
        testInsertMethod();
        Account paramAccount = accountMapper.selectOne(new QueryWrapper<>().where("id", SYS_UUID_CONTAINER.get(0)));
        Assert.isTrue(!ObjectUtils.isEmpty(paramAccount), "select one failed.");
    }
}
