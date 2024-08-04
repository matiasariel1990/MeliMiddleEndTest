package com.meli.middleend.exception;

import com.meli.middleend.controller.ItemController;
import com.meli.middleend.dto.ResponseError;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.ResourceAccessException;

import static com.meli.middleend.utils.StringConstants.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

public class HanderExceptionTest {

    private static final String ERROR_MESSAGE = "MessageError";

    HandlerException handlerException;

    @BeforeEach
    public void setUp(){
        handlerException = new HandlerException();
    }

    @Test
    public void validationExceptionTest(){
        ValidationException validationException = new ValidationException(ERROR_MESSAGE);
        ResponseEntity<Object> responseEntity = handlerException.handleServException(validationException);
        assertNotNull(responseEntity);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, responseEntity.getStatusCode());
        Object body = responseEntity.getBody();
        assertInstanceOf(ResponseError.class, body);
        ResponseError responseError = (ResponseError) body;
        assertEquals(ERROR_VALIDACION, responseError.getCode());
        assertEquals(ERROR_MESSAGE, responseError.getMessage());
    }

    @Test
    public void serviceExceptionTest(){
        ServiceException serviceException = new ServiceException(ERROR_MESSAGE);
        ResponseEntity<Object> responseEntity = handlerException.handleServException(serviceException);
        assertNotNull(responseEntity);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, responseEntity.getStatusCode());
        Object body = responseEntity.getBody();
        assertInstanceOf(ResponseError.class, body);
        ResponseError responseError = (ResponseError) body;
        assertEquals(ERROR_SERV, responseError.getCode());
        assertEquals(ERROR_MESSAGE, responseError.getMessage());
    }

    @Test
    public void handleClientExceptionTest(){
        ServiceClientException serviceClientException = new ServiceClientException(ERROR_MESSAGE);
        ResponseEntity<Object> responseEntity = handlerException.handleClientException(serviceClientException);
        assertNotNull(responseEntity);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, responseEntity.getStatusCode());
        Object body = responseEntity.getBody();
        assertInstanceOf(ResponseError.class, body);
        ResponseError responseError = (ResponseError) body;
        assertEquals(ERROR_CLIENT, responseError.getCode());
        assertEquals(ERROR_CLIENT_MESSAGE, responseError.getMessage());
    }

    @Test
    public void handleExceptionTest(){
        Exception ex = new Exception();
        ResponseEntity<Object> responseEntity = handlerException.handleGralException(ex);
        assertNotNull(responseEntity);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, responseEntity.getStatusCode());
        Object body = responseEntity.getBody();
        assertInstanceOf(ResponseError.class, body);
        ResponseError responseError = (ResponseError) body;
        assertEquals(GENERIC_CODE, responseError.getCode());
        assertEquals(ERROR_GENERICO, responseError.getMessage());
    }

    @Test
    public void handleResourceAccessExceptionTest(){
        ResourceAccessException rae = new ResourceAccessException("Not Found");
        ResponseEntity<Object> responseEntity = handlerException.handleResourseAccessException(rae);
        assertNotNull(responseEntity);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, responseEntity.getStatusCode());
        Object body = responseEntity.getBody();
        assertInstanceOf(ResponseError.class, body);
        ResponseError responseError = (ResponseError) body;
        assertEquals(CLIENT_CODE_RESOURSE_NOT_AVAIABLE, responseError.getCode());
        assertEquals(ERROR_GENERICO, responseError.getMessage());
    }


    @Test
    public void handleApiUnavailableStateExceptionTest(){
        ApiUnavailableStateException apiE = new ApiUnavailableStateException();
        ResponseEntity<Object> responseEntity = handlerException.handleClientException(apiE);
        assertNotNull(responseEntity);
        assertEquals(HttpStatus.SERVICE_UNAVAILABLE, responseEntity.getStatusCode());
        Object body = responseEntity.getBody();
        assertInstanceOf(ResponseError.class, body);
        ResponseError responseError = (ResponseError) body;
        assertEquals(CLIENT_NOT_AVAIABLE_CODE, responseError.getCode());
        assertEquals(CLIENT_RESOURSE_NOT_AVAIABLE_MESSAGE, responseError.getMessage());
    }





}
