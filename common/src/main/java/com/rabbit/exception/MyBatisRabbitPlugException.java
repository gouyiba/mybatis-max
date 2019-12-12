package com.rabbit.exception;

import lombok.Getter;

@Getter
public class MyBatisRabbitPlugException extends RuntimeException {

    public MyBatisRabbitPlugException(String message){
        super(message);
    }

}