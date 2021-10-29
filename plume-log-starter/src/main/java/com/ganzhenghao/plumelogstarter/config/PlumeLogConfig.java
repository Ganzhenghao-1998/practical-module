package com.ganzhenghao.plumelogstarter.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author Ganzhenghao
 * @version 1.0
 * @date 2021/10/29 10:32
 */
@ConfigurationProperties(prefix = "plumelog.config")
@Data
public class PlumeLogConfig {

    /**
     * 启用链路追踪
     */
    private boolean enableTrace = false;

    /**
     * 卡夫卡的主机  集群以;隔开49.234.138.177:19092;49.234.138.177:19092
     */
    private String kafkaHosts = "49.234.138.177:19092";


}
