package com.example.prevent;

import com.ganzhenghao.prsa.annotation.NoRepeatCommit;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class PreventTestApplicationTests {

    @Autowired
    private NoRepeatCommit noRepeatCommit;

    @Test
    void contextLoads() {
    }

    @Test
    public void testMethodName() {
        System.out.println(noRepeatCommit.cacheKeyPrefix());
        System.out.println(noRepeatCommit.headerName());
    }

}
