package com.ganzhenghao.prsa.init;

import com.ganzhenghao.prsa.annotation.NoRepeatCommit;
import com.ganzhenghao.prsa.exception.DataException;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 用于获取注解的默认参数
 *
 * @author Ganzhenghao
 * @version 1.0
 * @date 2021/9/28 9:51
 */
@Configuration
public class DefaultParameters {

    private static final Class<DefaultParameters> CLAZZ = DefaultParameters.class;


    /**
     * 获取@NoRepeatCommit注解的默认参数
     *
     * @return NoRepeatCommit
     */
    @NoRepeatCommit
    @Bean
    public NoRepeatCommit noRepeatCommit() {
        try {
            return CLAZZ.getMethod("noRepeatCommit").getAnnotation(NoRepeatCommit.class);
        } catch (Exception e) {
            throw new DataException("创建NoRepeatCommit默认注解对象失败!");
        }
    }


}
