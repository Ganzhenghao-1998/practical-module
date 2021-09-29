package com.ganzhenghao.prsa.config;

import com.ganzhenghao.prsa.bean.CacheData;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Ganzhenghao
 * @version 1.0
 * @date 2021/9/28 17:05
 */
@Configuration
//@ConditionalOnExpression("'${no.repeat.commit.no-repeat-commit-type}'.equals('internal_concurrenthashmap')"
@ConditionalOnProperty(prefix = "no.repeat.commit", name = {"no-repeat-commit-type"}, havingValue = "internal_concurrenthashmap", matchIfMissing = false)
@EnableScheduling
public class NoRepeatCommitScheduledConfig {

    @Getter
    private final ConcurrentHashMap<String, CacheData<?>> cacheMap;
    @Autowired
    private NoRepeatCommitConfig config;

    {
        // 创建一个 ConcurrentHashMap 初始大小 1024
        cacheMap = new ConcurrentHashMap<>(1024);
        System.out.println("NoRepeatCommitScheduledConfig  加载了@");
    }

//    private long clearCacheTime;
//
//    @PostConstruct
//    public void init() {
//        Long clearCacheTimeInterval = config.getClearCacheTimeInterval();
//        TimeUnit timeUnit = config.getClearCacheTimeIntervalTimeUnit();
//        clearCacheTime = timeUnit.toMillis(clearCacheTimeInterval);
//    }

    // todo 将过期时间设置为全局过期时间clearCacheTime 但是目前注解值没有好的解决办法
    @Scheduled(fixedDelay = 1000 * 60 * 5)
    public void clearCache() {

        // 遍历删除 过期值  提前获取系统时间 以防多次调用 System.currentTimeMillis()
        long time = System.currentTimeMillis();
        for (String key : cacheMap.keySet()) {
            CacheData<?> cacheData = cacheMap.get(key);
            if (cacheData.isExpire(time)) {
                cacheMap.remove(key);
            }

        }

    }


}
