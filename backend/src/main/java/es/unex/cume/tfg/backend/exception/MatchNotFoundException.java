package es.unex.cume.tfg.backend.exception;

/**
 * Exception thrown when a match cannot be found in the local database.
 */
public class MatchNotFoundException extends RuntimeException {
    /**
     * Creates an exception for a missing match.
     *
     * @param matchId the missing Riot match ID.
     */
    public MatchNotFoundException(String matchId) {
        super("Match not found: " + matchId);
    }
}
