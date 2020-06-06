package com.rabbit.common.exception;

import lombok.Getter;

/**
 * 统一异常处理
 */
@Getter
public class MyBatisRabbitPlugException extends RuntimeException {

    private static final String TAG="Mybatis-rabbit-plug -> ";

    public MyBatisRabbitPlugException(String message){
        super(TAG+message);
    }

}