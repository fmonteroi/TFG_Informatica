package es.unex.cume.tfg.backend.dto;

import es.unex.cume.tfg.backend.model.Build;
import es.unex.cume.tfg.backend.model.Champion;
import es.unex.cume.tfg.backend.model.Match;
import es.unex.cume.tfg.backend.model.Participation;
import es.unex.cume.tfg.backend.model.Player;
import es.unex.cume.tfg.backend.model.Professional;

import java.time.Instant;

public record ProBuildDto(
        Long buildId,
        String matchId,
        Instant gameStartAt,
        String gameVersion,
        Integer queueId,
        Integer championId,
        String championName,
        String proName,
        String gameName,
        String tagLine,
        String teamName,
        String league,
        String teamPosition,
        BuildDto build
) {
    public static ProBuildDto fromEntity(Build build) {
        Participation participation = build.getParticipation();
        Match match = participation.getMatch();
        Champion champion = participation.getChampion();
        Player player = participation.getPlayer();
        Professional professional = player.getProfessional();

        return new ProBuildDto(
                build.getId(),
                match.getMatchId(),
                match.getGameStartAt(),
                match.getGameVersion(),
                match.getQueueId(),
                champion.getChampionId(),
                champion.getChampionName(),
                professional.getProName(),
                player.getGameName(),
                player.getTagLine(),
                professional.getTeamName(),
                professional.getLeague(),
                participation.getTeamPosition(),
                BuildDto.fromEntity(build)
        );
    }
}