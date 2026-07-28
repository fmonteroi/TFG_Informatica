package es.unex.cume.tfg.backend.repository.projection;

/**
 * Groups the stored values needed to calculate player statistics.
 */
public record PlayerStatsAggregate(
        Long gamesPlayed,
        Long wins,
        Long kills,
        Long deaths,
        Long assists
) {
}
