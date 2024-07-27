package com.meli.middleend.config;

import com.meli.middleend.dto.enums.UserEnum;
import com.meli.middleend.filters.AuthFilter;
import com.meli.middleend.filters.LoggingFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.intercept.AuthorizationFilter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.security.web.context.SecurityContextPersistenceFilter;
import org.springframework.security.web.session.ConcurrentSessionFilter;
import org.springframework.web.filter.CorsFilter;

import java.util.HashMap;

import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;


@Configuration
public class SecurityConfig {


    @Value("${spring.security.userclient.token}")
    private String userclienttoken;

    @Value("${spring.security.usermock.token}")
    private String usermocktoken;


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
                .addFilterAfter(new LoggingFilter(), AuthFilter.class);                ;

        return http.build();
    }





}


