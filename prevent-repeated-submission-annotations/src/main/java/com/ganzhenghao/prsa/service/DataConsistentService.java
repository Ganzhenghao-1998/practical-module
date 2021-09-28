package com.ganzhenghao.prsa.service;

import javax.servlet.http.HttpServletRequest;

/**
 * 数据校验接口
 *
 * @author Ganzhenghao
 * @version 1.0
 * @date 2021/9/28 11:30
 */
public interface DataConsistentService {

    // todo 数据校验接口
    public boolean dataCheck(HttpServletRequest request, Class<?> clazz);

}
