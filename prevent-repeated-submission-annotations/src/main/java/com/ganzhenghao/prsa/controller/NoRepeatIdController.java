package com.ganzhenghao.prsa.controller;

import cn.hutool.core.util.IdUtil;
import com.ganzhenghao.prsa.service.CacheIdGenerate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * 获取唯一id
 *
 * @author Ganzhenghao
 * @version 1.0
 * @date 2021/9/27 16:01
 */
@RestController
@RequestMapping("${no.repeat.commit.id-controller.controller-path:id}")
@ConditionalOnProperty(prefix = "no.repeat.commit", name = {"open-id-controller"}, havingValue = "true", matchIfMissing = false)
public class NoRepeatIdController {

    @Autowired(required = false)
    private CacheIdGenerate cacheIdGenerate;

    @RequestMapping(value = "${no.repeat.commit.id-controller.get-id-path:getId}",
            method = {RequestMethod.GET, RequestMethod.POST})
    public Map<String, String> getSpecialId() {

        //todo 可以加入属性检测 检查header头等等

        Map<String, String> map = new HashMap<>(4);
        String id = null;
        if (cacheIdGenerate == null) {
            // 使用糊涂工具生成唯一id   todo 自己实现唯一id算法
            id = IdUtil.getSnowflake().nextIdStr();
        } else {
            id = cacheIdGenerate.id();
        }

        // todo 存入缓存机制,然后修改目前的存储逻辑

        map.put("id", id);

        return map;
    }


}
