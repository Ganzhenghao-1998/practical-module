package com.ganzhenghao.prsa.aspects;

import com.ganzhenghao.prsa.annotation.ConditionalOnLock;
import com.ganzhenghao.prsa.annotation.NoRepeatCommit;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 锁的切面
 *
 * @author Ganzhenghao
 * @version 1.0
 * @date 2021/9/30 11:06
 */
@Component
@Aspect
//@ConditionalOnProperty(prefix = "no.repeat.commit", name = {"no-repeat-commit-type"}, havingValue = "lock", matchIfMissing = false)
@ConditionalOnLock(prefix = "no.repeat.commit", name = "no-repeat-commit-type", havingValue = {"lock", "distributed_locks", "distributed_locks_redis"}, matchIfMissing = false)
public class NoRepeatCommitByLockAspect {

    @PostConstruct
    public void init() {
        System.out.println("NoRepeatCommitByLockAspect 加载成功! @@ ");
    }

    /**
     * 映射map,用于存储对应的方法的切点表达式
     */
    private final ConcurrentHashMap<String, String> mappingMap = new ConcurrentHashMap<>(256);


    /**
     * 切点 动态配置
     * <href>https://blog.csdn.net/weixin_35297190/article/details/112734737</href>
     */
    @Pointcut("execution(* *..controller.*.*(..))")
    public void pointCut() {
    }


    @Around("pointCut()")
    public Object around(ProceedingJoinPoint pjp) {


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
        System.out.println("Before....");
        Object ret = null;
        try {
            ret = pjp.proceed();
            System.out.println("after-Return...");
        } catch (Throwable throwable) {
            System.out.println("throwing...");
            throwable.printStackTrace();
        } finally {
            System.out.println("after...");
        }
        return ret;
    }


}
