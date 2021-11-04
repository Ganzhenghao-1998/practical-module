package com.ganzhenghao.messagesend;

import com.ganzhenghao.messagesend.config.WeChatConfig;
import org.springframework.boot.SpringApplication;
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

    public static void main(String[] args) {
        SpringApplication.run(MessageSendApplication.class, args);
    }

}
