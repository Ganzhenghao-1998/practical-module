package com.example.prsa.normal;

import org.junit.jupiter.api.Test;

import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Ganzhenghao
 * @version 1.0
 * @date 2021/9/30 14:12
 */
public class ConcurrentHashMapTest {


    @Test
    public void testMethodName() {

        ConcurrentHashMap<String, String> map = new ConcurrentHashMap<>();

        String key = "1";
        System.out.println(map.putIfAbsent(key, "2"));
        System.out.println(map.putIfAbsent(key, key));

    }

}
