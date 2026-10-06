package com.mgl.accountsservice.spring;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsUtils;
import org.springframework.web.servlet.handler.HandlerMappingIntrospector;

/** Preserves the service's existing anonymous API and proxy HTTPS policy. */
@EnableWebSecurity
@Configuration
public class WebSecurityConfig {

    /** Builds the service filter chain without changing the authorization policy. */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, HandlerMappingIntrospector introspector) throws Exception {
        http.redirectToHttps(https -> https
            .requestMatchers(request -> !request.isSecure() && request.getHeader("X-Forwarded-Proto") != null));
        // Framework 7 skips null CORS configurations; keep rejecting unconfigured preflights.
        http.cors(cors -> cors.configurationSource(request -> {
            CorsConfiguration configuration = introspector.getCorsConfiguration(request);
            if (configuration == null && CorsUtils.isPreFlightRequest(request)) {
                return new CorsConfiguration();
            }
            return configuration;
        }))
            .csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(authorize -> authorize.anyRequest().permitAll());
        return http.build();
    }
}
