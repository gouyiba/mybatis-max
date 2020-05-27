package com.rabbit.test;

import com.rabbit.entity.Account;
import com.rabbit.mapper.AccountMapper;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.Assert;

import javax.annotation.Resource;
import java.util.UUID;

@SpringBootTest
public class BaseMapperMethodTest {

    @Resource
    private AccountMapper accountMapper;

    @Test
    public void testInsertMethod() {
        Account account = new Account();
        account.setId(UUID.randomUUID().toString());
        account.setUserName("root");
        account.setPass(null);
        account.setCreatedBy(UUID.randomUUID().toString());
        Assert.isTrue(accountMapper.insert(account) > 0, "insert failed.");
    }
}
