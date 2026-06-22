package es.unex.cume.tfg.backend.exception;

/**
 * Exception thrown when a player cannot be found in the local database.
 */
public class PlayerNotFoundException extends RuntimeException {
    /**
     * Creates an exception for a missing player.
     *
     * @param puuid the missing player PUUID.
     */
    public PlayerNotFoundException(String puuid) {
        super("Player not found: " + puuid);
    }
}
