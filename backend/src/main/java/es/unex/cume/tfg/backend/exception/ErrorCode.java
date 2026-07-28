package es.unex.cume.tfg.backend.exception;

/**
 * Stable error codes returned by the API.
 */
public final class ErrorCode {

    /** Prevents utility class instantiation. */
    private ErrorCode() {
    }

    // 400 errors
    public static final String MISSING_PARAMETER = "MISSING_PARAMETER";
    public static final String INVALID_PARAMETER = "INVALID_PARAMETER";
    public static final String VALIDATION_ERROR = "VALIDATION_ERROR";

    // 404 errors
    public static final String PLAYER_NOT_FOUND = "PLAYER_NOT_FOUND";
    public static final String MATCH_NOT_FOUND = "MATCH_NOT_FOUND";
    public static final String CHAMPION_NOT_FOUND = "CHAMPION_NOT_FOUND";
    public static final String PROFESSIONAL_NOT_FOUND = "PROFESSIONAL_NOT_FOUND";

    // 409 errors
    public static final String DATA_INTEGRITY_VIOLATION = "DATA_INTEGRITY_VIOLATION";

    // Riot API errors
    public static final String RIOT_BAD_REQUEST = "RIOT_BAD_REQUEST";
    public static final String RIOT_UNAUTHORIZED = "RIOT_UNAUTHORIZED";
    public static final String RIOT_FORBIDDEN = "RIOT_FORBIDDEN";
    public static final String RIOT_RESOURCE_NOT_FOUND = "RIOT_RESOURCE_NOT_FOUND";
    public static final String RIOT_METHOD_NOT_ALLOWED = "RIOT_METHOD_NOT_ALLOWED";
    public static final String RIOT_UNSUPPORTED_MEDIA_TYPE = "RIOT_UNSUPPORTED_MEDIA_TYPE";
    public static final String RIOT_RATE_LIMIT = "RIOT_RATE_LIMIT";
    public static final String RIOT_INTERNAL_SERVER_ERROR = "RIOT_INTERNAL_SERVER_ERROR";
    public static final String RIOT_BAD_GATEWAY = "RIOT_BAD_GATEWAY";
    public static final String RIOT_SERVICE_UNAVAILABLE = "RIOT_SERVICE_UNAVAILABLE";
    public static final String RIOT_GATEWAY_TIMEOUT = "RIOT_GATEWAY_TIMEOUT";
    public static final String RIOT_API_ERROR = "RIOT_API_ERROR";

    // 500 errors
    public static final String CHAMPION_CATALOG_OUTDATED = "CHAMPION_CATALOG_OUTDATED";
    public static final String INTERNAL_SERVER_ERROR = "INTERNAL_SERVER_ERROR";
}
