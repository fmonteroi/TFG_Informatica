package es.unex.cume.tfg.backend.service;

import es.unex.cume.tfg.backend.model.Platform;
import es.unex.cume.tfg.backend.riot.client.*;
import es.unex.cume.tfg.backend.riot.dto.CurrentGameInfoDto;
import es.unex.cume.tfg.backend.riot.dto.LeagueEntryDto;
import es.unex.cume.tfg.backend.riot.dto.MatchDto;
import es.unex.cume.tfg.backend.riot.dto.SummonerDto;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Default implementation of RiotFetchService.
 */
@Service
public class RiotFetchServiceImpl implements RiotFetchService {

    private final AccountClient accountClient;
    private final MatchClient matchClient;
    private final SummonerClient summonerClient;
    private final SpectatorClient spectatorClient;
    private final LeagueClient leagueClient;

    /**
     * Creates the Riot data service.
     *
     * @param accountClient Riot account client
     * @param matchClient Riot match client
     * @param summonerClient Riot summoner client
     * @param spectatorClient Riot spectator client
     * @param leagueClient Riot league client
     */
    public RiotFetchServiceImpl(AccountClient accountClient,
                                MatchClient matchClient,
                                SummonerClient summonerClient,
                                SpectatorClient spectatorClient,
                                LeagueClient leagueClient
                                ) {

        this.accountClient = accountClient;
        this.matchClient = matchClient;
        this.summonerClient = summonerClient;
        this.spectatorClient = spectatorClient;
        this.leagueClient = leagueClient;
    }

    /**
     * Gets the PUUID of a player from their Riot ID.
     *
     * @param platform the platform/region
     * @param gameName the player's game name
     * @param tagLine  the player's tag line
     * @return the player's PUUID
     */
    public String fetchPuuid(Platform platform, String gameName, String tagLine) {
        return accountClient.fetchByRiotId(platform, gameName, tagLine).puuid();
    }

    /**
     * Gets summoner profile data by PUUID.
     *
     * @param platform Riot platform
     * @param puuid player PUUID
     * @return summoner profile data
     */
    public SummonerDto fetchSummoner(Platform platform, String puuid) {
        return summonerClient.fetchByPuuid(platform, puuid);
    }

    /**
     * Gets a single page of match IDs from Riot API.
     *
     * @param platform  the platform/region
     * @param puuid     the player's PUUID
     * @param count     the number of match IDs to fetch (max 100)
     * @param startTime epoch seconds to start from (null for no time filter)
     * @return the list of match IDs
     */
    public List<String> fetchMatchIdsSince(Platform platform, String puuid, int count, Long startTime) {
        return matchClient.getMatchIdsByPuuidSince(platform, puuid, count, 0, startTime);
    }

    /**
     * Gets all match IDs for a player in blocks of 100.
     * Stops when there are no more matches or the maxMatches limit is reached.
     *
     * Note: Unused because development api kay has very low rate limits. Used for more than 100 matches.
     *
     * @param platform   the platform/region
     * @param puuid      the player's PUUID
     * @param maxMatches the maximum number of match IDs to fetch
     * @param startTime  epoch seconds to start from (null for no time filter)
     * @return the list of all match IDs found
     */
    public List<String> fetchAllMatchIdsSince(Platform platform, String puuid, int maxMatches, Long startTime) {
        // Stores IDs from every requested page
        List<String> allMatchIds = new ArrayList<>();

        // Starts Riot pagination in blocks of 100
        int start = 0;
        int batchSize = 100;

        // Continues until the limit or the end of the history
        while (allMatchIds.size() < maxMatches) {
            int remaining = maxMatches - allMatchIds.size();
            int count = batchSize;

            if (remaining < batchSize) {
                count = remaining;
            }

            // Gets the next page of match IDs
            List<String> batch = matchClient.getMatchIdsByPuuidSince(platform, puuid, count, start, startTime);

            if (batch.isEmpty()) {
                return allMatchIds;
            }

            allMatchIds.addAll(batch);

            if (batch.size() < count) {
                return allMatchIds;
            }

            // Moves the offset to the next Riot page
            start = start + batchSize;
        }

        return allMatchIds;
    }

    /**
     * Gets match information by match ID.
     *
     * @param platform the platform/region
     * @param matchId  the match ID
     * @return the match DTO
     */
    public MatchDto fetchMatchByMatchId(Platform platform, String matchId) {
        return matchClient.getMatchByMatchId(platform, matchId);
    }

    /**
     * Gets the recent matches of a player.
     *
     * @param platform the platform/region
     * @param gameName the player's game name
     * @param tagLine  the player's tag line
     * @param count    the number of recent matches to fetch
     * @return the list of match DTOs
     */
    public List<MatchDto> fetchRecentMatches(Platform platform, String gameName, String tagLine, int count) {
        // Resolves the account before getting its match IDs
        String puuid = fetchPuuid(platform, gameName, tagLine);
        List<String> matchIds = fetchMatchIdsSince(platform, puuid, count, null);

        return matchIds.stream()
                .map(matchId -> fetchMatchByMatchId(platform, matchId))
                .toList();
    }

    /**
     * Gets current game information when the player is in a game.
     *
     * @param platform Riot platform
     * @param puuid player PUUID
     * @return the current game when Riot reports one.
     */
    public Optional<CurrentGameInfoDto> fetchCurrentGame(Platform platform, String puuid) {
        return spectatorClient.fetchCurrentGameByPuuid(platform, puuid);
    }

    /**
     * Gets ranked queue results by PUUID.
     *
     * @param platform Riot platform
     * @param puuid player PUUID
     * @return ranked queue results
     */
    public List<LeagueEntryDto> fetchLeagueEntries(Platform platform, String puuid) {
        return leagueClient.getEntriesByPuuid(platform, puuid);
    }


}
