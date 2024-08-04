package com.meli.middleend.exception;

import lombok.Getter;

@Getter
public class ServiceException extends RuntimeException{

    String code;
    String message;

    public ServiceException(String message) {
        this.code = "Generic";
        this.message = message;
    }

}
