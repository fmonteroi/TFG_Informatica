package es.unex.cume.tfg.backend.dto;

import es.unex.cume.tfg.backend.model.Professional;

public record ProfessionalDto(
        String puuid,
        String proName,
        String teamName,
        String league
) {
    public static ProfessionalDto fromEntity(Professional professional) {
        return new ProfessionalDto(
                professional.getPuuid(),
                professional.getProName(),
                professional.getTeamName(),
                professional.getLeague()
        );
    }
}
