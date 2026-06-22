package es.unex.cume.tfg.backend.riot.dto;

/**
 * DTO that maps Riot Summoner-V4 profile responses.
 */
public record SummonerDto(
        Integer profileIconId,
        Long summonerLevel
) {
}
