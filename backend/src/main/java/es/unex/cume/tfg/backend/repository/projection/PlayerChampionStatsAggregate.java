package es.unex.cume.tfg.backend.repository.projection;

import es.unex.cume.tfg.backend.model.Champion;

public record PlayerChampionStatsAggregate(
        Champion champion,
        Long gamesPlayed,
        Long wins
) {
}
