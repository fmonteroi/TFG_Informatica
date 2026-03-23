package es.unex.cume.tfg.backend.dto;

public record ProfessionalsRefreshResultDto(
        int totalProfessionals,
        int processedProfessionals,
        int successfulProfessionals,
        boolean stoppedByRateLimit,
        String message
) {}


