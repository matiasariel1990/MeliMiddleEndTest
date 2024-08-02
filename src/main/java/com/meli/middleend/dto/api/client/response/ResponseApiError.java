package com.meli.middleend.dto.api.client.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ResponseApiError {
    String message;
    String error;
    int status;
}
