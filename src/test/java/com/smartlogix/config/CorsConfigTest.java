package com.smartlogix.config;

import org.junit.jupiter.api.Test;
import org.springframework.web.servlet.config.annotation.CorsRegistration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

class CorsConfigTest {

    @Test
    void addCorsMappings() {
        CorsConfig corsConfig = new CorsConfig();
        CorsRegistry registry = mock(CorsRegistry.class);
        CorsRegistration registration = mock(CorsRegistration.class);

        when(registry.addMapping(anyString())).thenReturn(registration);
        when(registration.allowedOrigins(anyString())).thenReturn(registration);
        when(registration.allowedMethods(anyString(), anyString(), anyString(), anyString())).thenReturn(registration);
        when(registration.allowedHeaders(anyString())).thenReturn(registration);

        corsConfig.addCorsMappings(registry);

        verify(registry).addMapping("/api/**");
        verify(registration).allowedOrigins("http://localhost:5173");
        verify(registration).allowedMethods("GET", "POST", "PUT", "DELETE");
        verify(registration).allowedHeaders("*");
    }
}
