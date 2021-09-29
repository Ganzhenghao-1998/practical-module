package com.ganzhenghao.prsa.config;


import com.ganzhenghao.prsa.interceptors.NoRepeatCommitInterceptor;
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
public class NoRepeatCommitInterceptorConfig implements WebMvcConfigurer {

    @Autowired
    private NoRepeatCommitInterceptor noRepeatCommitInterceptor;

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
            case Internal_Hutool:
            case Internal_ConcurrentHashMap:
                registry.addInterceptor(noRepeatCommitInterceptor).addPathPatterns("/**");
                break;

        }


    }

}
