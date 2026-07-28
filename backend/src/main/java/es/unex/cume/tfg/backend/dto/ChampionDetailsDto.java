package es.unex.cume.tfg.backend.dto;

import es.unex.cume.tfg.backend.model.Tier;

import java.util.List;

/**
 * DTO that groups champion data, statistics and build information.
 */
public record ChampionDetailsDto(
        Integer championId,
        String championName,
        Tier tier,
        ChampionStatsDto stats,
        List<ChampionRoleBuildsDto> roleBuilds
) {
}
