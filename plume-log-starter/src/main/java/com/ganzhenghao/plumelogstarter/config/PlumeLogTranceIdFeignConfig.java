package com.ganzhenghao.plumelogstarter.config;

import feign.RequestInterceptor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;

/**
 * @author Ganzhenghao
 * @version 1.0
 * @date 2021/10/21 14:31
 */
@Configuration
public class PlumeLogTranceIdFeignConfig {

    @Bean("requestInterceptor")
    @ConditionalOnProperty(prefix = "plumelog.config", name = {"enable-trace"}, havingValue = "true", matchIfMissing = false)
    public RequestInterceptor requestInterceptor() {
        return template -> {
            ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            HttpServletRequest request = attributes.getRequest();
            Enumeration<String> headerNames = request.getHeaderNames();
            if (headerNames != null) {
                while (headerNames.hasMoreElements()) {
                    String name = (String) headerNames.nextElement();
                    String values = request.getHeader(name);
                    template.header(name, values);
                }
            }
        };
    }

}
