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
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @ClassName BaseMapperMethodTest
 * @Author duxiaoyu
 * @Date 2020/6/9 14:20
 * @Version 1.0
 */
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
        String id = ByIdBefore();

        User user = userMapper.selectById(id);
        user.setStuName("小张");
        user.setStuAge(24);
        user.setSex(Sex.MAX);

        // 全量修改
        int result = userMapper.updateById(user);
        log.info(result > 0 ? "updateById success..." : "updateById failed...");
    }

    @Test
    public void testUpdateBatchByIdMethod() {
        List<String> ids = BatchByIdBefore();
        List<User> entityList = userMapper.selectBatchIds(ids);
        for (int i = 0; i < entityList.size(); i++) {
            if (i % 2 == 0) {
                entityList.get(i).setSex(Sex.MAX);
                entityList.get(i).setStuName("小明" + i);
                entityList.get(i).setStuAge(i);
            } else {
                entityList.get(i).setSex(Sex.WOMEN);
                entityList.get(i).setStuName("小明" + i);
                entityList.get(i).setStuAge(i);
            }
        }

        // 全量修改
        int result = userMapper.updateBatchById(entityList);
        log.info(result > 0 ? "updateBatchById success..." : "updateBatchById failed...");
    }

    @Test
    public void testDeleteById() {
        String id = ByIdBefore();

        int result = userMapper.deleteById(id);
        log.info(result > 0 ? "deleteById success..." : "deleteById failed...");
    }

    @Test
    public void testDeleteBatchById() {
        List<Object> ids = BatchByIdBefore().stream().map(x -> x).collect(Collectors.toList());

        int result = userMapper.deleteBatchById(ids);
        log.info(result > 0 ? "deleteBatchById success..." : "deleteBatchById failed...");
    }

    @Test
    public void testUpdateMethod() {
        String id = ByIdBefore();

        User user = new User();
        user.setStuName("小李");
        user.setStuAge(21);
        user.setSex(Sex.WOMEN);

        UpdateWrapper wrapper = new UpdateWrapper();
        wrapper.setEntity(user);
        wrapper.where("stu_uid", id);
        wrapper.where("del_flag", 1);

        int result = userMapper.update(wrapper);
        log.info(result > 0 ? "update success..." : "update failed...");
    }

    @Test
    public void testDeleteMethod() {
        String id = ByIdBefore();

        DeleteWrapper wrapper = new DeleteWrapper();
        wrapper.where("stu_uid", id);
        wrapper.where("del_flag", 1);

        int result = userMapper.delete(wrapper);
        log.info(result > 0 ? "delete success..." : "delete failed...");
    }

    public String ByIdBefore() {
        User user = new User();
        // 自动生成uuid主键
        user.setStuName("小明");
        user.setStuAge(20);
        user.setSex(Sex.MAX);

        int result = userMapper.insert(user);
        return result > 0 ? user.getStuUid() : "";
    }

    public List<String> BatchByIdBefore() {
        // 自动生成uuid主键
        List<User> entityList = Stream.of(
                new User("", "小王1", 20, Sex.MAX),
                new User("", "小王2", 21, Sex.WOMEN),
                new User("", "小王3", 22, Sex.MAX),
                new User("", "小王4", 23, Sex.WOMEN),
                new User("", "小王5", 24, Sex.MAX)
        ).collect(Collectors.toList());
        int result = userMapper.insertBatch(entityList);

        List<String> ids = entityList.stream().map(x -> x.getStuUid()).collect(Collectors.toList());
        return CollectionUtils.isEmpty(ids) ? Collections.emptyList() : ids;
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
        // order by
        Integer orderByCount = accountMapper.selectCount(new QueryWrapper<>().orderBy("created_on", "DESC"));
        Assert.isTrue(orderByCount > 0, "select count failed.");
    }

    @Test
    public void testSelectListMethod() {
        insert();
        // empty param
        List<Account> emptyParamAccountList = accountMapper.selectList(null);
        Assert.isTrue(emptyParamAccountList.size() > 0, "select list failed.");
        // where
        List<Account> paramAccountList = accountMapper.selectList(new QueryWrapper<>().where("1", 1));
        Assert.isTrue(paramAccountList.size() > 0, "select list failed.");
        // selected column
        List<Account> selectedColumnAccountList = accountMapper.selectList(new QueryWrapper<>().setColumn("id, user_name"));
        Assert.isTrue(selectedColumnAccountList.size() > 0, "select list failed.");
        // order by
        List<Account> orderByAccountList = accountMapper.selectList(new QueryWrapper<>().orderBy("created_on", "DESC"));
        Assert.isTrue(orderByAccountList.size() > 0, "select list failed.");
        // limit
        List<Account> limitAccountList = accountMapper.selectList(new QueryWrapper<>().limit(2, 10).orderBy("created_on", "DESC"));
        Assert.isTrue(limitAccountList.size() > 0, "select list failed.");
    }

    @Test
    public void testSelectOneMethod() {
        insert();
        Account paramAccount = accountMapper.selectOne(new QueryWrapper<>().where("id", SYS_UUID_CONTAINER.get(0)));
        Assert.isTrue(!ObjectUtils.isEmpty(paramAccount), "select one failed.");
        // selected column
        Account selectedColumnAccount = accountMapper.selectOne(new QueryWrapper<>().setColumn("id, user_name").where("id", SYS_UUID_CONTAINER.get(0)));
        Assert.isTrue(!ObjectUtils.isEmpty(selectedColumnAccount), "select one failed.");
        // order by
        Account orderByAccount = accountMapper.selectOne(new QueryWrapper<>().orderBy("created_on", "DESC").where("id", SYS_UUID_CONTAINER.get(0)));
        Assert.isTrue(!ObjectUtils.isEmpty(orderByAccount), "select one failed.");
        // limit
        Account limitAccount = accountMapper.selectOne(new QueryWrapper<>().limit(1, 10).orderBy("created_on", "DESC").where("id", SYS_UUID_CONTAINER.get(0)));
        Assert.isTrue(!ObjectUtils.isEmpty(limitAccount), "select one failed.");
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
        Account account = new Account();
        account.setSex(Sex.MAX);
        account.setUserName("root");
        account.setCreatedBy(UUID.randomUUID().toString());
        Assert.isTrue(accountMapper.insert(account) > 0, "insert failed.");
        SYS_UUID_CONTAINER.add(account.getId());
    }

    @Test
    public void testlist() {
        List<Account> emptyParamAccountList = accountMapper.selectList(null);
        System.out.println(emptyParamAccountList);
    }
}
