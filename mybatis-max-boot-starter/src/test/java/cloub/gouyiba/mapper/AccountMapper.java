package cloub.gouyiba.mapper;

import cloub.gouyiba.core.mapper.BaseMapper;
import cloub.gouyiba.entity.Account;

/**
 * this class by created wuyongfei on 2020/5/10 13:50
 **/
public interface AccountMapper extends BaseMapper<Account> {
    Account findById(String id);
}
