package es.unex.cume.tfg.backend.dto;

import es.unex.cume.tfg.backend.model.Professional;
import es.unex.cume.tfg.backend.model.Role;

/**
 * DTO that exposes professional roster information.
 */
public record ProfessionalDto(
        String puuid,
        String proName,
        String teamName,
        String league,
        Role role
) {
    /**
     * Creates a DTO from a professional entity.
     *
     * @param professional professional player to convert
     * @return professional roster DTO
     */
    public static ProfessionalDto fromEntity(Professional professional) {
        return new ProfessionalDto(
                professional.getPuuid(),
                professional.getProName(),
                professional.getTeamName(),
                professional.getLeague(),
                professional.getRole()
        );
    }
}
