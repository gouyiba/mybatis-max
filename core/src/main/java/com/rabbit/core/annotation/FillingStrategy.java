package com.rabbit.core.annotation;

import java.lang.annotation.*;

/**
 * 填充策略类标识
 */
@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface FillingStrategy {

}
