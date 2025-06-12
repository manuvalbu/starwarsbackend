package com.mercedes.benz.infrastructure.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
@EnableConfigurationProperties(SwapiProperties.class)
public class GeneralConfig {
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
