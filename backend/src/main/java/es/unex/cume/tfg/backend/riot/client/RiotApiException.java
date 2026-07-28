package es.unex.cume.tfg.backend.riot.client;

import org.springframework.http.HttpStatusCode;

/**
 * Exception thrown when Riot returns a non-successful HTTP response.
 */
public class RiotApiException extends RuntimeException {

    private final HttpStatusCode status;

    /**
     * Creates an exception from a Riot error response.
     *
     * @param message error details
     * @param status Riot HTTP status
     */
    public RiotApiException(String message, HttpStatusCode status) {
        super(message);
        this.status = status;
    }

    /**
     * Returns the HTTP status returned by Riot.
     *
     * @return the Riot HTTP status.
     */
    public HttpStatusCode getStatus() {
        return status;
    }
}
