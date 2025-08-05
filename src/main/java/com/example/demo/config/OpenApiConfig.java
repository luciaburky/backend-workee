package com.example.demo.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;

import org.springframework.context.annotation.Configuration;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;

@Configuration
@OpenAPIDefinition(
    info = @Info(title = "Workee API", version = "1.0", description = "Documentación de Workee"),
    security = @SecurityRequirement(name = "bearerAuth") 
)
@SecurityScheme(
    name = "bearerAuth",
    type = SecuritySchemeType.HTTP,
    scheme = "bearer",
    bearerFormat = "JWT" 
)
public class OpenApiConfig {

}
