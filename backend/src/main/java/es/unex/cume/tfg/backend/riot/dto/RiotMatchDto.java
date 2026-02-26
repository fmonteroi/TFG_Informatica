package es.unex.cume.tfg.backend.riot.dto;

import java.util.List;

public record RiotMatchDto(
        Metadata metadata,
        Info info
) {
    public record Metadata(String matchId) {}

    public record Info(
            Long gameStartTimestamp,
            Long gameDuration,
            Integer queueId,
            String gameVersion,
            List<Participant> participants
    ) {}

    public record Participant(
            String puuid,
            Integer championId,
            Boolean win,
            Integer kills,
            Integer deaths,
            Integer assists,
            Integer teamId,
            String teamPosition,
            Integer summoner1Id,
            Integer summoner2Id,
            Integer item0,
            Integer item1,
            Integer item2,
            Integer item3,
            Integer item4,
            Integer item5,
            Integer item6
    ) {}
}
