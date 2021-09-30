package com.ganzhenghao.prsa.service;

import java.util.Map;

/**
 * 用于根据用户信息生成id
 *
 * @author Ganzhenghao
 * @version 1.0
 * @date 2021/9/29 16:06
 */
public interface CacheIdGenerate {


    /**
     * id生成
     *
     * @return {@link String}
     */
    public String id();


    /**
     * token生成
     *
     * @return {@link String}
     */
    public String token(Map<String, Object> map);

}
