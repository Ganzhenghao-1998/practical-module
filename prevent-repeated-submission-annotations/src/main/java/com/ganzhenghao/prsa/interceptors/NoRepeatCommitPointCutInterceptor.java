package com.ganzhenghao.prsa.interceptors;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

/**
 * 切点拦截器
 * 用于通过配置文件动态设置 切点表达式
 *
 * @author Ganzhenghao
 * @version 1.0
 * @date 2021/9/30 14:31
 */
public class NoRepeatCommitPointCutInterceptor implements MethodInterceptor {
    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {

        return invocation.proceed();
    }
}
