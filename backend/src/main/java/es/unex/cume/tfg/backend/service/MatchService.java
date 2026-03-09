package es.unex.cume.tfg.backend.service;

import es.unex.cume.tfg.backend.dto.MatchDetailsDto;
import es.unex.cume.tfg.backend.model.Match;
import es.unex.cume.tfg.backend.model.Participation;
import es.unex.cume.tfg.backend.model.Platform;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

public interface MatchService {
    Match findMatch(String matchId);
    MatchDetailsDto findMatchDetails(String matchId);
    List<Match> findMatchHistory(String puuid, int count);
    List<Match> loadMatches(Platform platform, String puuid, int count);
    List<Match> loadMatchesSince(Platform platform, String puuid, int count, Instant since);
    List<Match> loadAllMatchesSince(Platform platform, String puuid, int maxMatches, Instant since);
    List<Participation> findParticipationsByMatchId(String matchId);
}
