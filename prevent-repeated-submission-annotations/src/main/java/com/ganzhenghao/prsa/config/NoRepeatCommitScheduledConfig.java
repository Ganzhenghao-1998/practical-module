package com.ganzhenghao.prsa.config;

import com.ganzhenghao.prsa.bean.CacheData;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

import javax.annotation.PostConstruct;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

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

    // 创建一个 ConcurrentHashMap 初始大小 1024
    @Getter
    private final ConcurrentHashMap<String, CacheData<?>> cacheMap = new ConcurrentHashMap<>(1024);
    ;

    @Autowired
    private NoRepeatCommitConfig config;


    @PostConstruct
    public void init() {

        Long clearCacheTimeInterval = config.getClearCacheTimeInterval();
        TimeUnit timeUnit = config.getClearCacheTimeIntervalTimeUnit();
        // 获取到清理缓存的时间间隔
        long clearCacheTime = timeUnit.toMillis(clearCacheTimeInterval);

        // 设置定时任务
        Timer timer = new Timer();

        TimerTask timerTask = new TimerTask() {

            private long maxClearCacheTime = clearCacheTime;

            @Override
            public void run() {
                // 遍历删除 过期值  提前获取系统时间 以防多次调用 System.currentTimeMillis()
                long time = System.currentTimeMillis();
                for (String key : cacheMap.keySet()) {
                    CacheData<?> cacheData = cacheMap.get(key);
                    if (cacheData.isExpire(time)) {
                        cacheMap.remove(key);
                    }
                }
                //如果当前执行清理缓存任务的时间超过间隔时间,则减小清理间隔周期
                // todo  具体减少策略还需斟酌 无限制减少可能会出现线程占用过大 或则 时间为负数
                if (maxClearCacheTime <= System.currentTimeMillis() - time) {
                    timer.cancel();
                    timer.schedule(this, 1000, maxClearCacheTime - 1000L);
                }

            }
        };

        // 开启定时任务
        timer.schedule(timerTask, clearCacheTime, clearCacheTime);


        System.out.println("NoRepeatCommitScheduledConfig  加载了@");

    }

    // todo 将过期时间设置为全局过期时间clearCacheTime 但是目前注解值没有好的解决办法 可以不使用spring的定时任务
/*    @Scheduled(fixedDelay = 1000 * 60 * 5)
    public void clearCache() {

        // 遍历删除 过期值  提前获取系统时间 以防多次调用 System.currentTimeMillis()
        long time = System.currentTimeMillis();
        for (String key : cacheMap.keySet()) {
            CacheData<?> cacheData = cacheMap.get(key);
            if (cacheData.isExpire(time)) {
                cacheMap.remove(key);
            }
        }

    }*/


}
