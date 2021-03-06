package com.ganzhenghao.prsa.service.impl;

import com.ganzhenghao.prsa.annotation.NoRepeatCommit;
import com.ganzhenghao.prsa.service.Lockable;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

import java.util.concurrent.ConcurrentHashMap;

/**
 * 基于Synchronized锁实现
 *
 * @author Ganzhenghao
 * @version 1.0
 * @date 2021/9/30 16:16
 */
@Service("synchronizedLockable")
@ConditionalOnProperty(
        prefix = "no.repeat.commit",
        name = "no-repeat-commit-type",
        havingValue = "lock",
        matchIfMissing = false
)
public class SynchronizedLockableImpl implements Lockable {


    /**
     * 映射map,用于存储对应的方法的切点表达式
     */
    private final ConcurrentHashMap<String, String> mappingMap = new ConcurrentHashMap<>(256);

    @Override
    public Object lockedExecution(ProceedingJoinPoint pjp) {
        //实际类型为 MethodInvocationProceedingJoinPoint内部类MethodSignatureImpl
        MethodSignature signature = (MethodSignature) pjp.getSignature();

        //获取注解
        NoRepeatCommit annotation = signature.getMethod().getAnnotation(NoRepeatCommit.class);

        // 获取对应方法的切点表达式
        //实际打印的 是对应的切点表达式 String com.example.prevent.controller.NoRepeatController.m1()
        String pointCut = signature.toString();

        /*
         方案一: 直接判断是否有注解 有则加锁执行 没有则普通执行 (锁对象粒度大)

        方案二 :(锁对象粒度小)
                 直接判断mappingMap中是否存在对应的切点表达式
              如果存在则使用对应的键作为锁对象加锁,
              不存在则判断对应方法是否有@NoRepeatCommit, 如果有则将其存入mappingMap中,然后加锁执行
              否则放行
         */


        // 判断mappingMap中是否存在对应的切点表达式,如果存在则使用对应的键值作为锁对象加锁
        if (mappingMap.containsKey(pointCut)) {
            synchronized (mappingMap.get(pointCut)) {
                return executeRequestLogic(pjp);
            }
        }

        // 判断是否有@NoRepeatCommit注解
        if (annotation != null) {
            // 如果注解存在且对应的切点表达式不存在,则将其存入map
            // 返回值如果为null代表添加成功
            String result = mappingMap.putIfAbsent(pointCut, pointCut);

            if (result == null) {
                // 加锁执行
                synchronized (mappingMap.get(pointCut)) {
                    return executeRequestLogic(pjp);
                }
            }
        }

        return executeRequestLogic(pjp);
    }


    /**
     * 执行请求的逻辑
     *
     * @param pjp pjp
     * @return {@link Object}
     */
    private Object executeRequestLogic(ProceedingJoinPoint pjp) {
        Object ret = null;
        try {
            ret = pjp.proceed();
        } catch (Throwable e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        return ret;
    }
}
