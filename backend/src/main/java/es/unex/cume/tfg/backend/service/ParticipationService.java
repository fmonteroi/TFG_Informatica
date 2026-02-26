package es.unex.cume.tfg.backend.service;

import es.unex.cume.tfg.backend.model.Match;
import es.unex.cume.tfg.backend.model.Participation;
import es.unex.cume.tfg.backend.riot.dto.RiotMatchDto;

import java.util.List;

public interface ParticipationService {
    List<Participation> findByPuuid(String puuid);
    List<Participation> findByMatchId(String matchId);
    List<Participation> saveParticipationsFromDto(RiotMatchDto riotMatchDto, Match match);
}

