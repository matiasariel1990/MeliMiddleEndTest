package com.meli.middleend.config;

import com.meli.middleend.interceptor.LoggingInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;


@Configuration
public class AppConfig {

    @Value("${spring.security.userclient.token}")
    private String userclienttoken;

    @Value("${spring.security.usermock.token}")
    private String usermocktoken;

    @Autowired
    LoggingInterceptor loggingInterceptor;


    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder restTemplateBuilder){
        return restTemplateBuilder.interceptors(loggingInterceptor).build();
    }





}
