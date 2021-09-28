package com.ganzhenghao.prsa.redis;


import com.ganzhenghao.prsa.exception.DataException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;


/**
 * Redis校验id是否存在
 *
 * @author Ganzhenghao
 * @version 1.0
 * @date 2021/8/24 11:14
 */

@Component
public class RedisCheck {

    @Autowired
    private StringRedisTemplate redisTemplate;

    /**
     * 检查id存在
     * 检查id是否存在
     *
     * @param id             id
     * @param redisKeyPrefix redisKey的前缀
     * @param time           时间
     * @param unit           单位
     * @return boolean 设置id成功返回true 失败返回false
     */
    public boolean checkIdExist(String id, String redisKeyPrefix, Integer time, TimeUnit unit) {
        return checkIdExist(id, redisKeyPrefix, time, unit, "");
    }


    /**
     * 检查id存在
     * 检查id是否存在
     *
     * @param id             id
     * @param redisKeyPrefix redisKey的前缀
     * @param time           时间
     * @param unit           单位
     * @param data           数据
     * @return boolean 设置id成功返回true 失败返回false
     */
    public boolean checkIdExist(String id, String redisKeyPrefix, Integer time, TimeUnit unit, String data) {

        if (id == null || redisKeyPrefix == null || redisKeyPrefix.length() == 0 || time == null || unit == null) {
            throw new DataException("数据异常");
        }
        return Boolean.TRUE.equals(redisTemplate.opsForValue().setIfAbsent(redisKeyPrefix + id, data, time, unit));
    }


}

