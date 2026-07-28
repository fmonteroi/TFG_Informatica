package es.unex.cume.tfg.backend.riot.client;

import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * Converts Riot HTTP error responses into RiotApiException instances.
 */
@Component
public class RiotErrorHandler {

    /**
     * Reads the Riot error response and raises a domain-specific exception.
     *
     * @param request the outgoing request.
     * @param response the Riot error response.
     * @throws IOException if the response body cannot be read.
     */
    public void handleError(HttpRequest request, ClientHttpResponse response) throws IOException {
        // Reads the response body before its stream is closed
        String body = new String(response.getBody().readAllBytes(), StandardCharsets.UTF_8);

        // Adds request and response details to the exception message
        String message = "%s %s -> %s. Body: %s".formatted(
                request.getMethod(),
                request.getURI(),
                response.getStatusCode(),
                body
        );
        throw new RiotApiException(message, response.getStatusCode());
    }
}
