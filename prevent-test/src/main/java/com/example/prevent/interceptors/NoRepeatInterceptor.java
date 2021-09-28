package com.example.prevent.interceptors;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 防重复提交的SpringMVC拦截器
 *
 * @author Ganzhenghao
 * @version 1.0
 * @date 2021/8/24 11:57
 */
@ControllerAdvice
public class NoRepeatInterceptor implements HandlerInterceptor {




    /*    private Logger logger = LoggerFactory.getLogger(this.getClass());*/


    /**
     * 拦截前端请求,获取id
     *
     * @param request  请求
     * @param response 响应
     * @param handler  处理程序
     * @return boolean
     * @throws Exception 异常
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {


        // 判断是否为HandlerMethod 不是则放行
        if (!(handler instanceof HandlerMethod)) {
            return true;
        }

        HandlerMethod methodHandler = (HandlerMethod) handler;


        return true;
    }


}
