package com.meli.middleend.exception;

import com.meli.middleend.dto.api.client.response.ResponseApiError;
import lombok.Getter;

@Getter
public class ServiceClientException extends RuntimeException{

    String message;
    String error;
    int status;

    public ServiceClientException(ResponseApiError responseApiError) {
        this.error = responseApiError.getError();
        this.message = responseApiError.getMessage();
        this.status = responseApiError.getStatus();
    }

    public ServiceClientException(String message) {
        this.message = message;
        this.status = 500;
    }

}
