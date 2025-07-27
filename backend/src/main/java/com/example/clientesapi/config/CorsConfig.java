package com.example.clientesapi.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@Configuration
public class CorsConfig {

    @Value("${app.cors.allowed-origins:http://localhost:4200,http://127.0.0.1:4200,http://localhost:8080}")
    private String[] allowedOrigins;

    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        
        // Configuración para API REST (más restrictiva)
        CorsConfiguration apiCorsConfiguration = new CorsConfiguration();
        apiCorsConfiguration.setAllowCredentials(true);
        
        // Orígenes permitidos desde properties
        for (String origin : allowedOrigins) {
            apiCorsConfiguration.addAllowedOrigin(origin.trim());
        }
        
        apiCorsConfiguration.addAllowedHeader("*");
        apiCorsConfiguration.addAllowedMethod("GET");
        apiCorsConfiguration.addAllowedMethod("POST");
        apiCorsConfiguration.addAllowedMethod("PUT");
        apiCorsConfiguration.addAllowedMethod("DELETE");
        apiCorsConfiguration.addAllowedMethod("OPTIONS");
        
        // Aplicar a endpoints de API
        source.registerCorsConfiguration("/api/**", apiCorsConfiguration);
        
        // Configuración para Swagger UI (más permisiva para desarrollo)
        CorsConfiguration swaggerCorsConfiguration = new CorsConfiguration();
        swaggerCorsConfiguration.setAllowCredentials(false); // Swagger no necesita credenciales
        swaggerCorsConfiguration.addAllowedOriginPattern("*"); // Permite acceso desde cualquier origen para Swagger
        swaggerCorsConfiguration.addAllowedHeader("*");
        swaggerCorsConfiguration.addAllowedMethod("*");
        
        // Aplicar a endpoints de Swagger/OpenAPI
        source.registerCorsConfiguration("/swagger-ui/**", swaggerCorsConfiguration);
        source.registerCorsConfiguration("/swagger-ui.html", swaggerCorsConfiguration);
        source.registerCorsConfiguration("/api-docs/**", swaggerCorsConfiguration);
        source.registerCorsConfiguration("/v3/api-docs/**", swaggerCorsConfiguration);
        
        return new CorsFilter(source);
    }
}