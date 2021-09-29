package com.ganzhenghao.prsa.service.impl;

import cn.hutool.cache.CacheUtil;
import cn.hutool.cache.impl.TimedCache;
import cn.hutool.core.util.StrUtil;
import com.ganzhenghao.prsa.config.NoRepeatCommitConfig;
import com.ganzhenghao.prsa.service.CacheService;
import com.ganzhenghao.prsa.util.CacheKeyUtil;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.concurrent.TimeUnit;

/**
 * 基于 Hutool的缓存工具类实现缓存
 *
 * @author Ganzhenghao
 * @version 1.0
 * @date 2021/9/28 11:46
 */
@Service("hutoolCache")
//@ConditionalOnExpression("'${no.repeat.commit.no-repeat-commit-type}'.equals('internal_hutool')")
@ConditionalOnProperty(prefix = "no.repeat.commit", name = {"no-repeat-commit-type"}, havingValue = "internal_hutool", matchIfMissing = false)
public class HutoolCacheImpl implements CacheService {


    @Getter
    private TimedCache<String, String> timedCache;

    @Autowired
    private NoRepeatCommitConfig config;


    @PostConstruct
    public void init() {

        // 获取缓存清理间隔时间
        Long clearCacheTimeInterval = config.getClearCacheTimeInterval();
        TimeUnit timeUnit = config.getClearCacheTimeIntervalTimeUnit();
        long clearCacheTime = timeUnit.toMillis(clearCacheTimeInterval);

        // 获取键值默认过期时间
        TimeUnit unit = config.getTimeUnit();
        Long expireTime = config.getExpireTime();

        expireTime = unit.toMillis(expireTime);

        // 设置缓存过期时间
        timedCache = CacheUtil.newTimedCache(expireTime);
        // 设置缓存清理间隔时间
        timedCache.schedulePrune(clearCacheTime);


        System.out.println("HutoolCacheImpl 加载了@ 缓存过期时间:" + clearCacheTime / 1000L + "秒" +
                " @ 缓存清理间隔时间: " + expireTime / 1000L + " 秒");
    }

    @Override
    public boolean cache(String id, String cacheKeyPrefix, Long time, TimeUnit unit, String data) {

        argsCheck(id, cacheKeyPrefix, time, unit);

        long expireTime = unit.toMillis(time);

        String cacheKey = CacheKeyUtil.getCacheKey(cacheKeyPrefix, id);
        // 如果data为空 那么使用cacheKey
        if (StrUtil.isEmpty(data)) {
            data = cacheKey;
        }

        // 查改删必须同时进行 ==不用加上续约逻辑==
        // 默认读取时会重置过期时间

        // 是否存在某个key, 不会刷新过期时间 , 如果存在 返回false 表示设置id失败

/*        if (timedCache.containsKey(cacheKey)) {
            return false;
        } else {
            // 如果id不存在 那么将id作为锁对象
            synchronized (id) {
                if (timedCache.containsKey(cacheKey)){
                    timedCache.put(cacheKey, data, expireTime);
                }
            }
        }*/

        synchronized (this) {
            if (timedCache.containsKey(cacheKey)) {
                //如果 key存在 则返回 false 代表设置id失败
                return false;
            } else {
                timedCache.put(cacheKey, data, expireTime);
                //如果 key不存在 则返回 true 代表设置id成功
                return true;
            }
        }

    }

    @Override
    public boolean cache(String id, String cacheKeyPrefix, Long time, TimeUnit unit) {

        return cache(id, cacheKeyPrefix, time, unit, "");
    }
}
