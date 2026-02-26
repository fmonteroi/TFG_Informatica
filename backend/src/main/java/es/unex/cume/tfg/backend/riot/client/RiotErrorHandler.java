package es.unex.cume.tfg.backend.riot.client;

import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.http.HttpRequest;


import java.io.IOException;

@Component
public class RiotErrorHandler {

    public void handleError(HttpRequest request, ClientHttpResponse response) throws IOException {
        String body = new String(response.getBody().readAllBytes());
        throw new RiotApiException(body, response.getStatusCode());
    }
}
