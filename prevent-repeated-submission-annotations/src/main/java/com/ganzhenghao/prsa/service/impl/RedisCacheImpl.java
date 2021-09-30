package com.ganzhenghao.prsa.service.impl;

import cn.hutool.core.util.StrUtil;
import com.ganzhenghao.prsa.service.CacheService;
import com.ganzhenghao.prsa.util.CacheKeyUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.concurrent.TimeUnit;

/**
 * 基于Redis实现缓存
 *
 * @author Ganzhenghao
 * @version 1.0
 * @date 2021/9/28 11:33
 */
@Service("redisCache")
@ConditionalOnProperty(prefix = "no.repeat.commit", name = {"no-repeat-commit-type"}, havingValue = "redis", matchIfMissing = true)
public class RedisCacheImpl implements CacheService {


    @PostConstruct
    public void init() {
        System.out.println("RedisCacheImpl 加载了@");
    }

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Override
    public boolean cache(String id, String cacheKeyPrefix, Long time, TimeUnit unit, String data) {

        argsCheck(id, cacheKeyPrefix, time, unit);

        String cacheKey = CacheKeyUtil.getCacheKey(cacheKeyPrefix, id);
        // 如果data为空 那么使用cacheKey
        if (StrUtil.isEmpty(data)) {
            data = cacheKey;
        }
        return Boolean.TRUE.equals(redisTemplate.opsForValue().setIfAbsent(cacheKey, data, time, unit));
    }


    @Override
    public boolean cache(String id, String cacheKeyPrefix, Long time, TimeUnit unit) {
        // 如果 没有data 那么 使用id替代data
        return cache(id, cacheKeyPrefix, time, unit, "");
    }

}
