package com.meli.middleend.config;

import com.meli.middleend.dto.enums.UserEnum;
import com.meli.middleend.filters.AuthFilter;
import com.meli.middleend.filters.LoggingFilter;
import com.meli.middleend.service.ItemService;
import com.meli.middleend.service.impl.ItemServiceImpl;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;


@Configuration
public class AppConfig {

    @Value("${spring.security.userclient.token}")
    private String userclienttoken;

    @Value("${spring.security.usermock.token}")
    private String usermocktoken;


    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder restTemplateBuilder){
        return restTemplateBuilder.interceptors().build();
    }


}
