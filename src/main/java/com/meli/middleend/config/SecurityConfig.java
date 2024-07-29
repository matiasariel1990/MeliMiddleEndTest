package com.meli.middleend.config;

import com.meli.middleend.dto.enums.UserEnum;
import com.meli.middleend.filters.AuthFilter;
import com.meli.middleend.filters.LoggingFilter;
import com.meli.middleend.service.impl.LoggingFileImpl;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.intercept.AuthorizationFilter;

import java.util.HashMap;

import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;


@Configuration
public class SecurityConfig {


    @Value("${spring.security.userclient.token}")
    private String userclienttoken;

    @Value("${spring.security.usermock.token}")
    private String usermocktoken;


    private LoggingFilter loggingFilter;

    public SecurityConfig(){
        this.loggingFilter = new LoggingFilter(new LoggingFileImpl());
    }


    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        HashMap<String, UserDetails> tokenStore ;
        tokenStore = new HashMap<>();
        tokenStore.put(userclienttoken,
                User.withUsername(UserEnum.USER_CLIENT.getUserName()).password("").authorities(UserEnum.USER_CLIENT.getRole()).build());
        tokenStore.put(usermocktoken,
                User.withUsername(UserEnum.USER_MOCK.getUserName()).password("").authorities(UserEnum.USER_MOCK.getRole()).build());
        AuthFilter authFilter =  new AuthFilter(tokenStore);

        http.csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(authorizationManagerRequestMatcherRegistry ->
                        authorizationManagerRequestMatcherRegistry.anyRequest()
                                .authenticated())
                .sessionManagement(httpSecuritySessionManagementConfigurer ->
                        httpSecuritySessionManagementConfigurer.sessionCreationPolicy(STATELESS))
                .addFilterBefore(authFilter, AuthorizationFilter.class)
                .addFilterAfter(loggingFilter, AuthFilter.class);                ;

        return http.build();
    }





}


