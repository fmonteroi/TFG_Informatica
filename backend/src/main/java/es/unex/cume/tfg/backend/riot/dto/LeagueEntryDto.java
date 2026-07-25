package es.unex.cume.tfg.backend.riot.dto;

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
