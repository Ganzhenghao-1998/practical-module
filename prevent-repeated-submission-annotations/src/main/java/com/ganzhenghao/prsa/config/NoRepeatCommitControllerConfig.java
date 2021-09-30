package com.ganzhenghao.prsa.config;

import lombok.Data;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author Ganzhenghao
 * @version 1.0
 * @date 2021/9/30 9:17
 */
@ConfigurationProperties("no.repeat.commit.id-controller")
@Data
@ConditionalOnExpression("${no.repeat.commit.open-id-controller}")
public class NoRepeatCommitControllerConfig {

    /*
    这里的配置其实没有用,具体的默认值写到NoRepeatIdController的注解上的,
    在这里定义,只是为了让idea提示
     */

    /**
     * 头的路径
     */
    private String controllerPath = "/id";

    private String getIdPath = "/getId";

    // todo 获取id的种类 雪花 uuid 用户自定义id生成器

}
