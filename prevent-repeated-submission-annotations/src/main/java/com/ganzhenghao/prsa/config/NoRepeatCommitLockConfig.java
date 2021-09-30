package com.ganzhenghao.prsa.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author Ganzhenghao
 * @version 1.0
 * @date 2021/9/30 11:01
 */

@ConfigurationProperties("no.repeat.commit.lock")
@Data
public class NoRepeatCommitLockConfig {

    /*
    这里的配置其实没有用,具体的默认值写到NoRepeatIdController的注解上的,
    在这里定义,只是为了让idea提示
     */

    /**
     * 点切表达式
     */
    private String pointCutExpression = "execution(* *..controller.*.*(..))";

}
