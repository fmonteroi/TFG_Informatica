package es.unex.cume.tfg.backend.dto;

import es.unex.cume.tfg.backend.model.Platform;
import es.unex.cume.tfg.backend.model.Role;

import java.util.List;

/**
 * DTO that groups professional player data and recent builds.
 */
public record ProfessionalDetailsDto(
        String puuid,
        String proName,
        String teamName,
        String league,
        Role role,
        String gameName,
        String tagLine,
        Platform platform,
        Integer profileIconId,
        List<ProBuildDto> recentBuilds
) {
}
