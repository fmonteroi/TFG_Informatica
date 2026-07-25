package es.unex.cume.tfg.backend.repository.projection;

import es.unex.cume.tfg.backend.model.Champion;

public record ChampionStatsAggregate(
        Champion champion,
        Long gamesPlayed,
        Long wins,
        Long kills,
        Long deaths,
        Long assists
) {
}
