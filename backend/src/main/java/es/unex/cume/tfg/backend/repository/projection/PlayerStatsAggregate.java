package es.unex.cume.tfg.backend.repository.projection;

public record PlayerStatsAggregate(
        Long gamesPlayed,
        Long wins,
        Long kills,
        Long deaths,
        Long assists
) {
}
