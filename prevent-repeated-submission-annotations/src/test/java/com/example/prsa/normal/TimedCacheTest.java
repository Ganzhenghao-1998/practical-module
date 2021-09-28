package com.example.prsa.normal;

import cn.hutool.cache.CacheUtil;
import cn.hutool.cache.impl.TimedCache;
import org.junit.jupiter.api.Test;

/**
 * @author Ganzhenghao
 * @version 1.0
 * @date 2021/9/28 14:42
 */
public class TimedCacheTest {

    TimedCache<String, String> timedCache = CacheUtil.newTimedCache(1000 * 10);

    /**
     * 测试 获取缓存是缓存依过期但是未删除 是否还能获取到
     */
    @Test
    public void cacheTest() throws InterruptedException {

        TimedCache<Object, Object> timedCache = CacheUtil.newTimedCache(1000 * 10);
        //timedCache.schedulePrune(1000 * 60 * 5);
        timedCache.put("1", "1");
        timedCache.put("2", "2");
        System.out.println(timedCache.get("1"));
        timedCache.put("2", "two");

        System.out.println(timedCache.get("2"));
        System.out.println(" start sleep ... ");
        Thread.sleep(1000 * 10);

        System.out.println(timedCache.get("1"));
    }

    @Test
    public void cacheObjTest() throws InterruptedException {

        timedCache.put("1", "");
        timedCache.put("2", "1");


        new Thread(() -> {

            synchronized (timedCache.get("1")) {
                for (int i = 0; i < 15; i++) {
                    System.out.println("thread 1 = > " + i);
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }

        }).start();

        new Thread(() -> {
            synchronized (timedCache.get("2")) {
                for (int i = 0; i < 15; i++) {
                    System.out.println("thread 2 = > " + i);
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();


        Thread.currentThread().join();

    }


}
