package com.rabbit.test;

import com.rabbit.core.constructor.DeleteWrapper;
import com.rabbit.core.constructor.QueryWrapper;
import com.rabbit.core.constructor.UpdateWrapper;
import com.rabbit.entity.Account;
import com.rabbit.entity.User;
import com.rabbit.enumatioon.Sex;
import com.rabbit.mapper.AccountMapper;
import com.rabbit.mapper.UserMapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.Assert;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@SpringBootTest
@Slf4j
public class BaseMapperMethodTest {

    @Resource
    private AccountMapper accountMapper;

    @Resource
    private UserMapper userMapper;

    private static List<String> SYS_UUID_CONTAINER = new ArrayList<>();


    /*********************************************************************** INSERT/UPDATE/DELETE ***********************************************************************/

    @Test
    public void testInsertMethod() {
        User user = new User();
        // 自动生成uuid主键
        user.setStuName("小明");
        user.setStuAge(20);
        user.setSex(Sex.MAX);

        int result = userMapper.insert(user);
        log.info(result > 0 ? "insert success..." : "insert failed...");
    }

    @Test
    public void testInsertBatchMethod() {
        // 自动生成uuid主键
        List<User> entityList = Stream.of(
                new User("", "小明1", 20, Sex.MAX),
                new User("", "小明2", 21, Sex.WOMEN),
                new User("", "小明3", 22, Sex.MAX),
                new User("", "小明4", 23, Sex.WOMEN),
                new User("", "小明5", 24, Sex.MAX)
        ).collect(Collectors.toList());

        int result = userMapper.insertBatch(entityList);
        log.info(result > 0 ? "insertBatch success..." : "insertBatch failed...");
    }

    @Test
    public void testUpdateByIdMethod() {
        User user = new User();
        user.setStuUid("558fdc1e53ab44bb840deb1a9b3014af");
        user.setStuName("小张");
        user.setStuAge(25);
        user.setSex(Sex.MAX);

        int result = userMapper.updateById(user);
        log.info(result > 0 ? "updateById success..." : "updateById failed...");
    }

    @Test
    public void testUpdateBatchByIdMethod() {
        List<User> entityList = Stream.of(
                new User("5e182bf8de7cf5871d904e05", "小明1", 20, Sex.MAX),
                new User("5e182bf8de7cf5871d904e06", "小明2", 21, Sex.WOMEN),
                new User("5e182bf8de7cf5871d904e07", "小明3", 22, Sex.MAX),
                new User("5e182bf8de7cf5871d904e08", "小明4", 23, Sex.WOMEN),
                new User("5e182bf8de7cf5871d904e09", "小明5", 24, Sex.MAX)
        ).collect(Collectors.toList());

        int result = userMapper.updateBatchById(entityList);
        log.info(result > 0 ? "updateBatchById success..." : "updateBatchById failed...");
    }

    @Test
    public void testDeleteById() {
        int result = userMapper.deleteById("5e182bf8de7cf5871d904e0a");
        log.info(result > 0 ? "deleteById success..." : "deleteById failed...");
    }

    @Test
    public void testDeleteBatchById() {
        List<Object> idList = Stream.of(
                "5e182bf8de7cf5871d904e05",
                "5e182bf8de7cf5871d904e06",
                "5e182bf8de7cf5871d904e07",
                "5e182bf8de7cf5871d904e08",
                "5e182bf8de7cf5871d904e09"
        ).collect(Collectors.toList());
        int result = userMapper.deleteBatchById(idList);
        log.info(result > 0 ? "deleteBatchById success..." : "deleteBatchById failed...");
    }

    @Test
    public void testUpdateMethod() {
        User user = new User();
        user.setStuName("小李");
        user.setStuAge(21);
        user.setSex(Sex.WOMEN);

        UpdateWrapper wrapper = new UpdateWrapper();
        wrapper.setEntity(user);
        wrapper.where("stu_uid", "558fdc1e53ab44bb840deb1a9b3014af");
        wrapper.where("del_flag", 1);

        int result = userMapper.update(wrapper);
        log.info(result > 0 ? "update success..." : "update failed...");
    }

    @Test
    public void testDeleteMethod() {
        DeleteWrapper wrapper = new DeleteWrapper();
        wrapper.where("stu_uid", "558fdc1e53ab44bb840deb1a9b3014af");
        wrapper.where("del_flag", 1);

        int result = userMapper.delete(wrapper);
        log.info(result > 0 ? "delete success..." : "delete failed...");
    }


    /*********************************************************************** SELECT ***********************************************************************/


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
