package es.unex.cume.tfg.backend.exception;

public class PlayerNotFoundException extends RuntimeException {
    public PlayerNotFoundException(String puuid) {
        super("Player not found: " + puuid);
    }
}
