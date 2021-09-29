package com.ganzhenghao.prsa.bean;

import lombok.Getter;
import lombok.Setter;

/**
 * 用于基于ConcurrentHashMap实现缓存的数据存储类
 *
 * @author Ganzhenghao
 * @version 1.0
 * @date 2021/9/28 17:35
 */
@Getter
@Setter
public class CacheData<T> {

    /**
     * 数据
     */
    private T data;

    /**
     * 创建时间
     */
    private long createTime;

    /**
     * 到期时间
     */
    private long expireTime;

    /**
     * 实际的过期时间  将过期时间的处理放到创建对象的时候 减少删除过期键值时的性能集中损耗
     * 值等于 createTime + expireTime
     */
    private long actualExpirationTime;


    public CacheData(long expireTime, T data) {
        this.data = data;
        this.expireTime = expireTime;
        this.createTime = System.currentTimeMillis();
        this.actualExpirationTime = createTime + expireTime;
    }


    public CacheData(long expireTime) {
        this.expireTime = expireTime;
        this.createTime = System.currentTimeMillis();
        this.actualExpirationTime = createTime + expireTime;
    }


    /**
     * 距离当前时间是否到期
     *
     * @return boolean
     */
    public boolean isExpire() {
        return System.currentTimeMillis() > actualExpirationTime;
    }

    /**
     * 距离某一确定时间是否到期
     *
     * @param time 现在时间
     * @return boolean
     */
    public boolean isExpire(long time) {
        return time >= actualExpirationTime;

    }


    @Override
    public String toString() {
        return "CacheData{" +
                "数据=" + data +
                ", 创建时间=" + createTime +
                ", 过期时间=" + expireTime +
                ", 实际过期时间=" + actualExpirationTime +
                '}';
    }


}
