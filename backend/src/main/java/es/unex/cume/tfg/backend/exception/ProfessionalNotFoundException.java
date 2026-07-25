package es.unex.cume.tfg.backend.exception;

public class ProfessionalNotFoundException extends RuntimeException {

    public ProfessionalNotFoundException(String puuid) {
        super("Professional not found: " + puuid);
    }
}