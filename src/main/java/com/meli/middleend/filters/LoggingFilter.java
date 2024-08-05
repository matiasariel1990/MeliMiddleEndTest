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

import static com.meli.middleend.filters.FIlterSupport.isFromSwagger;
import static com.meli.middleend.utils.StringConstants.DOC_START_PATH;
import static com.meli.middleend.utils.StringConstants.SWAGGER_START_PATH;

public class LoggingFilter implements Filter {


    private static final Logger log = LoggerFactory.getLogger(LoggingFilter.class);

    LoggingService loggingService;

    public LoggingFilter(LoggingService loggingService){
        this.loggingService = loggingService;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;

        if (isFromSwagger(httpRequest.getRequestURI())) {
            chain.doFilter(request, response);
            return;
        }

        ContentCachingRequestWrapper wrappedRequest = new ContentCachingRequestWrapper(httpRequest);
        ContentCachingResponseWrapper wrappedResponse = new ContentCachingResponseWrapper((HttpServletResponse) response);

        LogElementDto logElementDto = new LogElementDto();
        logElementDto.setTipoLog(TipoLogEnum.LOG_APP);
        logElementDto.setRequestId(UUID.randomUUID().toString());

        try{
            LoggingFilterSupport.extractRequestInfoLog(logElementDto, wrappedRequest);
        }catch (Exception e){

        }
        chain.doFilter(request, wrappedResponse);
        try{
            LoggingFilterSupport.extractResponseInfoLog(logElementDto, wrappedResponse);

        }catch (Exception e){

        }
        wrappedResponse.copyBodyToResponse();

        loggingService.logElement(logElementDto);

    }




}
