package com.meli.middleend.filter;

import com.meli.middleend.dto.LogElementDto;
import com.meli.middleend.filters.LoggingFilterSupport;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

import java.io.UnsupportedEncodingException;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.Arrays;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

public class LoggingFilterSupportTest {

    @Mock
    private ContentCachingRequestWrapper wrapperRequest;

    @Mock
    private ContentCachingResponseWrapper wrappedResponse;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testExtratRequestInfoLog() throws UnsupportedEncodingException {
        LogElementDto logElementDto = new LogElementDto();
        String requestBody = "request body";
        String characterEncoding = "UTF-8";
        String httpVerb = "POST";
        String endpoint = "/apiMeli/unReqourse";

        when(wrapperRequest.getContentAsByteArray()).thenReturn(requestBody.getBytes(characterEncoding));
        when(wrapperRequest.getCharacterEncoding()).thenReturn(characterEncoding);
        when(wrapperRequest.getMethod()).thenReturn(httpVerb);
        when(wrapperRequest.getRequestURI()).thenReturn(endpoint);
        List<String> headerNamesList = Arrays.asList("Content-Type", "User-Agent");
        Enumeration<String> headerNames = Collections.enumeration(headerNamesList);
        when(wrapperRequest.getHeaderNames()).thenReturn(headerNames);
        when(wrapperRequest.getHeader("Content-Type")).thenReturn("application/json");
        when(wrapperRequest.getHeader("User-Agent")).thenReturn("Mozilla/5.0");

        LoggingFilterSupport.extractRequestInfoLog(logElementDto, wrapperRequest);

        assertEquals(httpVerb, logElementDto.getHttpVerb());
        assertEquals(endpoint, logElementDto.getEndpoint());
        assertEquals(requestBody, logElementDto.getRequestBody());
        assertEquals("Content-Type: 'application/json' - User-Agent: 'Mozilla/5.0' - ", logElementDto.getRequestHeaders());


    }


    @Test
    public void testExtractResponseInfoLog() throws UnsupportedEncodingException {
        String responseBody = "response body mock";
        String characterEncoding = "UTF-8";
        int status = 200;

        LogElementDto logElementDto = new LogElementDto();
        when(wrappedResponse.getContentAsByteArray()).thenReturn(responseBody.getBytes(characterEncoding));
        when(wrappedResponse.getCharacterEncoding()).thenReturn(characterEncoding);
        when(wrappedResponse.getStatus()).thenReturn(status);
        when(wrappedResponse.getHeaderNames()).thenReturn(Arrays.asList("Content-Type", "Content-Length"));
        when(wrappedResponse.getHeader("Content-Type")).thenReturn("application/json");
        when(wrappedResponse.getHeader("Content-Length")).thenReturn("123");

        LoggingFilterSupport.extractResponseInfoLog(logElementDto, wrappedResponse);

        assertEquals(String.valueOf(status), logElementDto.getResultCode());
        assertEquals("Content-Type: application/json - Content-Length: 123 - ", logElementDto.getResponseHeaders());
        assertEquals(responseBody, logElementDto.getResponseBody());
    }
}
