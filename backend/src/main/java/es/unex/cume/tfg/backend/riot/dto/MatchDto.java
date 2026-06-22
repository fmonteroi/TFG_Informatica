package es.unex.cume.tfg.backend.riot.dto;

import java.util.List;

/**
 * DTO that maps Riot Match-V5 match responses.
 */
public record MatchDto(
        Metadata metadata,
        Info info
) {
    /**
     * DTO that maps Riot match metadata.
     */
    public record Metadata(String matchId) {}

    /**
     * DTO that maps Riot match information.
     */
    public record Info(
            Long gameStartTimestamp,
            Long gameDuration,
            Integer queueId,
            String gameVersion,
            List<Participant> participants
    ) {}

    /**
     * DTO that maps one Riot match participant.
     */
    public record Participant(
            String puuid,
            String riotIdGameName,
            String riotIdTagline,
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
            Integer item6,
            Integer roleBoundItem
    ) {}
}
