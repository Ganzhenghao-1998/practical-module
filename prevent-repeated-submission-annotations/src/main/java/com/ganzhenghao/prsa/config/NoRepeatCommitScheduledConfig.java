package com.ganzhenghao.prsa.config;

import com.ganzhenghao.prsa.bean.CacheData;
import com.ganzhenghao.prsa.bean.CacheMap;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

/**
 * @author Ganzhenghao
 * @version 1.0
 * @date 2021/9/28 17:05
 */
@Configuration
@ConditionalOnProperty(prefix = "no.repeat.commit", name = {"no-repeat-commit-type"}, havingValue = "internal_concurrenthashmap", matchIfMissing = false)
public class NoRepeatCommitScheduledConfig {

    @Getter
    private final CacheMap cacheMap = new CacheMap(2048);

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

            private final long maxClearCacheTime = clearCacheTime;

            private long actualClearCacheTime = clearCacheTime;

            @Override
            public void run() {
                // 遍历删除 过期值  提前获取系统时间 以防多次调用 System.currentTimeMillis()
                long time;

                time = System.currentTimeMillis();
                for (String key : cacheMap.keySet()) {
                    CacheData<?> cacheData = cacheMap.get(key);
                    if (cacheData.isExpire(time)) {
                        cacheMap.remove(key);
                    }
                }

                // 如果当前执行清理缓存任务的时间超过间隔时间,则减小清理间隔周期
                // todo  具体减少策略还需斟酌 无限制减少可能会出现线程占用过大 或者 时间为负数
//                if (maxClearCacheTime <= System.currentTimeMillis() - time) {
//                    timer.cancel();
//                    actualClearCacheTime = actualClearCacheTime - 1000L;
//                    timer.schedule(this, 1000, actualClearCacheTime);
//                }

            }
        };

        // 开启定时任务
        timer.schedule(timerTask, clearCacheTime, clearCacheTime);


        System.out.println("NoRepeatCommitScheduledConfig  加载了@");

    }


}
