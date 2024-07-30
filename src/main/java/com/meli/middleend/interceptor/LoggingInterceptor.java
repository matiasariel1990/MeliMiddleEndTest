package com.meli.middleend.interceptor;

import com.meli.middleend.dto.LogElementDto;
import com.meli.middleend.dto.enums.TipoLogEnum;
import com.meli.middleend.service.LoggingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.util.StreamUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.Date;
import java.util.UUID;

@Component
public class LoggingInterceptor implements ClientHttpRequestInterceptor {

    @Autowired
    LoggingService loggingService;

    @Override
    public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {

        LogElementDto logElementDto = new LogElementDto();
        try{
            logElementDto.setRequestId(UUID.randomUUID().toString());
            logElementDto.setTipoLog(TipoLogEnum.LOG_BACK_APP);
            logElementDto.setRequestBody(new String(body, "UTF-8"));
            logElementDto.setEndpoint(request.getURI().toString());
            logElementDto.setHttpVerb(request.getMethod().toString());
            logElementDto.setFechaHoraRequest(Timestamp.from(Instant.now()));

        }catch (Exception e){

        }

        ClientHttpResponse clientHttpResponse = execution.execute(request, body);

        try{
            logElementDto.setFechaHoraResponse(Timestamp.from(Instant.now()));
            byte[] responseBody = StreamUtils.copyToByteArray(clientHttpResponse.getBody());
            String responseBodyString = new String(responseBody, StandardCharsets.UTF_8);
            logElementDto.setResponseBody(responseBodyString);
            logElementDto.setResultCode(clientHttpResponse.getStatusCode().toString());
            loggingService.logElement(logElementDto);
            return new BufferClientHttpResponse(clientHttpResponse, responseBody);
        }catch (Exception e){
            return clientHttpResponse;
        }

    }
}
