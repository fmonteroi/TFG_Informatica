package es.unex.cume.tfg.backend.dto;

public record ProfessionalsRefreshResultDto(
        int totalProfessionals,
        int checkedProfessionals,
        boolean stoppedByRateLimit,
        String message
) {}


