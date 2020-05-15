package com.rabbit.core.injector;

import java.util.List;

/**
 * this class by created wuyongfei on 2020/5/10 20:53
 **/
public class DefaultRabbitSqlInjector extends AbstractRabbitSqlInjector {
    public DefaultRabbitSqlInjector() {
    }

    public List<RabbitAbstractMethod> getMethodList(Class<?> mapperClass) {
//        return (List)Stream.of(new Insert(), new Delete(), new DeleteByMap(), new DeleteById(), new DeleteBatchByIds(), new Update(), new UpdateById(), new SelectById(), new SelectBatchByIds(), new SelectByMap(), new SelectOne(), new SelectCount(), new SelectMaps(), new SelectMapsPage(), new SelectObjs(), new SelectList(), new SelectPage()).collect(Collectors.toList());
        return null;
    }
}
