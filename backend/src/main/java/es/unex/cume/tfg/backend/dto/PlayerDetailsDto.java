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
    /**
     * Creates a complete player response from profile, rank and history data.
     *
     * @param player player profile
     * @param rankedRanks current ranked results
     * @param participations stored match history
     * @return complete player details
     */
    public static PlayerDetailsDto from(Player player, List<RankedRank> rankedRanks, List<Participation> participations) {
        // Maps optional calculated statistics
        PlayerStatsDto statsDto = null;

        if (player.getStats() != null) {
            statsDto = PlayerStatsDto.fromEntity(player.getStats());
        }

        // Groups profile, ranks and match history in one response
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
