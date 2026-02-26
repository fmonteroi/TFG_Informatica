package es.unex.cume.tfg.backend.service;

import es.unex.cume.tfg.backend.model.Platform;
import es.unex.cume.tfg.backend.riot.client.RiotAccountClient;
import es.unex.cume.tfg.backend.riot.client.RiotMatchClient;
import es.unex.cume.tfg.backend.riot.dto.RiotMatchDto;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RiotFetchServiceImpl implements RiotFetchService{

    private final RiotAccountClient riotAccountClient;
    private final RiotMatchClient riotMatchClient;

    public RiotFetchServiceImpl(RiotAccountClient riotAccountClient, RiotMatchClient riotMatchClient) {
        this.riotAccountClient = riotAccountClient;
        this.riotMatchClient = riotMatchClient;
    }

    /**
     * Fetches the PUUID of a player given their Riot game name and tag line.
     *
     * @param platform
     * @param gameName
     * @param tagLine
     * @return
     */
    public String fetchPuuid(Platform platform, String gameName, String tagLine) {
        return riotAccountClient.fetchByRiotId(platform, gameName, tagLine).puuid();
    }


    /**
     * Fetches the IDs of a player recent matches.
     *
     * @param platform
     * @param puuid
     * @param count
     * @return
     */
    public List<String> fetchRecentMatchIds(Platform platform, String puuid, int count) {
        return riotMatchClient.getMatchIdsByPuuid(platform, puuid, count);
    }


    /**
     * Fetches the match information of a match.
     *
     * @param platform
     * @param matchId
     * @return
     */
    public RiotMatchDto fetchMatchByMatchId(Platform platform, String matchId) {
        return riotMatchClient.getMatchByMatchId(platform, matchId);
    }

    /**
     * Fetches the recent matches of a player.
     *
     * @param platform
     * @param gameName
     * @param tagLine
     * @param count
     * @return
     */
    public List<RiotMatchDto> fetchRecentMatches(Platform platform, String gameName, String tagLine, int count) {
        String puuid = fetchPuuid(platform, gameName, tagLine);
        List<String> matchIds = fetchRecentMatchIds(platform, puuid, count);

        return matchIds.stream()
                .map(matchId -> fetchMatchByMatchId(platform, matchId))
                .toList();
    }
}

