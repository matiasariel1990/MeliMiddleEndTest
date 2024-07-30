package com.meli.middleend.service.impl;

import com.meli.middleend.dto.LogElementDto;
import com.meli.middleend.dto.enums.TipoLogEnum;
import com.meli.middleend.filters.LoggingFilter;
import com.meli.middleend.interceptor.LoggingInterceptor;
import com.meli.middleend.service.LoggingService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;

@Service
public class LoggingFileImpl implements LoggingService {

    private static final Logger loggerApp = LogManager.getLogger(LoggingFilter.class);
    private static final Logger loggerBack = LogManager.getLogger(LoggingInterceptor.class);
    @Override
    public void logElement(LogElementDto logDto) {

        Logger logger = null;

        if(logDto.getTipoLog().equals(TipoLogEnum.LOG_APP)){
            logger = loggerApp;
        }
        if(logDto.getTipoLog().equals(TipoLogEnum.LOG_BACK_APP)){
            logger = loggerBack;
        }

        String requestLog =
                MessageFormat.format("\nLog Request {0}: \n" +
                        "Timespam: {1} \n" +
                        "Method: {2} - {3} \n" +
                        "Headers : {4}" +
                        "Body: {5} \n", logDto.getRequestId(),
                        logDto.getFechaHoraRequest(), logDto.getHttpVerb(), logDto.getEndpoint(),
                        logDto.getRequestHeaders(), logDto.getRequestBody());

        String responseLog =
                MessageFormat.format("Log Response {0}: \n" +
                                "Timespam Response: {1} \n" +
                                "Response Code: {2} \n" +
                                "Headers : {3} \n" +
                                "Body: {4} \n" +
                        "Response time: {5} ms\n", logDto.getRequestId(),
                        logDto.getFechaHoraResponse(), logDto.getResultCode(), logDto.getResponseHeaders(),
                        logDto.getResponseBody(), logDto.getTimeDif());

        String logResult = requestLog.concat(responseLog);

        logger.info(logResult);
    }
}
