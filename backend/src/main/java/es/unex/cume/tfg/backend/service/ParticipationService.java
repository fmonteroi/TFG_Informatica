package es.unex.cume.tfg.backend.service;

import es.unex.cume.tfg.backend.model.Match;
import es.unex.cume.tfg.backend.model.Participation;
import es.unex.cume.tfg.backend.model.Platform;
import es.unex.cume.tfg.backend.riot.dto.MatchDto;

import java.util.List;

/**
 * Service that manages participation queries and match participant imports.
 */
public interface ParticipationService {
    /**
     * Finds all participations for a player.
     *
     * @param puuid the player PUUID.
     * @return the player's participations.
     */
    List<Participation> findByPuuid(String puuid);

    /**
     * Finds all participations for a match.
     *
     * @param matchId the Riot match ID.
     * @return the match participations.
     */
    List<Participation> findByMatchId(String matchId);

    /**
     * Saves all participations from a Riot match DTO.
     *
     * @param matchDto the Riot match DTO.
     * @param match the saved match entity.
     * @param platform the Riot platform.
     */
    void saveParticipationsFromDto(MatchDto matchDto, Match match, Platform platform);
}
