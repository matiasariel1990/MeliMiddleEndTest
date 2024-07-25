package com.meli.middleend.config;

import com.meli.middleend.service.ItemService;
import com.meli.middleend.service.impl.ItemServiceImpl;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class AppConfig {

    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder restTemplateBuilder){
        return restTemplateBuilder.interceptors().build();
    }

    @Bean
    public ItemService itemService(){
        return new ItemServiceImpl();
    }
}
