package com.meli.middleend.exception;

import com.meli.middleend.dto.ResponseError;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.client.ResourceAccessException;

@ControllerAdvice
public class HandlerException {

    private static final String ERROR_VALIDACION = "ERR_VAL";
    private static final String ERROR_SERV = "ERR_SERV";
    private static final String ERROR_GENERICO = "An error has occurred.";
    private static final String GENERIC_CODE = "GENERIC_ERROR";
    private static final String ERROR_CLIENT = "RESOURSE_NOT_AVAILABLE";
    private static final String ERROR_CLIENT_MESSAGE = "The resource is unavailable.";
    private static final String CLIENT_RESOURSE_NOT_AVAIABLE_MESSAGE = "The resource is temporarily unavailable.";
    private static final String CLIENT_CODE_RESOURSE_NOT_AVAIABLE = "ERR_CON_CLI";
    private static final String CLIENT_NOT_AVAIABLE_CODE = "ERR_WAIT_CLIENT";

    @ExceptionHandler(value
            =  AuthException.class )
    protected ResponseEntity<Object> handleConflict(
            AuthException ex) {
        String bodyOfResponse = "No tienes los permisos.";
        return new ResponseEntity<>(bodyOfResponse, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(value = ValidationException.class)
    protected ResponseEntity<Object> handleServException(
            ValidationException ex) {
        ResponseError responseError = ResponseError.builder()
                .code(ERROR_VALIDACION)
                .message(ex.getMessage()).build();
        return new ResponseEntity<>(responseError, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(value = ServiceException.class)
    protected ResponseEntity<Object> handleServException(
            ServiceException ex) {
        ResponseError responseError = ResponseError.builder()
                .code(ERROR_SERV)
                .message(ex.getMessage()).build();
        return new ResponseEntity<>(responseError, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(value = ServiceClientException.class)
    protected ResponseEntity<Object> handleClientException(
            ServiceClientException ex) {
        ResponseError responseError = ResponseError.builder()
                .code(ERROR_CLIENT)
                .message(ERROR_CLIENT_MESSAGE).build();
        return new ResponseEntity<>(responseError, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(value = ApiUnavailableStateException.class)
    protected ResponseEntity<Object> handleClientException(
            ApiUnavailableStateException ex) {
        ResponseError responseError = ResponseError.builder()
                .code(CLIENT_NOT_AVAIABLE_CODE)
                .message(CLIENT_RESOURSE_NOT_AVAIABLE_MESSAGE).build();
        return new ResponseEntity<>(responseError, HttpStatus.SERVICE_UNAVAILABLE);
    }


    
    @ExceptionHandler(value = ResourceAccessException.class)
    protected ResponseEntity<Object> handleResourseAccessException(
            Exception ex) {
        ResponseError responseError = ResponseError.builder()
                .code(CLIENT_CODE_RESOURSE_NOT_AVAIABLE)
                .message(ERROR_GENERICO).build();
        return new ResponseEntity<>(responseError, HttpStatus.INTERNAL_SERVER_ERROR);
    }


    @ExceptionHandler(value = Exception.class)
    protected ResponseEntity<Object> handleGralException(
            Exception ex) {
        ResponseError responseError = ResponseError.builder()
                .code(GENERIC_CODE)
                .message(ERROR_GENERICO).build();
        return new ResponseEntity<>(responseError, HttpStatus.INTERNAL_SERVER_ERROR);
    }



}
