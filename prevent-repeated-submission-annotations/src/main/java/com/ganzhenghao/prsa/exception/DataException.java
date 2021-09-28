package com.ganzhenghao.prsa.exception;

/**
 * 数据异常类
 *
 * @author Ganzhenghao
 * @version 1.0
 * @date 2021/8/31 9:34
 */
public class DataException extends RuntimeException {


    /**
     * 数据异常
     *
     * @param message 消息
     */
    public DataException(String message) {
        super(message);
    }


}
