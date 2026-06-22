package es.unex.cume.tfg.backend.dto;

/**
 * DTO that summarizes a professional refresh execution.
 */
public record ProfessionalsRefreshResultDto(
        int totalProfessionals,
        int checkedProfessionals,
        boolean stoppedByRateLimit,
        String message
) {}


