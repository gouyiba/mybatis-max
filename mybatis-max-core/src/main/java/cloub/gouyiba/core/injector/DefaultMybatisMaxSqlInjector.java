package cloub.gouyiba.core.injector;

import cloub.gouyiba.core.injector.method.base.*;
import cloub.gouyiba.core.injector.method.business.*;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * this class by created wuyongfei on 2020/5/10 20:53
 **/
public class DefaultMybatisMaxSqlInjector extends AbstractMybatisMaxSqlInjector {
    public DefaultMybatisMaxSqlInjector() {
    }

    /**
     * 获取自定义方法
     *
     * @param mapperClass
     * @return
     */
    @Override
    public List<MybatisMaxAbstractMethod> getMethodList(Class<?> mapperClass) {
        return Stream.of(
                // business
                new AddBatchObject(),
                new AddObject(),
                new CustomSqlObject(),
                new DeleteBatchByIdObject(),
                new DeleteObject(),
                new GetObject(),
                new GetObjectList(),
                new UpdateBatchByIdObject(),
                new UpdateObject(),
                // base
                new Insert(),
                new InsertBatch(),
                new DeleteById(),
                new DeleteBatchById(),
                new Delete(),
                new UpdateById(),
                new UpdateBatchById(),
                new Update(),
                new SelectById(),
                new SelectBatchIds(),
                new SelectCount(),
                new SelectList(),
                new SelectOne()
        ).collect(Collectors.toList());
    }
}
