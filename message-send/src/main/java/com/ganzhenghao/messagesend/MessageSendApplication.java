package com.ganzhenghao.messagesend;

import com.ganzhenghao.messagesend.config.WeChatConfig;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan("com.ganzhenghao.messagesend")
@EnableConfigurationProperties(
        {
                WeChatConfig.class
        }
)
public class MessageSendApplication {

}
