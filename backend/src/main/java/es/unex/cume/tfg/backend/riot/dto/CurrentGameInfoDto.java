package es.unex.cume.tfg.backend.riot.dto;

import java.util.List;

public record CurrentGameInfoDto(
        Long gameLength,
        Long gameQueueConfigId,
        List<CurrentGameParticipant> participants
) {
    public record CurrentGameParticipant(
            String puuid,
            Integer championId
    ) {}
}
