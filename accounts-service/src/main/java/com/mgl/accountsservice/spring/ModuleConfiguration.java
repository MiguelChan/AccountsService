package com.mgl.accountsservice.spring;

import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configures the Jackson Modules.
 */
@Configuration
public class ModuleConfiguration {

    /**
     * Configures the {@link JavaTimeModule} to be used with Jackson.
     *
     *  @return .
     */
    @Bean
    public Module javaTimeModule() {
        JavaTimeModule module = new JavaTimeModule();
        return module;
    }

}
