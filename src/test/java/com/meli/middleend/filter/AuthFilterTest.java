package com.meli.middleend.filter;

import com.meli.middleend.filters.AuthFilter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.IOException;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import static org.springframework.test.util.AssertionErrors.assertNotNull;

public class AuthFilterTest {

    AuthFilter authFilter;
    private HashMap<String, UserDetails> tokenStore;
    private HttpServletRequest request;
    private HttpServletResponse response;
    private FilterChain chain;

    @BeforeEach
    public void setUp() {
        tokenStore = new HashMap<>();
        authFilter = new AuthFilter(tokenStore);
        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);
        chain = mock(FilterChain.class);
    }

    @Test
    public void noAuthTokenTest() throws ServletException, IOException {
        when(request.getHeader("x-auth-token")).thenReturn(null);
        when(request.getRequestURI()).thenReturn("uri/");
        authFilter.doFilter(request, response, chain);

        verify(response).sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized");
        verifyNoInteractions(chain);
    }

    @Test
    public void invalidTokenTest() throws ServletException, IOException {
        String invalidToken = "invalidToken";
        when(request.getHeader("x-auth-token")).thenReturn(invalidToken);
        when(request.getRequestURI()).thenReturn("uri/");

        authFilter.doFilter(request, response, chain);

        verify(response).sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized");
        verifyNoInteractions(chain);
    }

    @Test
    public void validTokenTest() throws ServletException, IOException {
        String validToken = "validToken";
        when(request.getHeader("x-auth-token")).thenReturn(validToken);
        when(request.getRequestURI()).thenReturn("uri/");

        UserDetails userDetails = User.withUsername("USERTEST").password("").roles("USERTEST").build();
        tokenStore.put(validToken, userDetails);

        authFilter.doFilter(request, response, chain);

        verify(chain).doFilter(request, response);
        assertEquals(userDetails, SecurityContextHolder.getContext().getAuthentication().getPrincipal());
    }



}
