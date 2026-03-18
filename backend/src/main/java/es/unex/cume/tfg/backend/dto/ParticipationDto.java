package es.unex.cume.tfg.backend.dto;

import es.unex.cume.tfg.backend.model.Build;
import es.unex.cume.tfg.backend.model.Champion;
import es.unex.cume.tfg.backend.model.Participation;
import es.unex.cume.tfg.backend.model.Player;

import java.time.Instant;

public record ParticipationDto(
        Long id,
        String matchId,
        Integer queueId,
        String puuid,
        String gameName,
        String tagLine,
        Integer championId,
        String championName,
        Integer teamId,
        boolean win,
        Integer kills,
        Integer deaths,
        Integer assists,
        Instant gameStartAt,
        String teamPosition,
        BuildDto build
) {
    public static ParticipationDto fromEntity(Participation participation) {
        Player player = participation.getPlayer();
        Champion champion = participation.getChampion();
        Build build = participation.getBuild();

        String matchId = null;
        Integer queueId = null;
        if (participation.getMatch() != null) {
            matchId = participation.getMatch().getMatchId();
            queueId = participation.getMatch().getQueueId();
        }

        String puuid = null;
        String gameName = null;
        String tagLine = null;
        if (player != null) {
            puuid = player.getPuuid();
            gameName = player.getGameName();
            tagLine = player.getTagLine();
        }

        Integer championId = null;
        String championName = null;
        if (champion != null) {
            championId = champion.getChampionId();
            championName = champion.getChampionName();
        }

        BuildDto buildDto = null;
        if (build != null) {
            buildDto = BuildDto.fromEntity(build);
        }

        return new ParticipationDto(
                participation.getId(),
                matchId,
                queueId,
                puuid,
                gameName,
                tagLine,
                championId,
                championName,
                participation.getTeamId(),
                participation.isWin(),
                participation.getKills(),
                participation.getDeaths(),
                participation.getAssists(),
                participation.getGameStartAt(),
                participation.getTeamPosition(),
                buildDto
        );
    }
}