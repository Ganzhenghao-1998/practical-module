package com.example.prsa.normal;

import org.junit.jupiter.api.Test;

import java.util.concurrent.TimeUnit;

/**
 * @author Ganzhenghao
 * @version 1.0
 * @date 2021/9/29 9:10
 */
public class EnumTest {


    @Test
    public void testMethodName() {

        System.out.println("TimeUnit.MINUTES == TimeUnit.MINUTES = " + (TimeUnit.MINUTES == TimeUnit.MINUTES));

        System.out.println("TimeUnit.MINUTES.equals(TimeUnit.MINUTES) = " + TimeUnit.MINUTES.equals(TimeUnit.MINUTES));


    }

}
