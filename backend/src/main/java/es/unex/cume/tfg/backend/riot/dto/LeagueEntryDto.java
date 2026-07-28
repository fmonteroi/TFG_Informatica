package es.unex.cume.tfg.backend.riot.dto;

/**
 * DTO that maps Riot League-V4 ranked result responses.
 */
public record LeagueEntryDto(
        String queueType,
        // rank
        String tier,
        // division
        String rank,
        Integer leaguePoints,
        Integer wins,
        Integer losses
) {
}
