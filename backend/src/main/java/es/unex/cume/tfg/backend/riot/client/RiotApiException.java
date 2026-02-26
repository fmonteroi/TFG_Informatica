package es.unex.cume.tfg.backend.riot.client;

import org.springframework.http.HttpStatusCode;

public class RiotApiException extends RuntimeException {

    private final HttpStatusCode status;

    public RiotApiException(String message, HttpStatusCode status) {
        super(message);
        this.status = status;
    }

    public HttpStatusCode getStatus() {
        return status;
    }
}
