package com.ganzhenghao.prsa.controller;

import cn.hutool.core.util.IdUtil;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.web.bind.annotation.RequestMapping;
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
@RequestMapping("/id")
@ConditionalOnExpression("${no.repeat.commit.open-id-controller}")
public class NoRepeatIdController {

    @RequestMapping("/getId")
    public Map<String, String> getSpecialId() {
        Map<String, String> map = new HashMap<>(2);
        // 使用糊涂工具生成唯一id   todo 自己实现唯一id算法
        String id = IdUtil.getSnowflake().nextIdStr();
        map.put("id", id);

        return map;
    }

}
