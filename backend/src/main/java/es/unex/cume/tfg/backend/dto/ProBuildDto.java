package es.unex.cume.tfg.backend.dto;

import es.unex.cume.tfg.backend.model.Build;
import es.unex.cume.tfg.backend.model.Champion;
import es.unex.cume.tfg.backend.model.Match;
import es.unex.cume.tfg.backend.model.Participation;

import java.time.Instant;

public record ProBuildDto(
        String matchId,
        Instant gameStartAt,
        String gameVersion,
        Integer queueId,
        Integer championId,
        String championName,
        String teamPosition,
        BuildDto build
) {
    public static ProBuildDto fromEntity(Build build) {
        Participation participation = build.getParticipation();
        Match match = participation.getMatch();
        Champion champion = participation.getChampion();

        return new ProBuildDto(
                match.getMatchId(),
                match.getGameStartAt(),
                match.getGameVersion(),
                match.getQueueId(),
                champion.getChampionId(),
                champion.getChampionName(),
                participation.getTeamPosition(),
                BuildDto.fromEntity(build)
        );
    }
}