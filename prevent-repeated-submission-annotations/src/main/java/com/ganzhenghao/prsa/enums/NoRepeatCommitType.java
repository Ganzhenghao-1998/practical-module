package com.ganzhenghao.prsa.enums;

/**
 * 使用何种防重复提交注解的枚举类
 *
 * @author Ganzhenghao
 * @version 1.0
 * @date 2021/9/27 16:28
 */
public enum NoRepeatCommitType {

    Redis("使用redis进行防重复提交Key的存储判断"),
    Internal("使用内置的缓存机制进行防重复提交Key的存储判断"),
    Mysql("使用Mysql进行防重复提交Key的存储判断"),
    Lock("加Sync锁"),
    Distributed_Locks("加分布式锁"),
    Hash("进行数据Hash判断"),
    Data("进行数据校验");

    NoRepeatCommitType(String type) {
    }
}
