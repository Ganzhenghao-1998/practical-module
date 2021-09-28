package com.example.prevent.controller;

import com.ganzhenghao.prsa.annotation.NoRepeatCommit;
import com.ganzhenghao.prsa.util.RedisKeyThreadLocal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.TimeUnit;

/**
 * @author Ganzhenghao
 * @version 1.0
 * @date 2021/9/28 10:30
 */
@RestController
@RequestMapping("/no")
public class NoRepeatController {


    @PostMapping("/m1")
    @NoRepeatCommit(
            expireTime = 2,
            status = 401,
            redisKeyPrefix = "m1:",
            headerName = "m1",
            timeUnit = TimeUnit.MINUTES
    )
    public String m1() {
        try {
            Thread.sleep(5000L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return "success  -->" + RedisKeyThreadLocal.get();
    }

    @PostMapping("/m2")
    @NoRepeatCommit
    public String m2() {
        return "success  -->" + RedisKeyThreadLocal.get();
    }

}
