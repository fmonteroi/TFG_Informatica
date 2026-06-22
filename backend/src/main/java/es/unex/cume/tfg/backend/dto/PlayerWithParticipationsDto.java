package es.unex.cume.tfg.backend.dto;

import es.unex.cume.tfg.backend.model.Participation;
import es.unex.cume.tfg.backend.model.Player;

import java.util.List;

/**
 * DTO that groups a player with their match history participations.
 */
public record PlayerWithParticipationsDto(
        PlayerDto player,
        List<ParticipationDto> participations
) {
    /**
     * Creates a DTO from a player and their participations.
     *
     * @param player the player entity to convert.
     * @param participations the player's participations.
     * @return the combined player DTO.
     */
    public static PlayerWithParticipationsDto from(Player player, List<Participation> participations) {
        return new PlayerWithParticipationsDto(
                PlayerDto.fromEntity(player),
                participations.stream()
                        .map(ParticipationDto::fromEntity)
                        .toList()
        );
    }
}
