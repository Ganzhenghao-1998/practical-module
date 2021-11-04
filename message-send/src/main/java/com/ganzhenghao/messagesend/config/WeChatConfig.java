package com.ganzhenghao.messagesend.config;

import com.ganzhenghao.messagesend.enums.MessageType;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 企业微信发送配置类
 *
 * @author Ganzhenghao
 * @version 1.0
 * @date 2021/11/4 14:07
 */
@ConfigurationProperties(prefix = "message.wechat.config")
@Data
public class WeChatConfig {

    /**
     * 消息类型
     */
    private MessageType messageType;

    private String hook;

}
