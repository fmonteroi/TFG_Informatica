package es.unex.cume.tfg.backend.exception;

/**
 * Exception thrown when a requested champion cannot be found in the local catalog.
 */
public class ChampionNotFoundException extends RuntimeException {
    /**
     * Creates an exception for a missing champion.
     *
     * @param championId the missing champion ID.
     */
    public ChampionNotFoundException(Integer championId) {
        super("Champion not found: " + championId);
    }
}
