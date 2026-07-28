package es.unex.cume.tfg.backend.dto;

import java.time.Instant;

/**
 * Standard error response returned by the API.
 */
public record ApiErrorDto(
        String timestamp,
        int status,
        String code,
        String error,
        String message
) {
    /**
     * Creates an error response using the current timestamp.
     *
     * @param status HTTP status code
     * @param code internal error code
     * @param error short error name
     * @param message readable error message
     * @return API error response
     */
    public static ApiErrorDto from(int status, String code, String error, String message) {
        return new ApiErrorDto(Instant.now().toString(), status, code, error, message);
    }
}
