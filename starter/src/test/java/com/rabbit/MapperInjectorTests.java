package com.rabbit;

import com.rabbit.core.entity.User;
import com.rabbit.core.mapper.AccountMapper;
import com.rabbit.core.mapper.UserMapper;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.util.List;
import java.util.Optional;

@SpringBootTest
class MapperInjectorTests {

    @Resource
    private UserMapper userMapper;

    @Resource
    private AccountMapper accountMapper;

    @Test
    void testMapperInjector() throws Exception {
        // rmp function
        List<User> testList1 = userMapper.list();

        System.out.println("size: " + testList1.size());

        testList1.forEach(user -> System.out.println("id: " + user.getId() + " type: " + user.getType()));

        Integer count = userMapper.count();

        System.out.println("size: " + count);

        // mybatis function
        User user = Optional.ofNullable(accountMapper.findById("1"))
                .orElseThrow(() -> new Exception());
        System.out.println("type: " + user.getType());
    }
}
