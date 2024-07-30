package com.meli.middleend.dto;

import com.meli.middleend.dto.enums.TipoLogEnum;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

@Getter
@Setter
public class LogElementDto {
    String requestId;
    TipoLogEnum tipoLog;
    Timestamp fechaHoraRequest;
    String httpVerb;
    String endpoint;
    String requestHeaders;
    String requestBody;


    Timestamp fechaHoraResponse;
    String resultCode;
    String responseHeaders;
    String responseBody;

    public long getTimeDif(){
        return (fechaHoraResponse.getTime() - fechaHoraRequest.getTime());
    }
}
