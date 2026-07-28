package es.unex.cume.tfg.backend.repository.projection;

import es.unex.cume.tfg.backend.model.Champion;

/**
 * Groups one player's results for a single champion.
 */
public record PlayerChampionStatsAggregate(
        Champion champion,
        Long gamesPlayed,
        Long wins
) {
}
