package com.ganzhenghao.prsa.service;

import com.ganzhenghao.prsa.exception.DataException;

import java.util.concurrent.TimeUnit;

/**
 * 缓存通用接口
 *
 * @author Ganzhenghao
 * @version 1.0
 * @date 2021/9/28 11:29
 */
public interface CacheService {

    /**
     * 缓存
     *
     * @param id             id
     * @param cacheKeyPrefix 缓存键前缀
     * @param time           时间
     * @param unit           单位
     * @param data           数据
     * @return {@link Boolean}
     */
    public boolean cache(String id, String cacheKeyPrefix, Long time, TimeUnit unit, String data);

    /**
     * 缓存
     *
     * @param id             id
     * @param cacheKeyPrefix 缓存键前缀
     * @param time           时间
     * @param unit           单位
     * @return {@link Boolean}设置id成功返回true 失败返回false
     */
    public boolean cache(String id, String cacheKeyPrefix, Long time, TimeUnit unit);

    default void argsCheck(String id, String cacheKeyPrefix, Long time, TimeUnit unit) {
        if (id == null || cacheKeyPrefix == null || cacheKeyPrefix.length() == 0 || time == null || unit == null) {
            throw new DataException("数据异常");
        }
    }

}
