package cloud.gouyiba.mapper;

import cloud.gouyiba.core.mapper.BaseMapper;
import cloud.gouyiba.entity.Account;

/**
 * this class by created wuyongfei on 2020/5/10 13:50
 **/
public interface AccountMapper extends BaseMapper<Account> {
    Account findById(String id);
}
