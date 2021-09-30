package com.ganzhenghao.prsa.config;

import com.ganzhenghao.prsa.annotation.ConditionalOnLock;
import com.ganzhenghao.prsa.interceptors.NoRepeatCommitPointCutInterceptor;
import org.springframework.aop.aspectj.AspectJExpressionPointcutAdvisor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 防重复提交 切点配置
 *
 * @author Ganzhenghao
 * @version 1.0
 * @date 2021/9/30 14:34
 */
@Configuration
@ConditionalOnLock(prefix = "no.repeat.commit", name = "no-repeat-commit-type", havingValue = {"lock", "distributed_locks", "distributed_locks_redis"}, matchIfMissing = false)
public class NoRepeatCommitPointCutConfig {

    @Autowired
    private NoRepeatCommitLockConfig config;

    @Bean
    public AspectJExpressionPointcutAdvisor noRepeatCommitPointCutAdvisor() {

        AspectJExpressionPointcutAdvisor advisor = new AspectJExpressionPointcutAdvisor();
        advisor.setExpression(config.getPointCutExpression());
        advisor.setAdvice(new NoRepeatCommitPointCutInterceptor());
        return advisor;
    }


}
