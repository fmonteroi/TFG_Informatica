package es.unex.cume.tfg.backend.service;

import es.unex.cume.tfg.backend.model.Platform;
import es.unex.cume.tfg.backend.model.Player;
import es.unex.cume.tfg.backend.riot.dto.MatchDto;

/**
 * Service that synchronizes basic player data from match participants.
 */
public interface PlayerSyncService {
    /**
     * Synchronizes basic player fields from a Riot participant payload.
     *
     * @param participant the Riot participant DTO.
     * @param platform the Riot platform.
     * @return the synchronized player.
     */
    Player syncBasicPlayer(MatchDto.Participant participant, Platform platform);
}
