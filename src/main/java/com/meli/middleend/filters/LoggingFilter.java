package com.meli.middleend.filters;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.meli.middleend.dto.LogElementDto;
import com.meli.middleend.dto.enums.TipoLogEnum;
import com.meli.middleend.service.LoggingService;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

import java.io.IOException;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

public class LoggingFilter implements Filter {


    private static final Logger log = LoggerFactory.getLogger(LoggingFilter.class);

    LoggingService loggingService;

    public LoggingFilter(LoggingService loggingService){
        this.loggingService = loggingService;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        ContentCachingRequestWrapper wrappedRequest = new ContentCachingRequestWrapper(httpRequest);
        ContentCachingResponseWrapper wrappedResponse = new ContentCachingResponseWrapper((HttpServletResponse) response);
        LogElementDto logElementDto = new LogElementDto();
        logElementDto.setTipoLog(TipoLogEnum.LOG_APP);
        logElementDto.setRequestId(UUID.randomUUID().toString());

        try{
            //Extract request info
            logElementDto.setFechaHoraRequest(Timestamp.from(Instant.now()));
            logElementDto.setHttpVerb(httpRequest.getMethod());
            logElementDto.setEndpoint(httpRequest.getRequestURI());
            String requestJson = new String(wrappedRequest.getContentAsByteArray(), wrappedRequest.getCharacterEncoding());
            logElementDto.setRequestBody(formatJson(requestJson));
            Iterator headerI = httpRequest.getHeaderNames().asIterator();
            String headerList = "";
            while(headerI.hasNext()){
                String headerName = headerI.next().toString();
                String headerValue = httpRequest.getHeader(headerName);
                headerList = headerList.concat(headerName+": '"+ headerValue +"' - ");
            }
            logElementDto.setRequestHeaders(headerList);
        }catch (Exception e){

        }


        chain.doFilter(request, wrappedResponse);


        try{
            String responseBody = new String(wrappedResponse.getContentAsByteArray(), wrappedResponse.getCharacterEncoding());

            logElementDto.setResultCode(String.valueOf(wrappedResponse.getStatus()));
            logElementDto.setFechaHoraResponse(Timestamp.from(Instant.now()));
            //Extract response headers
            List<String> headersResponseList =
            wrappedResponse.getHeaderNames().stream().map(
                    header -> header.concat(": " + Objects.requireNonNull(wrappedResponse.getHeader(header)))
            ).toList();
            String headerResponse = "";
            for(String headers : headersResponseList){
                headerResponse = headerResponse.concat(headers + " - ");
            }
            logElementDto.setResponseHeaders(headerResponse);
            logElementDto.setResponseBody(formatJson(responseBody));


        }catch (Exception e){

        }
        wrappedResponse.copyBodyToResponse();

        loggingService.logElement(logElementDto);

    }


    private String formatJson(String jsonString) {
        ObjectMapper mapper = new ObjectMapper();
        Object jsonResponse;
        try {
            jsonResponse = mapper.readValue(jsonString, Object.class);
            ObjectWriter writer = mapper.writerWithDefaultPrettyPrinter();
            return writer.writeValueAsString(jsonResponse);
        } catch (Exception e) {
            return jsonString;
        }
    }
}
