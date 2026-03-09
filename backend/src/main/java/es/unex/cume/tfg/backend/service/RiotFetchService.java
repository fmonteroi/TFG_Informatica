package es.unex.cume.tfg.backend.service;

import es.unex.cume.tfg.backend.model.Platform;
import es.unex.cume.tfg.backend.riot.dto.CurrentGameInfoDto;
import es.unex.cume.tfg.backend.riot.dto.MatchDto;
import es.unex.cume.tfg.backend.riot.dto.SummonerDto;

import java.util.List;
import java.util.Optional;

public interface RiotFetchService {
    String fetchPuuid(Platform platform, String gameName, String tagLine);
    SummonerDto fetchSummoner(Platform platform, String puuid);
    List<String> fetchMatchIdsSince(Platform platform, String puuid, int count, Long startTime);
    List<String> fetchAllMatchIdsSince(Platform platform, String puuid, int maxMatches, Long startTime);
    MatchDto fetchMatchByMatchId(Platform platform, String matchId);
    List<MatchDto> fetchRecentMatches(Platform platform, String gameName, String tagLine, int count);
    Optional<CurrentGameInfoDto> fetchCurrentGame(Platform platform, String puuid);
}
