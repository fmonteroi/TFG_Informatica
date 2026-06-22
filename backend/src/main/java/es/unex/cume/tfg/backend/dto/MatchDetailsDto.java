package es.unex.cume.tfg.backend.dto;

import es.unex.cume.tfg.backend.model.Match;
import es.unex.cume.tfg.backend.model.Participation;

import java.time.Instant;
import java.util.List;

/**
 * DTO that exposes a match with all its participations.
 */
public record MatchDetailsDto(
        String matchId,
        Integer queueId,
        Instant gameStartAt,
        Long gameDuration,
        String gameVersion,
        List<ParticipationDto> participations
) {
    /**
     * Creates a DTO from a Match entity and its participations.
     *
     * @param match the match entity to convert.
     * @param participations the participations linked to the match.
     * @return the match details DTO.
     */
    public static MatchDetailsDto from(Match match, List<Participation> participations) {
        return new MatchDetailsDto(
                match.getMatchId(),
                match.getQueueId(),
                match.getGameStartAt(),
                match.getGameDuration(),
                match.getGameVersion(),
                participations.stream()
                        .map(ParticipationDto::fromEntity)
                        .toList()
        );
    }
}
