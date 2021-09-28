package com.ganzhenghao.prsa.annotation;

import java.lang.annotation.*;

/**
 * 作用于类和成员变量,用于标识该变量是否校验数据一致
 *
 * @author Ganzhenghao
 * @version 1.0
 * @date 2021/9/28 8:40
 */

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.FIELD})
@Documented
public @interface DataConsistent {

    // todo 数据校验注解
}
