package com.ganzhenghao.prsa.service.impl;

import com.ganzhenghao.prsa.service.CacheService;

import java.util.concurrent.TimeUnit;

/**
 * 基于 Hutool的缓存工具类实现缓存
 *
 * @author Ganzhenghao
 * @version 1.0
 * @date 2021/9/28 11:46
 */
public class HutoolCacheImpl implements CacheService {
    @Override
    public boolean cache(String id, String cacheKeyPrefix, Integer time, TimeUnit unit, String data) {
        return false;
    }

    @Override
    public boolean cache(String id, String cacheKeyPrefix, Integer time, TimeUnit unit) {
        return false;
    }
}
