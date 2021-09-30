package com.ganzhenghao.prsa.annotation;

import com.ganzhenghao.prsa.conditions.ConditionOnLock;
import org.springframework.context.annotation.Conditional;

import java.lang.annotation.*;

/**
 * 有条件的锁
 *
 * @author Ganzhenghao
 * @version 1.0
 * @date 2021/9/30 14:51
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
@Documented
@Conditional(ConditionOnLock.class)
public @interface ConditionalOnLock {

    String prefix() default "";

    String name() default "";

    String[] havingValue() default {};

    boolean matchIfMissing() default false;

}
