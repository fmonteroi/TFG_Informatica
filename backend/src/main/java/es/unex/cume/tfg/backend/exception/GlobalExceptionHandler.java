package es.unex.cume.tfg.backend.exception;

import es.unex.cume.tfg.backend.riot.client.RiotApiException;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(PlayerNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handlePlayerNotFound(PlayerNotFoundException ex) {
        return buildResponse(HttpStatus.NOT_FOUND, "Player not found", ex.getMessage());
    }

    @ExceptionHandler(MatchNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleMatchNotFound(MatchNotFoundException ex) {
        return buildResponse(HttpStatus.NOT_FOUND, "Match not found", ex.getMessage());
    }

    @ExceptionHandler(ChampionNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleChampionNotFound(ChampionNotFoundException ex) {
        return buildResponse(HttpStatus.NOT_FOUND, "Champion not found", ex.getMessage());
    }

    @ExceptionHandler(RiotApiException.class)
    public ResponseEntity<Map<String, Object>> handleRiotApiException(RiotApiException ex) {
        return buildResponse(ex.getStatus(), "Riot API error", ex.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleGenericException(Exception ex) {
        return buildResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Internal server error", "An unexpected error occurred");
    }

    private ResponseEntity<Map<String, Object>> buildResponse(HttpStatusCode status, String error, String message) {
        return ResponseEntity.status(status).body(
                Map.of(
                        "timestamp", Instant.now().toString(),
                        "status", status.value(),
                        "error", error,
                        "message", message
                )
        );
    }
}