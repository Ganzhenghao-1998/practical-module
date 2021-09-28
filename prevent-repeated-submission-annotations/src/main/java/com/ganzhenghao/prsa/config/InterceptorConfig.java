package com.ganzhenghao.prsa.config;


import com.ganzhenghao.prsa.interceptors.NoRepeatInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * 拦截器配置
 *
 * @author Doraemon
 * @date 2021/08/26
 */
@Configuration
public class InterceptorConfig implements WebMvcConfigurer {

    @Autowired
    private NoRepeatInterceptor noRepeatInterceptor;

    @Autowired
    private NoRepeatCommitConfig noRepeatCommitConfig;

    /**
     * 添加拦截器
     *
     * @param registry 注册表
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {

        switch (noRepeatCommitConfig.getNoRepeatCommitType()) {
            case Redis:
                registry.addInterceptor(noRepeatInterceptor).addPathPatterns("/**");
                break;
        }


    }

}
