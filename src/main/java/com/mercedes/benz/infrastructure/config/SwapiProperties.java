package com.mercedes.benz.infrastructure.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "swapi")
public record SwapiProperties(
        String baseUrl,
        Endpoints endpoints
) {
    public record Endpoints(
            String people,
            String planets
    ) {
    }
}