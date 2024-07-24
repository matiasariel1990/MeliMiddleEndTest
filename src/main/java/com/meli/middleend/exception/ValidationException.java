package com.meli.middleend.exception;

import lombok.Getter;

@Getter
public class ValidationException extends RuntimeException{

    String message;

    public ValidationException(String message) {
        this.message = message;
    }
}
