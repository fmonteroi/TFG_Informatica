package es.unex.cume.tfg.backend.riot.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.time.Duration;

/**
 * Configuration properties used by Riot API clients.
 */
@ConfigurationProperties(prefix = "riot.api")
public record ApiProperties(
        String key,
        Duration connectTimeout,
        Duration readTimeout
) {}


