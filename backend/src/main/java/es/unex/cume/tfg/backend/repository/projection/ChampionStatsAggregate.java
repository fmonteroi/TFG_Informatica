package es.unex.cume.tfg.backend.repository.projection;

import es.unex.cume.tfg.backend.model.Champion;

/**
 * Groups the stored values needed to calculate champion statistics.
 */
public record ChampionStatsAggregate(
        Champion champion,
        Long gamesPlayed,
        Long wins,
        Long kills,
        Long deaths,
        Long assists
) {
}
