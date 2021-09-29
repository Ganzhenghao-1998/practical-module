package com.ganzhenghao.prsa.annotation;

import java.lang.annotation.*;
import java.util.concurrent.TimeUnit;

/**
 * 防重复提交注解
 *
 * @author Ganzhenghao
 * @version 1.0
 * @date 2021/8/24 10:56
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
@Documented
public @interface NoRepeatCommit {

    /**
     * Redis中Key过期时间 默认 5 分钟
     *
     * @return {@link String}
     */
    long expireTime() default 5;

    /**
     * RedisKey前缀
     *
     * @return {@link String}
     */
    String cacheKeyPrefix() default "NoRepeatCommit";

    /**
     * 时间单位
     *
     * @return {@link TimeUnit}
     */
    TimeUnit timeUnit() default TimeUnit.MINUTES;

    /**
     * 针对每个方法设置请求头防重复提交的Id名称
     *
     * @return {@link String}
     */
    String headerName() default "noRepeatId";

    /**
     * 设置请求被拦截后返回的状态码,默认505
     *
     * @return int
     */
    int status() default 505;
}
