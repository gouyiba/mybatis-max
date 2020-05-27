package com.rabbit.test;

import com.rabbit.common.utils.Assert;
import com.rabbit.entity.Account;
import com.rabbit.mapper.AccountMapper;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.util.UUID;

@SpringBootTest
public class BaseMapperMethodTest {

    @Resource
    private AccountMapper accountMapper;

    @Test
    public void testInsert() {
        Account account = new Account();
        account.setId(UUID.randomUUID().toString());
        account.setUserName("root");
        account.setPass("521");
        account.setCreatedBy(UUID.randomUUID().toString());
        Assert.isTrue(accountMapper.insert(account) > 0, "insert success.");
    }
}
