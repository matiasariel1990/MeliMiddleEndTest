package com.meli.middleend.dto;

import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

@Getter
@Setter
public class LogElementDto {
    String requestId;
    Timestamp fechaHoraRequest;
    String httpVerb;
    String endpoint;
    String requestHeaders;
    String requestBody;


    Timestamp fechaHoraResponse;
    String resultCode;
    String responseHeaders;
    String responseBody;

    String timeReponse;
}
