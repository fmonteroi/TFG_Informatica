package es.unex.cume.tfg.backend.service;

import es.unex.cume.tfg.backend.model.Platform;
import es.unex.cume.tfg.backend.model.Player;
import es.unex.cume.tfg.backend.riot.dto.MatchDto;

public interface PlayerSyncService {
    Player syncBasicPlayer(MatchDto.Participant participant, Platform platform);
}
