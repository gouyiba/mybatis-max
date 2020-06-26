package cloub.gouyiba.core.annotation;

import java.lang.annotation.*;

/**
 * 逻辑删除
 *
 * @author duxiaoyu
 * @since 2019-12-12
 */
@Documented
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Delete {

    /**
     * true: 默认物理删除
     * false: 逻辑删除
     *
     * @return
     */
    boolean physicsDel() default true;

    /**
     * 逻辑删除value
     *
     * @return
     */
    int value() default 0;
}
