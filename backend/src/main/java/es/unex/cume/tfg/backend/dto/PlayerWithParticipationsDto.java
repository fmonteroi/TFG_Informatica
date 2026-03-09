package es.unex.cume.tfg.backend.dto;

import es.unex.cume.tfg.backend.model.Participation;
import es.unex.cume.tfg.backend.model.Player;

import java.util.List;

public record PlayerWithParticipationsDto(
        PlayerDto player,
        List<ParticipationDto> participations
) {
    public static PlayerWithParticipationsDto from(Player player, List<Participation> participations) {
        return new PlayerWithParticipationsDto(
                PlayerDto.fromEntity(player),
                participations.stream()
                        .map(ParticipationDto::fromEntity)
                        .toList()
        );
    }
}
