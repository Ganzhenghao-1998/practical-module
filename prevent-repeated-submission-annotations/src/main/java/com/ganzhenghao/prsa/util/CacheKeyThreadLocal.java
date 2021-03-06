package com.ganzhenghao.prsa.util;

/**
 * 缓存键的ThreadLocal
 *
 * @author Ganzhenghao
 * @version 1.0
 * @date 2021/9/28 10:54
 */
public class CacheKeyThreadLocal {

    /**
     * 当地的
     */
    private static final ThreadLocal<String> LOCAL = new ThreadLocal<>();

    /**
     * 复述,关键线程本地
     */
    private CacheKeyThreadLocal() {
    }

    /**
     * 获取值
     *
     * @return {@link String}
     */
    public static String get() {
        return LOCAL.get();
    }

    /**
     * 设置值
     *
     * @param redisKey redis的key
     */
    public static void set(String redisKey) {
        LOCAL.set(redisKey);
    }

    /**
     * 删除
     */
    public static void remove() {
        LOCAL.remove();
    }

}
