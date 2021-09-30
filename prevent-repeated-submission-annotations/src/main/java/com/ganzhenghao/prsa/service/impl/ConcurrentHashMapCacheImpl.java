package com.ganzhenghao.prsa.service.impl;

import cn.hutool.core.util.StrUtil;
import com.ganzhenghao.prsa.bean.CacheData;
import com.ganzhenghao.prsa.config.NoRepeatCommitScheduledConfig;
import com.ganzhenghao.prsa.service.CacheService;
import com.ganzhenghao.prsa.util.CacheKeyUtil;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

/**
 * 基于ConcurrentHashMapCacheImpl 实现缓存
 *
 * @author Ganzhenghao
 * @version 1.0
 * @date 2021/9/28 17:02
 */
@Service("concurrentHashMapCache")
@ConditionalOnProperty(prefix = "no.repeat.commit", name = {"no-repeat-commit-type"}, havingValue = "internal_concurrenthashmap", matchIfMissing = false)
public class ConcurrentHashMapCacheImpl implements CacheService {

    @Autowired
    NoRepeatCommitScheduledConfig noRepeatCommitScheduledConfig;
    @Getter
    private ConcurrentHashMap<String, CacheData<?>> cacheMap;

    @PostConstruct
    public void init() {
        cacheMap = noRepeatCommitScheduledConfig.getCacheMap();

        System.out.println("ConcurrentHashMapCacheImpl 加载了@");
    }


    @Override
    public boolean cache(String id, String cacheKeyPrefix, Long time, TimeUnit unit, String data) {

        argsCheck(id, cacheKeyPrefix, time, unit);

        String cacheKey = CacheKeyUtil.getCacheKey(cacheKeyPrefix, id);
        // 如果data为空 那么使用cacheKey
        if (StrUtil.isEmpty(data)) {
            data = cacheKey;
        }

        CacheData<String> cacheData = new CacheData<>(unit.toMillis(time), data);

        //如果指定的键尚未与值关联（或映射到null ）将其与给定值关联并返回null ，否则返回当前值。
        //返回值：
        //与指定键关联的前一个值，如果没有该键的映射，则为null
        // todo 目前有个问题 putIfAbsent是ConcurrentHashMap自带的方法 他不会判断数据是否过期, 需要等待后台任务清理过期键值,所以数据实际过期时间可能比设置时间要长, 解决办法:可以封装一个新的ConcurrentHashMap来实现
        CacheData<?> result = cacheMap.putIfAbsent(cacheKey, cacheData);

        // 返回true代表设置成功
        return null == result;

    }

    @Override
    public boolean cache(String id, String cacheKeyPrefix, Long time, TimeUnit unit) {
        return this.cache(id, cacheKeyPrefix, time, unit, "");
    }
}
