package es.unex.cume.tfg.backend.exception;

public class ParticipantNotFoundException extends RuntimeException {
    public ParticipantNotFoundException(String puuid) {
        super("Player not found in current game participants: " + puuid);
    }
}
