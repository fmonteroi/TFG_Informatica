package es.unex.cume.tfg.backend.riot.dto;

/**
 * DTO that maps Riot Account-V1 account responses.
 */
public record AccountDto(
        String puuid,
        String gameName,
        String tagLine
) {
}
