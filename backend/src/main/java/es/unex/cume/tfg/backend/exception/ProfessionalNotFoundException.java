package es.unex.cume.tfg.backend.exception;

/**
 * Exception thrown when a professional player cannot be found.
 */
public class ProfessionalNotFoundException extends RuntimeException {

    /**
     * Creates an exception for a missing professional player.
     *
     * @param puuid missing professional player PUUID
     */
    public ProfessionalNotFoundException(String puuid) {
        super("Professional not found: " + puuid);
    }
}
