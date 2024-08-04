package com.meli.middleend.filters;

import com.meli.middleend.dto.LogElementDto;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

import java.io.UnsupportedEncodingException;
import java.net.http.HttpRequest;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

public class LoggingFilterSupport {

    public static void extractRequestInfoLog(LogElementDto logElementDto, ContentCachingRequestWrapper wrapperRequest) throws UnsupportedEncodingException {
        logElementDto.setFechaHoraRequest(Timestamp.from(Instant.now()));
        logElementDto.setHttpVerb(wrapperRequest.getMethod());
        logElementDto.setEndpoint(wrapperRequest.getRequestURI());
        String requestJson = new String(wrapperRequest.getContentAsByteArray(), wrapperRequest.getCharacterEncoding());
        logElementDto.setRequestBody(requestJson);
        Iterator headerI = wrapperRequest.getHeaderNames().asIterator();
        String headerList = "";
        while(headerI.hasNext()){
            String headerName = headerI.next().toString();
            String headerValue = wrapperRequest.getHeader(headerName);
            headerList = headerList.concat(headerName+": '"+ headerValue +"' - ");
        }
        logElementDto.setRequestHeaders(headerList);
    }

    public static void extractResponseInfoLog(LogElementDto logElementDto, ContentCachingResponseWrapper wrappedResponse) throws UnsupportedEncodingException {
        String responseBody = new String(wrappedResponse.getContentAsByteArray(), wrappedResponse.getCharacterEncoding());
        logElementDto.setResultCode(String.valueOf(wrappedResponse.getStatus()));
        logElementDto.setFechaHoraResponse(Timestamp.from(Instant.now()));
        List<String> headersResponseList =
                wrappedResponse.getHeaderNames().stream().map(
                        header -> header.concat(": " + Objects.requireNonNull(wrappedResponse.getHeader(header)))
                ).toList();
        String headerResponse = "";
        for(String headers : headersResponseList){
            headerResponse = headerResponse.concat(headers + " - ");
        }
        logElementDto.setResponseHeaders(headerResponse);
        logElementDto.setResponseBody(responseBody);
    }
}
