package es.unex.cume.tfg.backend.exception;

/**
 * Signals that Riot returned a champion missing from the local catalog.
 */
public class ChampionCatalogException extends RuntimeException {
    /**
     * Creates an exception for a champion missing from the local catalog.
     *
     * @param championId the missing champion ID.
     */
    public ChampionCatalogException(Integer championId) {
        super("Champion catalog is missing champion: " + championId);
    }
}
