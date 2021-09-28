package com.ganzhenghao.prsa.util;

import cn.hutool.core.text.StrBuilder;

/**
 * @author Ganzhenghao
 * @version 1.0
 * @date 2021/9/28 11:42
 */

public class CacheKeyUtil {

    private CacheKeyUtil() {
    }


    /**
     * 获取缓存键
     *
     * @param cacheKeyPrefix 缓存键前缀
     * @param args           arg游戏
     * @return {@link String}
     */
    public static String getCacheKey(String cacheKeyPrefix, String... args) {
        StrBuilder sb = new StrBuilder(cacheKeyPrefix);
        for (String arg : args) {
            sb.append(":").append(arg);
        }
        return sb.toString();
    }

}
