package com.meli.middleend.exception;

import com.meli.middleend.dto.ResponseError;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class HandlerException {

    private static final String ERROR_VALIDACION = "ERR_VAL";

    @ExceptionHandler(value
            =  AuthException.class )
    protected ResponseEntity<Object> handleConflict(
            AuthException ex) {
        String bodyOfResponse = "No tienes los permisos.";
        return new ResponseEntity<>(bodyOfResponse, HttpStatus.UNAUTHORIZED);
    }



}
