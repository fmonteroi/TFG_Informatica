package es.unex.cume.tfg.backend.dto;

import java.util.List;

public record ChampionDetailsDto(
        Integer championId,
        String championName,
        ChampionStatsDto stats,
        RecommendedBuildDto recommendedBuild,
        List<ChampionProBuildDto> recentProBuilds
) {
}