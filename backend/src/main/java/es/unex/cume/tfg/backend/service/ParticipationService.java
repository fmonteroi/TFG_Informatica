package es.unex.cume.tfg.backend.service;

import es.unex.cume.tfg.backend.model.Match;
import es.unex.cume.tfg.backend.model.Participation;
import es.unex.cume.tfg.backend.model.Platform;
import es.unex.cume.tfg.backend.riot.dto.MatchDto;

import java.util.List;

public interface ParticipationService {
    List<Participation> findByPuuid(String puuid);
    List<Participation> findByMatchId(String matchId);
    void saveParticipationsFromDto(MatchDto matchDto, Match match, Platform platform);
}