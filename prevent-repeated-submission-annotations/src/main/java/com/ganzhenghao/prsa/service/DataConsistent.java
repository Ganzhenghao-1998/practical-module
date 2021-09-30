package com.ganzhenghao.prsa.service;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * 数据校验接口
 *
 * @author Ganzhenghao
 * @version 1.0
 * @date 2021/9/28 11:30
 */
public interface DataConsistent {

    //数据校验接口
    public boolean dataCheck(HttpServletRequest request, Class<?> clazz);

    public boolean dataCheck(Object... arg);

    public boolean dataCheck(Map<Object, Object> dataMap);

    //数据处理接口

    public String dataHandle(HttpServletRequest request, Class<?> clazz);

    public String dataHandle(Object... arg);

    public String dataHandle(Map<Object, Object> dataMap);

}
