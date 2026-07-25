package es.unex.cume.tfg.backend.dto;

import es.unex.cume.tfg.backend.model.Participation;
import es.unex.cume.tfg.backend.model.Player;
import es.unex.cume.tfg.backend.model.RankedRank;

import java.util.List;

/**
 * DTO that groups a player with their match history participations.
 */
public record PlayerDetailsDto(
        PlayerDto player,
        PlayerStatsDto stats,
        List<RankedRankDto> rankedRanks,
        List<ParticipationDto> participations
) {
    public static PlayerDetailsDto from(Player player, List<RankedRank> rankedRanks, List<Participation> participations) {
        PlayerStatsDto statsDto = null;

        if (player.getStats() != null) {
            statsDto = PlayerStatsDto.fromEntity(player.getStats());
        }

        return new PlayerDetailsDto(PlayerDto.fromEntity(player), statsDto,
                rankedRanks.stream()
                        .map(RankedRankDto::fromEntity)
                        .toList(),
                participations.stream()
                        .map(ParticipationDto::fromEntity)
                        .toList()
        );
    }
}
