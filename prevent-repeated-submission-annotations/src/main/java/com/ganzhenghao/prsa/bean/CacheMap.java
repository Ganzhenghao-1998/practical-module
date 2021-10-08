package com.ganzhenghao.prsa.bean;

import com.ganzhenghao.prsa.exception.DataException;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Consumer;

/**
 * 封装ConcurrentHashMap类
 *
 * @author Ganzhenghao
 * @version 1.0
 * @date 2021/10/8 9:55
 */
public class CacheMap extends ConcurrentHashMap<String, CacheData<?>> {

    private final ConcurrentHashMap<String, CacheData<?>> CACHE_MAP;

    public CacheMap() {
        CACHE_MAP = new ConcurrentHashMap<String, CacheData<?>>();
    }

    public CacheMap(int initialCapacity) {
        CACHE_MAP = new ConcurrentHashMap<String, CacheData<?>>(initialCapacity);
    }

    @Override
    public CacheData<?> put(String key, CacheData<?> value) {
        return CACHE_MAP.put(key, value);
    }

    @Override
    public boolean containsKey(Object key) {
        return !isExpire((String) key);
    }

    @Override
    public CacheData<?> putIfAbsent(String key, CacheData<?> value) {

        // 删除过期key
        CacheData<?> cacheData = CACHE_MAP.get(key);

        if (cacheData.isExpire()) {
            CACHE_MAP.remove(key);
        }

        return CACHE_MAP.putIfAbsent(key, value);
    }


    @Override
    public CacheData<?> get(Object key) {

        if (isExpire(key)) {
            return null;
        }

        return CACHE_MAP.get(key);
    }


    @Override
    public KeySetView<String, CacheData<?>> keySet() {
        return CACHE_MAP.keySet();
    }


    @Override
    public Set<Entry<String, CacheData<?>>> entrySet() {
        return CACHE_MAP.entrySet();
    }


    @Override
    public void forEach(long parallelismThreshold, BiConsumer<? super String, ? super CacheData<?>> action) {
        CACHE_MAP.forEach(parallelismThreshold, action);
    }

    @Override
    public <U> void forEach(long parallelismThreshold, BiFunction<? super String, ? super CacheData<?>, ? extends U> transformer, Consumer<? super U> action) {
        CACHE_MAP.forEach(parallelismThreshold, transformer, action);
    }


    /**
     * 判断键是否到期,到期则删除
     *
     * @param key 关键
     * @return boolean
     */
    private boolean isExpire(Object key) {

        if (!(key instanceof String)) {
            throw new DataException("key必须为String类型!");
        }

        // 判断是否过期,过期则删除
        if (CACHE_MAP.get(key).isExpire()) {
            CACHE_MAP.remove(key);
            return true;
        }

        return false;
    }


}
