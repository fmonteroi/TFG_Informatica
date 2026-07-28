package es.unex.cume.tfg.backend.dto;

import es.unex.cume.tfg.backend.model.Build;
import es.unex.cume.tfg.backend.model.Champion;
import es.unex.cume.tfg.backend.model.Match;
import es.unex.cume.tfg.backend.model.Participation;
import es.unex.cume.tfg.backend.model.Role;

import java.time.Instant;

/**
 * DTO that exposes one recent build from a professional player.
 */
public record ProBuildDto(
        String matchId,
        Instant gameStartAt,
        String gameVersion,
        Integer queueId,
        Integer championId,
        String championName,
        Role teamPosition,
        BuildDto build
) {
    /**
     * Creates a DTO from a professional player's build.
     *
     * @param build build to convert
     * @return professional build DTO
     */
    public static ProBuildDto fromEntity(Build build) {
        // Gets the match and champion data linked to the build
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
