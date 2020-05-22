package com.rabbit.core.injector;

import com.rabbit.core.injector.method.service.*;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.annotations.Insert;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * this class by created wuyongfei on 2020/5/10 20:53
 **/
public class DefaultRabbitSqlInjector extends AbstractRabbitSqlInjector {
    public DefaultRabbitSqlInjector() {
    }

    /**
     * 获取自定义方法
     * @param mapperClass
     * @return
     */
    @Override
    public List<RabbitAbstractMethod> getMethodList(Class<?> mapperClass) {
        String mapperName=mapperClass.getName();
        if(StringUtils.equals(mapperName,"com.rabbit.core.super_mapper.BusinessMapper")){
            return Stream.of(
                    new AddBatchObject(),
                    new AddObject(),
                    new CustomSqlObject(),
                    new DeleteBatchByIdObject(),
                    new DeleteObject(),
                    new GetObject(),
                    new GetObjectList(),
                    new UpdateBatchByIdObject(),
                    new UpdateObject()
            ).collect(Collectors.toList());
        }
        return null;
    }
}
