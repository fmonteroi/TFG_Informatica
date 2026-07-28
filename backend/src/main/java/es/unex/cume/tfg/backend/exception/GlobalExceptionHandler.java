package es.unex.cume.tfg.backend.exception;

import es.unex.cume.tfg.backend.dto.ApiErrorDto;
import es.unex.cume.tfg.backend.riot.client.RiotApiException;
import jakarta.validation.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

/**
 * Converts application exceptions into standardized API error responses.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Handles missing players.
     *
     * @param ex thrown exception
     * @return not found response
     */
    @ExceptionHandler(PlayerNotFoundException.class)
    public ResponseEntity<ApiErrorDto> handlePlayerNotFound(PlayerNotFoundException ex) {
        return buildResponse(
                HttpStatus.NOT_FOUND,
                ErrorCode.PLAYER_NOT_FOUND,
                "Not Found",
                "El jugador buscado no existe."
        );
    }

    /**
     * Handles missing matches.
     *
     * @param ex thrown exception
     * @return not found response
     */
    @ExceptionHandler(MatchNotFoundException.class)
    public ResponseEntity<ApiErrorDto> handleMatchNotFound(MatchNotFoundException ex) {
        return buildResponse(
                HttpStatus.NOT_FOUND,
                ErrorCode.MATCH_NOT_FOUND,
                "Not Found",
                "La partida solicitada no existe."
        );
    }

    /**
     * Handles missing champions.
     *
     * @param ex thrown exception
     * @return not found response
     */
    @ExceptionHandler(ChampionNotFoundException.class)
    public ResponseEntity<ApiErrorDto> handleChampionNotFound(ChampionNotFoundException ex) {
        return buildResponse(
                HttpStatus.NOT_FOUND,
                ErrorCode.CHAMPION_NOT_FOUND,
                "Not Found",
                "El campeón solicitado no existe."
        );
    }

    /**
     * Handles outdated champion catalog errors.
     *
     * @param ex thrown exception
     * @return internal server error response
     */
    @ExceptionHandler(ChampionCatalogException.class)
    public ResponseEntity<ApiErrorDto> handleChampionCatalog(ChampionCatalogException ex) {
        return buildResponse(
                HttpStatus.INTERNAL_SERVER_ERROR,
                ErrorCode.CHAMPION_CATALOG_OUTDATED,
                "Internal Server Error",
                "El catálogo local de campeones está desactualizado."
        );
    }

    /**
     * Handles missing professional players.
     *
     * @param ex thrown exception
     * @return not found response
     */
    @ExceptionHandler(ProfessionalNotFoundException.class)
    public ResponseEntity<ApiErrorDto> handleProfessionalNotFound(ProfessionalNotFoundException ex) {
        return buildResponse(
                HttpStatus.NOT_FOUND,
                ErrorCode.PROFESSIONAL_NOT_FOUND,
                "Not Found",
                "El profesional solicitado no existe."
        );
    }

    /**
     * Converts Riot API errors into stable API responses.
     *
     * @param ex Riot API exception
     * @return response matching the Riot status
     */
    @ExceptionHandler(RiotApiException.class)
    public ResponseEntity<ApiErrorDto> handleRiotApiException(RiotApiException ex) {
        // Maps Riot status codes to stable application errors
        int status = ex.getStatus().value();

        if (status == 400) {
            return buildResponse(
                    HttpStatus.BAD_REQUEST,
                    ErrorCode.RIOT_BAD_REQUEST,
                    "Bad Request",
                    "La petición enviada a Riot no es válida."
            );
        }

        if (status == 401) {
            return buildResponse(
                    HttpStatus.UNAUTHORIZED,
                    ErrorCode.RIOT_UNAUTHORIZED,
                    "Unauthorized",
                    "La API key de Riot no es válida o ha caducado."
            );
        }

        if (status == 403) {
            return buildResponse(
                    HttpStatus.FORBIDDEN,
                    ErrorCode.RIOT_FORBIDDEN,
                    "Forbidden",
                    "No hay permisos para acceder al recurso solicitado en Riot."
            );
        }

        if (status == 404) {
            return buildResponse(
                    HttpStatus.NOT_FOUND,
                    ErrorCode.RIOT_RESOURCE_NOT_FOUND,
                    "Not Found",
                    "El jugador buscado no existe."
            );
        }

        if (status == 405) {
            return buildResponse(
                    HttpStatus.METHOD_NOT_ALLOWED,
                    ErrorCode.RIOT_METHOD_NOT_ALLOWED,
                    "Method Not Allowed",
                    "Riot no permite el método usado para esta petición."
            );
        }

        if (status == 415) {
            return buildResponse(
                    HttpStatus.UNSUPPORTED_MEDIA_TYPE,
                    ErrorCode.RIOT_UNSUPPORTED_MEDIA_TYPE,
                    "Unsupported Media Type",
                    "Riot no acepta el formato de la petición enviada."
            );
        }

        if (status == 429) {
            return buildResponse(
                    HttpStatus.TOO_MANY_REQUESTS,
                    ErrorCode.RIOT_RATE_LIMIT,
                    "Too Many Requests",
                    "Se ha alcanzado el límite de Riot. Inténtalo de nuevo en unos minutos."
            );
        }

        if (status == 500) {
            return buildResponse(
                    HttpStatus.BAD_GATEWAY,
                    ErrorCode.RIOT_INTERNAL_SERVER_ERROR,
                    "Bad Gateway",
                    "Riot ha devuelto un error interno. Inténtalo de nuevo más tarde."
            );
        }

        if (status == 502) {
            return buildResponse(
                    HttpStatus.BAD_GATEWAY,
                    ErrorCode.RIOT_BAD_GATEWAY,
                    "Bad Gateway",
                    "Riot no ha respondido correctamente. Inténtalo de nuevo más tarde."
            );
        }

        if (status == 503) {
            return buildResponse(
                    HttpStatus.SERVICE_UNAVAILABLE,
                    ErrorCode.RIOT_SERVICE_UNAVAILABLE,
                    "Service Unavailable",
                    "Riot no está disponible temporalmente. Inténtalo de nuevo más tarde."
            );
        }

        if (status == 504) {
            return buildResponse(
                    HttpStatus.GATEWAY_TIMEOUT,
                    ErrorCode.RIOT_GATEWAY_TIMEOUT,
                    "Gateway Timeout",
                    "Riot ha tardado demasiado en responder. Inténtalo de nuevo más tarde."
            );
        }

        return buildResponse(
                ex.getStatus(),
                ErrorCode.RIOT_API_ERROR,
                "Riot API Error",
                "No se pudo completar la petición a Riot."
        );
    }

    /**
     * Handles missing request parameters.
     *
     * @param ex thrown exception
     * @return bad request response
     */
    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<ApiErrorDto> handleMissingParameter(MissingServletRequestParameterException ex) {
        return buildResponse(
                HttpStatus.BAD_REQUEST,
                ErrorCode.MISSING_PARAMETER,
                "Bad Request",
                "Falta el parámetro obligatorio: " + ex.getParameterName()
        );
    }

    /**
     * Handles request parameters with an invalid type.
     *
     * @param ex thrown exception
     * @return bad request response
     */
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ApiErrorDto> handleTypeMismatch(MethodArgumentTypeMismatchException ex) {
        return buildResponse(
                HttpStatus.BAD_REQUEST,
                ErrorCode.INVALID_PARAMETER,
                "Bad Request",
                "Parámetro inválido: " + ex.getName()
        );
    }

    /**
     * Handles invalid request body values.
     *
     * @param ex thrown exception
     * @return bad request response
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiErrorDto> handleMethodArgumentNotValid(MethodArgumentNotValidException ex) {
        return buildResponse(
                HttpStatus.BAD_REQUEST,
                ErrorCode.VALIDATION_ERROR,
                "Bad Request",
                "La petición contiene datos inválidos."
        );
    }

    /**
     * Handles invalid method parameter values.
     *
     * @param ex thrown exception
     * @return bad request response
     */
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ApiErrorDto> handleConstraintViolation(ConstraintViolationException ex) {
        return buildResponse(
                HttpStatus.BAD_REQUEST,
                ErrorCode.VALIDATION_ERROR,
                "Bad Request",
                "La petición contiene datos inválidos."
        );
    }

    /**
     * Handles database integrity conflicts.
     *
     * @param ex thrown exception
     * @return conflict response
     */
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ApiErrorDto> handleDataIntegrityViolation(DataIntegrityViolationException ex) {
        return buildResponse(
                HttpStatus.CONFLICT,
                ErrorCode.DATA_INTEGRITY_VIOLATION,
                "Conflict",
                "No se pudo completar la operación por un conflicto de datos."
        );
    }

    /**
     * Handles unexpected application errors.
     *
     * @param ex thrown exception
     * @return internal server error response
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiErrorDto> handleGenericException(Exception ex) {
        return buildResponse(
                HttpStatus.INTERNAL_SERVER_ERROR,
                ErrorCode.INTERNAL_SERVER_ERROR,
                "Internal Server Error",
                "Ha ocurrido un error inesperado."
        );
    }

    /**
     * Builds the shared API error format.
     *
     * @param status HTTP response status
     * @param code stable application error code
     * @param error short error name
     * @param message readable error message
     * @return formatted API error response
     */
    private ResponseEntity<ApiErrorDto> buildResponse(HttpStatusCode status, String code, String error, String message) {
        // Creates the common response body and applies its HTTP status
        return ResponseEntity
                .status(status)
                .body(ApiErrorDto.from(status.value(), code, error, message));
    }
}
