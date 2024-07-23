package com.meli.middleend.dto.api.client.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResponseApiError {
    String message;
    String error;
    int status;
}
