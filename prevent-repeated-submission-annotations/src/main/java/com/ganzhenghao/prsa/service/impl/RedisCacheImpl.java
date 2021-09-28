package com.ganzhenghao.prsa.service.impl;

import com.ganzhenghao.prsa.exception.DataException;
import com.ganzhenghao.prsa.service.CacheService;
import com.ganzhenghao.prsa.util.CacheKeyUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

/**
 * 基于Redis实现缓存
 *
 * @author Ganzhenghao
 * @version 1.0
 * @date 2021/9/28 11:33
 */
@Service("redisCache")
public class RedisCacheImpl implements CacheService {

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Override
    public boolean cache(String id, String cacheKeyPrefix, Integer time, TimeUnit unit, String data) {
        if (id == null || cacheKeyPrefix == null || cacheKeyPrefix.length() == 0 || time == null || unit == null) {
            throw new DataException("数据异常");
        }

        String cacheKey = CacheKeyUtil.getCacheKey(cacheKeyPrefix, id);
        return Boolean.TRUE.equals(redisTemplate.opsForValue().setIfAbsent(cacheKey, data, time, unit));
    }

    @Override
    public boolean cache(String id, String cacheKeyPrefix, Integer time, TimeUnit unit) {
        return cache(id, cacheKeyPrefix, time, unit, "");
    }

}
