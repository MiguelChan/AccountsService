package com.mgl.accountsservice.spring;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

/** Preserves the service's existing anonymous API and proxy HTTPS policy. */
@EnableWebSecurity
@Configuration
public class WebSecurityConfig {

    /** Builds the service filter chain without changing the authorization policy. */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.redirectToHttps(https -> https
            .requestMatchers(request -> !request.isSecure() && request.getHeader("X-Forwarded-Proto") != null));
        http.cors(Customizer.withDefaults())
            .csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(authorize -> authorize.anyRequest().permitAll());
        return http.build();
    }
}
