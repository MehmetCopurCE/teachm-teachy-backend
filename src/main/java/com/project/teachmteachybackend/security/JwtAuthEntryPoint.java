package com.project.teachmteachybackend.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.teachmteachybackend.exceptions.ErrorResponse;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Date;

@Component
public class JwtAuthEntryPoint implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException {
        //response.sendError(HttpServletResponse.SC_UNAUTHORIZED, authException.getMessage());

        // ErrorResponse nesnesini oluşturun
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setTimestamp(new Date());
        errorResponse.setStatus(String.valueOf(HttpServletResponse.SC_UNAUTHORIZED));
        errorResponse.setError("Unauthorized");
        errorResponse.setMessage("Full authentication is required to access this source");

        // ErrorResponse nesnesini JSON'a dönüştürün
        String jsonResponse = new ObjectMapper().writeValueAsString(errorResponse);

        // Yanıt gövdesini ve durum kodunu ayarlayın
        response.setContentType("application/json");
        response.getWriter().write(jsonResponse);
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
    }
}
