package com.ganzhenghao.prsa.config;

import com.ganzhenghao.prsa.enums.NoRepeatCommitType;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 防重复提交配置
 *
 * @author Ganzhenghao
 * @version 1.0
 * @date 2021/8/26 17:08
 */
@ConfigurationProperties("no.repeat.commit")
@Data
public class NoRepeatCommitConfig {

    /**
     * 全局防重复提交 header name
     */
    private String headerName = "noRepeatId";

    /**
     * 请求拦截后,全局统一状态码返回
     */
    private Integer status = 505;

    /**
     * 全局redis-key前缀
     */
    private String redisKeyPrefix = "NoRepeatCommit:";

    /**
     * 是否开启id生成Controller 默认为false
     */
    private Boolean openIdController = false;

    /**
     * 使用何种方式进行防重复提交
     */
    private NoRepeatCommitType noRepeatCommitType = NoRepeatCommitType.Redis;


}
