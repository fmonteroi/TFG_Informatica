package es.unex.cume.tfg.backend.dto;

import es.unex.cume.tfg.backend.model.Build;
import es.unex.cume.tfg.backend.model.Champion;
import es.unex.cume.tfg.backend.model.Match;
import es.unex.cume.tfg.backend.model.Participation;
import es.unex.cume.tfg.backend.model.Player;
import es.unex.cume.tfg.backend.model.Professional;
import es.unex.cume.tfg.backend.model.Role;

import java.time.Instant;

/**
 * DTO that exposes a professional player's recent build for a champion.
 */
public record ChampionProBuildDto(
        String matchId,
        Instant gameStartAt,
        String gameVersion,
        Integer queueId,
        Integer championId,
        String championName,
        String puuid,
        String proName,
        String gameName,
        String tagLine,
        String teamName,
        String league,
        Role teamPosition,
        BuildDto build
) {
    /**
     * Creates a DTO from a Build entity linked to a professional player.
     *
     * @param build the build entity to convert.
     * @return the professional build DTO.
     */
    public static ChampionProBuildDto fromEntity(Build build) {
        // Gets the related match, champion and professional data
        Participation participation = build.getParticipation();
        Match match = participation.getMatch();
        Champion champion = participation.getChampion();
        Player player = participation.getPlayer();
        Professional professional = player.getProfessional();

        return new ChampionProBuildDto(
                match.getMatchId(),
                match.getGameStartAt(),
                match.getGameVersion(),
                match.getQueueId(),
                champion.getChampionId(),
                champion.getChampionName(),
                player.getPuuid(),
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
