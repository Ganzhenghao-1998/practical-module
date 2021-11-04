package com.example.prevent;

import com.ganzhenghao.messagesend.enums.MessageType;
import com.ganzhenghao.messagesend.service.WeChatService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.HashMap;
import java.util.Map;

@SpringBootTest
class PreventTestApplicationTests {


    @Autowired
    WeChatService weChatService;

    @Test
    void contextLoads() {
    }


    @Test
    public void message() {
        Map<String, String> params = new HashMap<>();

        params.put("name", "张三");
        params.put("country", "中国");
        params.put("phone", "10086");

        boolean boo = weChatService.sendMessage(params, MessageType.text, null, null);
    }

    @Test
    public void sendFile() {
        String file = "wechat.txt";

        boolean b = weChatService.sendFile(file);
        System.out.println(b);


    }

}
