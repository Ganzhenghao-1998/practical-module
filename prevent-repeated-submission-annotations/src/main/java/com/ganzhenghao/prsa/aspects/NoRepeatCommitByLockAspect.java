package com.ganzhenghao.prsa.aspects;

import com.ganzhenghao.prsa.annotation.ConditionalOnLock;
import com.ganzhenghao.prsa.service.Lockable;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * 锁的切面
 *
 * @author Ganzhenghao
 * @version 1.0
 * @date 2021/9/30 11:06
 */
@Component
@Aspect
@ConditionalOnLock(prefix = "no.repeat.commit", name = "no-repeat-commit-type", havingValue = {"lock", "distributed_locks", "distributed_locks_redis"}, matchIfMissing = false)
public class NoRepeatCommitByLockAspect {

    @Autowired(required = false)
    private Lockable lockable;


    @PostConstruct
    public void init() {
        System.out.println("NoRepeatCommitByLockAspect 加载成功! @@ ");
    }

    /**
     * todo 切点 动态配置
     * <href>https://blog.csdn.net/weixin_35297190/article/details/112734737</href>
     */
    @Pointcut("execution(* *..controller.*.*(..))")
    public void pointCut() {
    }


    @Around("pointCut()")
    public Object around(ProceedingJoinPoint pjp) {

        return lockable.lockedExecution(pjp);

    }



}
