package es.unex.cume.tfg.backend.exception;

public class ChampionNotFoundException extends RuntimeException {
    public ChampionNotFoundException(Integer championId) {
        super("Champion not found: " + championId);
    }
}
