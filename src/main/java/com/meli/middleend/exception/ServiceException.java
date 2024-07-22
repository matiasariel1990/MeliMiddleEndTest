package com.meli.middleend.exception;


public class ServiceException extends RuntimeException{

    String code;
    String message;

    public ServiceException(String message) {
        this.code = "Generic";
        this.message = message;
    }
}
