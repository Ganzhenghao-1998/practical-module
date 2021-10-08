package com.ganzhenghao.prsa.service;

/**
 * 数据生成接口
 *
 * @author Ganzhenghao
 * @version 1.0
 * @date 2021/10/8 9:44
 */
@FunctionalInterface
public interface DataGenerate {

    /**
     * 数据生成
     *
     * @return {@link String}
     */
    public String data();

}
