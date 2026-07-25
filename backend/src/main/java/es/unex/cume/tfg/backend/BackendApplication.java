package es.unex.cume.tfg.backend;

import es.unex.cume.tfg.backend.riot.config.ApiProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * Main entry point for the Spring Boot backend application.
 */
@EnableScheduling
@EnableConfigurationProperties(ApiProperties.class)
@SpringBootApplication
public class BackendApplication {

    /**
     * Starts the backend application.
     *
     * @param args command-line arguments.
     */
    public static void main(String[] args) {
        SpringApplication.run(BackendApplication.class, args);
    }

}
