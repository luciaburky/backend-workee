package com.example.demo.security;

import java.io.IOException;
import java.io.PrintWriter;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request,
                         HttpServletResponse response,
                         AuthenticationException authException) throws IOException {
        //response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "No está autenticado");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED); // 401
        response.setContentType("application/json");

        PrintWriter writer = response.getWriter();
        writer.write("{\"error\": \"El usuario no esta autenticado. Debe loguearse para acceder a la funcionalidad.\"}");
        writer.flush();
    }
}
