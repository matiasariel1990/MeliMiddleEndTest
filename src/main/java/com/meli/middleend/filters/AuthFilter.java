package com.meli.middleend.filters;


import com.meli.middleend.dto.enums.UserEnum;
import com.meli.middleend.exception.AuthException;
import com.sun.net.httpserver.HttpExchange;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashMap;

public class AuthFilter implements Filter {

    private static final String AUTHTOKEN = "x-auth-token";

    HashMap<String, UserDetails> tokenStore;

    public AuthFilter(HashMap<String, UserDetails> tokenStore){
        this.tokenStore = tokenStore;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        String authToken = httpRequest.getHeader(AUTHTOKEN);

        if(authToken != null && tokenStore.containsKey(authToken)){

            UserDetails userDetails = tokenStore.get(authToken);

            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                    userDetails, null, userDetails.getAuthorities());

            authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails((HttpServletRequest) request));

            SecurityContextHolder.getContext().setAuthentication(authentication);


        }else{
            HttpServletResponse httpResponse = (HttpServletResponse) response;
            httpResponse.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized");
            return;
        }

        chain.doFilter(request, response);
    }

}
