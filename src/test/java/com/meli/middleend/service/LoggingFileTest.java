package com.meli.middleend.service;

import com.meli.middleend.dto.LogElementDto;
import com.meli.middleend.dto.enums.TipoLogEnum;
import com.meli.middleend.filters.LoggingFilter;
import com.meli.middleend.interceptor.LoggingInterceptor;
import com.meli.middleend.service.impl.LoggingFileImpl;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.Timestamp;
import java.time.Instant;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;


@ExtendWith(MockitoExtension.class)
public class LoggingFileTest {

    @InjectMocks
    LoggingFileImpl loggingFile;

    @Mock
    Logger loggerApp;

    @Mock
    Logger loggerBack;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        loggingFile = new LoggingFileImpl();
        loggingFile.setLoggerApp(loggerApp);
        loggingFile.setLoggerBack(loggerBack);
    }

    private LogElementDto createLogDtoMock(TipoLogEnum tipoLogEnum) {
        LogElementDto logDto = new LogElementDto();
        logDto.setTipoLog(tipoLogEnum);
        logDto.setRequestId("12345");
        logDto.setFechaHoraRequest(Timestamp.from(Instant.now().minusSeconds(1L)));
        logDto.setHttpVerb("GET");
        logDto.setEndpoint("/test");
        logDto.setRequestHeaders("Headers");
        logDto.setRequestBody("{\"key\":\"value\"}");
        logDto.setFechaHoraResponse(Timestamp.from(Instant.now()));
        logDto.setResultCode("200");
        logDto.setResponseHeaders("Headers");
        logDto.setResponseBody("{\"key\":\"value\"}");
        return logDto;
    }

    @Test
    public void useLogAppTest(){
        loggingFile.logElement(createLogDtoMock(TipoLogEnum.LOG_APP));
        verify(loggerApp).info(anyString());
        verifyNoInteractions(loggerBack);
    }

    @Test
    public void useLogAppBckTest(){
        loggingFile.logElement(createLogDtoMock(TipoLogEnum.LOG_BACK_APP));
        verify(loggerBack).info(anyString());
        verifyNoInteractions(loggerApp);
    }
}
