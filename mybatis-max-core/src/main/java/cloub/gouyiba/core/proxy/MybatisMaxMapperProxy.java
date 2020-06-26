package cloub.gouyiba.core.proxy;

import cloub.gouyiba.core.mapper.BaseMapper;
import org.apache.ibatis.binding.MapperMethod;
import org.apache.ibatis.reflection.ExceptionUtil;
import org.apache.ibatis.session.SqlSession;
import org.springframework.util.CollectionUtils;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.Map;
import java.util.Objects;

/**
 * this class by created wuyongfei on 2020/5/4 15:53
 **/
public class MybatisMaxMapperProxy<T> implements InvocationHandler {
    private final SqlSession sqlSession;
    private final Class<T> mapperInterface;

    public MybatisMaxMapperProxy(SqlSession sqlSession, Class<T> mapperInterface) {
        this.sqlSession = sqlSession;
        this.mapperInterface = mapperInterface;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        Object obj = verifyParameter(method, args);
        if (Objects.nonNull(obj)) {
            return obj;
        }
        try {
            if (Object.class.equals(method.getDeclaringClass())) {
                return method.invoke(this, args);
            }
        } catch (Throwable throwable) {
            throw ExceptionUtil.unwrapThrowable(throwable);
        }
        MapperMethod mapperMethod = new MapperMethod(this.mapperInterface, method, this.sqlSession.getConfiguration());
        return mapperMethod.execute(this.sqlSession, args);
    }

    public Object verifyParameter(Method method, Object[] args) throws Exception {
        Object obj = null;
        if (BaseMapper.class.getName().equals(method.getDeclaringClass().getName())) {
            Class<?> clazz = null;
            Type type = method.getAnnotatedReturnType().getType();
            if (type instanceof Class<?>) {
                clazz = (Class<?>) type;
                if (int.class.isAssignableFrom(clazz)) {
                    obj = 0;
                } else if (Integer.class.isAssignableFrom(clazz)) {
                    obj = Integer.valueOf(0);
                } else if (long.class.isAssignableFrom(clazz)) {
                    obj = 0L;
                } else if (Long.class.isAssignableFrom(clazz)) {
                    obj = Long.valueOf(0L);
                } else if (String.class.isAssignableFrom(clazz)) {
                    obj = new String();
                }
            }
            for (Object item : args) {
                if (item instanceof Map) {
                    if (CollectionUtils.isEmpty((Map) item)) {
                        return obj;
                    }
                } else if (item instanceof Collection) {
                    if (CollectionUtils.isEmpty((Collection) item)) {
                        return obj;
                    }
                } else {
                    if (Objects.isNull(item)) {
                        return obj;
                    }
                }
            }
        }
        return null;
    }
}
