package es.unex.cume.tfg.backend.riot.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.time.Duration;

@ConfigurationProperties(prefix = "riot.api")
public record RiotApiProperties(
        String key,
        Duration connectTimeout,
        Duration readTimeout
) {}


