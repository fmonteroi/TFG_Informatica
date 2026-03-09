package es.unex.cume.tfg.backend.riot.client;

import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Component
public class RiotErrorHandler {

    public void handleError(HttpRequest request, ClientHttpResponse response) throws IOException {
        String body = new String(response.getBody().readAllBytes(), StandardCharsets.UTF_8);
        String message = "%s %s -> %s. Body: %s".formatted(
                request.getMethod(),
                request.getURI(),
                response.getStatusCode(),
                body
        );
        throw new RiotApiException(message, response.getStatusCode());
    }
}