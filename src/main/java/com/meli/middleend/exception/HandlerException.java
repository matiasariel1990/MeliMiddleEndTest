package com.meli.middleend.exception;

import com.meli.middleend.dto.ResponseError;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.client.ResourceAccessException;

import static com.meli.middleend.utils.StringConstants.*;


@ControllerAdvice
public class HandlerException {

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
