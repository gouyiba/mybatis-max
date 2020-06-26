package cloub.gouyiba.core.proxy;

import org.apache.ibatis.session.SqlSession;

import java.lang.reflect.Proxy;

/**
 * this class by created wuyongfei on 2020/5/4 16:29
 **/
public class MybatisMaxMapperProxyFactory<T> {
    private final Class<T> mapperInterface;

    public MybatisMaxMapperProxyFactory(Class<T> mapperInterface) {
        this.mapperInterface = mapperInterface;
    }

    protected T newInstance(MybatisMaxMapperProxy<T> mapperProxy) {
        return (T) Proxy.newProxyInstance(mapperInterface.getClassLoader(), new Class[]{mapperInterface}, mapperProxy);
    }

    public T newInstance(SqlSession sqlSession) {
        MybatisMaxMapperProxy<T> mapperProxy = new MybatisMaxMapperProxy<>(sqlSession, mapperInterface);
        return newInstance(mapperProxy);
    }
}
