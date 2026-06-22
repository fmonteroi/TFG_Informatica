package es.unex.cume.tfg.backend.riot.dto;

import java.util.List;

/**
 * DTO that maps Riot Spectator-V5 current game responses.
 */
public record CurrentGameInfoDto(
        Long gameLength,
        Long gameQueueConfigId,
        List<CurrentGameParticipant> participants
) {
    /**
     * DTO that maps a participant in a current game response.
     */
    public record CurrentGameParticipant(
            String puuid,
            Integer championId
    ) {}
}
