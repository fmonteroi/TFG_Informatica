package es.unex.cume.tfg.backend.service;

import es.unex.cume.tfg.backend.model.Match;
import es.unex.cume.tfg.backend.model.Participation;
import es.unex.cume.tfg.backend.model.Platform;

import java.util.List;
import java.util.Optional;

public interface MatchService {
    Optional<Match> findByMatchId(String matchId);
    List<Match> findMatchHistoryByPuuid(String puuid);
    Match findMatchDetails(String matchId);
    List<Match> loadRecentMatches(Platform platform, String gameName, String tagLine, int count);
    List<Participation> findParticipationsByMatchId(String matchId);
}
