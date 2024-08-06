package com.meli.middleend.interceptor;

import com.meli.middleend.dto.LogElementDto;
import com.meli.middleend.service.LoggingService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.ClientHttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpResponse;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

public class LoggingInterceptorTest {

    @Mock
    private LoggingService loggingService;

    @InjectMocks
    private LoggingInterceptor loggingInterceptor;

    @Mock
    private ClientHttpRequest request;

    @Mock
    private ClientHttpResponse response;

    @Mock
    private ClientHttpRequestExecution execution;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void interceptOkTest() throws IOException, URISyntaxException {
        String requestBody = "{\"key\":\"aValue\"}";
        String responseBody = "{\"responseKey\":\"responseValue\"}";
        ByteArrayInputStream responseBodyStream = new ByteArrayInputStream(responseBody.getBytes(StandardCharsets.UTF_8));

        when(request.getURI()).thenReturn(new URI("http://uri/test"));
        when(request.getMethod()).thenReturn(HttpMethod.GET);
        when(execution.execute(request, requestBody.getBytes(StandardCharsets.UTF_8)))
                .thenReturn(response);

        when(response.getStatusCode()).thenReturn(HttpStatus.OK);
        when(response.getBody()).thenReturn(responseBodyStream);

        ClientHttpResponse actualResponse = loggingInterceptor.intercept(request, requestBody.getBytes(StandardCharsets.UTF_8), execution);

        assertNotNull(actualResponse);
        verify(loggingService, times(1)).logElement(any(LogElementDto.class));
    }

    @Test
    public void anExceptionNotBreacksFlowTest() throws IOException, URISyntaxException {

        String requestBody = "{\"key\":\"value\"}";
        when(request.getURI()).thenReturn(new URI("http://uri/test"));
        when(request.getMethod()).thenReturn(HttpMethod.GET);
        when(execution.execute(request, requestBody.getBytes(StandardCharsets.UTF_8)))
                .thenReturn(response);
        ByteArrayInputStream responseBodyStream = new ByteArrayInputStream(new byte[0]);

        when(response.getStatusCode()).thenReturn(HttpStatus.OK);
        when(response.getBody()).thenReturn(responseBodyStream);
        doThrow(new RuntimeException("Logging failed")).when(loggingService).logElement(any(LogElementDto.class));


        ClientHttpResponse actualResponse = loggingInterceptor.intercept(request, requestBody.getBytes(StandardCharsets.UTF_8), execution);

        assertNotNull(actualResponse);
        verify(loggingService, times(1)).logElement(any(LogElementDto.class));
    }



}
