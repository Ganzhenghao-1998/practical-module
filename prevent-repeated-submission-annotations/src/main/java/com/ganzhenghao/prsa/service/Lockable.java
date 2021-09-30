package com.ganzhenghao.prsa.service;

import org.aspectj.lang.ProceedingJoinPoint;

/**
 * 实现此接口,可以自定义加锁逻辑
 *
 * @author Ganzhenghao
 * @version 1.0
 * @date 2021/9/30 16:13
 */
public interface Lockable {
    /**
     * 加锁执行
     *
     * @param pjp pjp
     * @return {@link Object}
     */
    Object lockedExecution(ProceedingJoinPoint pjp);

}
