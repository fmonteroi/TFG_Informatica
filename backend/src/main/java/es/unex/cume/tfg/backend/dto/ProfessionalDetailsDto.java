package es.unex.cume.tfg.backend.dto;

import es.unex.cume.tfg.backend.model.Platform;

import java.util.List;

public record ProfessionalDetailsDto(
        String puuid,
        String proName,
        String teamName,
        String league,
        String gameName,
        String tagLine,
        Platform platform,
        Integer profileIconId,
        List<ProBuildDto> recentBuilds
) {
}
