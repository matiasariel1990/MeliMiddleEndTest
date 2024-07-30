package com.meli.middleend.interceptor;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

public class BufferClientHttpResponse implements ClientHttpResponse {

    private ClientHttpResponse origin;
    private byte[] body;

    public BufferClientHttpResponse(ClientHttpResponse origin, byte[] body){
        this.origin = origin;
        this.body = body;
    }

    @Override
    public HttpStatusCode getStatusCode() throws IOException {
        return origin.getStatusCode();
    }

    @Override
    public String getStatusText() throws IOException {
        return origin.getStatusText();
    }

    @Override
    public void close() {
        origin.close();
    }

    @Override
    public InputStream getBody() throws IOException {
        return new ByteArrayInputStream(body);
    }

    @Override
    public HttpHeaders getHeaders() {
        return origin.getHeaders();
    }
}
