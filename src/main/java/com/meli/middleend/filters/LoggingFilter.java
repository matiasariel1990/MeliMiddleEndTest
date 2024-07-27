package com.meli.middleend.filters;

import jakarta.servlet.*;

import java.io.IOException;


public class LoggingFilter implements Filter {


    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        chain.doFilter(request, response);
    }
}
