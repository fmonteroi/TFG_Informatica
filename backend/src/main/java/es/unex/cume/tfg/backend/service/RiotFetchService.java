package es.unex.cume.tfg.backend.service;

import es.unex.cume.tfg.backend.model.Platform;
import es.unex.cume.tfg.backend.riot.client.RiotAccountClient;
import es.unex.cume.tfg.backend.riot.client.RiotMatchClient;
import es.unex.cume.tfg.backend.riot.dto.RiotMatchDto;

import java.util.List;

public interface RiotFetchService {
    String fetchPuuid(Platform platform, String gameName, String tagLine);
    List<String> fetchRecentMatchIds(Platform platform, String puuid, int count);
    RiotMatchDto fetchMatchByMatchId(Platform platform, String matchId);
    List<RiotMatchDto> fetchRecentMatches(Platform platform, String gameName, String tagLine, int count);
}
