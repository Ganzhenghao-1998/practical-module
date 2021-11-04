package com.ganzhenghao.messagesend.service;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.resource.ResourceUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.ganzhenghao.messagesend.config.WeChatConfig;
import com.ganzhenghao.messagesend.enums.MessageType;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

/**
 * 企业微信发送服务
 *
 * @author Ganzhenghao
 * @version 1.0
 * @date 2021/11/4 14:13
 */
@Service
@Slf4j
public class WeChatService {

    /**
     * 接收机通过id参数名称
     */
    private final String receiverByIdParamName = "mentioned_list";

    /**
     * 接收机通过移动参数名称
     */
    private final String receiverByMobileParamName = "mentioned_mobile_list";

    /**
     * 内容的名字
     */
    private final String contentName = "content";

    @Autowired
    private WeChatConfig weChatConfig;

    /**
     * 模板
     */
    private String template = "";

    @PostConstruct
    public void init() {

        template = ResourceUtil.readUtf8Str("wechat.txt");

        if (StrUtil.isEmpty(template)) {
            throw new RuntimeException("模板内容为空");
        }

    }


    /**
     * 发送消息
     *
     * @param params            参数
     * @param type              消息类型
     * @param receiversById     接收器通过id
     * @param receiversByMobile 接收器通过手机
     * @return boolean
     */
    public boolean sendMessage(Map<String, String> params, MessageType type, List<String> receiversById, List<String> receiversByMobile) {

        Map<String, Object> outside = new HashMap<>();
        outside.put("msgtype", type.toString());

        Map<String, Object> inside = new HashMap<>();

        if (ObjectUtil.isAllEmpty(receiversById, receiversByMobile)) {
            inside.put(receiverByIdParamName, new String[]{"@all"});
        } else {

            if (ObjectUtil.isNotEmpty(receiversById)) {
                inside.put(receiverByIdParamName, receiversById);
            }

            if (ObjectUtil.isNotEmpty(receiversByMobile)) {
                inside.put(receiverByMobileParamName, receiversByMobile);
            }
        }

        // 替换模板参数
        if (ObjectUtil.isEmpty(params)) {
            inside.put(contentName, template);
        } else {

            AtomicReference<String> temporaryTemplate = new AtomicReference<>(template);
            params.forEach((key, value) -> {
                temporaryTemplate.set(StrUtil.replace(temporaryTemplate.get(), joint(key), value));
            });
            inside.put(contentName, temporaryTemplate.get());

        }

        outside.put(type.toString(), inside);


        String msg = JSON.toJSONString(outside);


        String body = HttpRequest.post(weChatConfig.getHook())
                .body(msg)
                .execute()
                .body();

        return sendResult(body);

    }

    private boolean sendResult(String body) {
        SendResult sendResult = JSON.parseObject(body, SendResult.class);

        if (sendResult == null) {
            log.error("发送微信失败. 未知错误!");
            return false;
        }

        if (StrUtil.equals("ok", sendResult.getErrmsg())) {
            return true;
        } else {
            log.error("发送微信异常. 错误信息: " + sendResult.getErrmsg());
            return false;
        }
    }

    public boolean sendFile(File file) {

        String url = "https://qyapi.weixin.qq.com/cgi-bin/webhook/upload_media?" +
                weChatConfig.getHook().substring(weChatConfig.getHook().lastIndexOf("key="))
                + "&type=file";

        HashMap<String, Object> paramMap = new HashMap<>();
        //文件上传只需将参数中的键指定（默认file），值设为文件对象即可，对于使用者来说，文件上传与普通表单提交并无区别
        paramMap.put("file", file);

        String result = HttpUtil.post(url, paramMap);

        JSONObject jsonObject = JSON.parseObject(result);

        if ("ok".equals(jsonObject.get("errmsg"))) {
            String mediaId = (String) jsonObject.get("media_id");

            Map<String, Object> outside = new HashMap<>();
            Map<String, Object> inside = new HashMap<>();
            outside.put("msgtype", MessageType.file.toString());
            inside.put("media_id", mediaId);
            outside.put(MessageType.file.toString(), inside);

            String post = HttpUtil.post(weChatConfig.getHook(), JSON.toJSONString(outside));

            return sendResult(post);
        }

        /**
         * {
         *     "msgtype": "file",
         *     "file": {
         *          "media_id": "3a8asd892asd8asd"
         *     }
         * }
         */


        return false;

    }

    public boolean sendFile(String filePath) {

        return sendFile(FileUtil.file(filePath));

    }


    private String joint(String key) {
        return "#{" + key + "}";
    }

    @Getter
    @Setter
    static class SendResult {
        private int errcode;
        private String errmsg;
    }

}

