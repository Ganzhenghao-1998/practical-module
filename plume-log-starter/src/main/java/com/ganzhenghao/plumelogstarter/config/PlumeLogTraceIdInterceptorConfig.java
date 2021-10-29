package com.ganzhenghao.plumelogstarter.config;

import com.ganzhenghao.plumelogstarter.interceptors.PlumeLogTraceIdInterceptor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author Ganzhenghao
 * @version 1.0
 * @date 2021/10/21 14:03
 */
@Configuration
@ConditionalOnProperty(prefix = "plumelog.config", name = {"enable-trace"}, havingValue = "true", matchIfMissing = false)
@Slf4j
public class PlumeLogTraceIdInterceptorConfig implements WebMvcConfigurer {

    @Autowired
    private PlumeLogTraceIdInterceptor plumeLogTraceIdInterceptor;

    /**
     * 添加拦截器
     *
     * @param registry 注册表
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        log.info("链路追踪已开启................");
        registry.addInterceptor(plumeLogTraceIdInterceptor).addPathPatterns("/**");
    }

}
