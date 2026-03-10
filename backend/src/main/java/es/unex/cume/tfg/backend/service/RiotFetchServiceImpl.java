package es.unex.cume.tfg.backend.service;

import es.unex.cume.tfg.backend.model.Platform;
import es.unex.cume.tfg.backend.riot.client.AccountClient;
import es.unex.cume.tfg.backend.riot.client.MatchClient;
import es.unex.cume.tfg.backend.riot.client.SpectatorClient;
import es.unex.cume.tfg.backend.riot.client.SummonerClient;
import es.unex.cume.tfg.backend.riot.dto.CurrentGameInfoDto;
import es.unex.cume.tfg.backend.riot.dto.MatchDto;
import es.unex.cume.tfg.backend.riot.dto.SummonerDto;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class RiotFetchServiceImpl implements RiotFetchService {

    private final AccountClient accountClient;
    private final MatchClient matchClient;
    private final SummonerClient summonerClient;
    private final SpectatorClient spectatorClient;

    public RiotFetchServiceImpl(AccountClient accountClient,
                                MatchClient matchClient,
                                SummonerClient summonerClient,
                                SpectatorClient spectatorClient) {

        this.accountClient = accountClient;
        this.matchClient = matchClient;
        this.summonerClient = summonerClient;
        this.spectatorClient = spectatorClient;
    }

    /**
     * Fetches the PUUID of a player given their Riot game name and tag line.
     *
     * @param platform the platform/region
     * @param gameName the player's game name
     * @param tagLine  the player's tag line
     * @return the player's PUUID
     */
    public String fetchPuuid(Platform platform, String gameName, String tagLine) {
        return accountClient.fetchByRiotId(platform, gameName, tagLine).puuid();
    }

    public SummonerDto fetchSummoner(Platform platform, String puuid) {
        return summonerClient.fetchByPuuid(platform, puuid);
    }

    /**
     * Fetches a single page of match IDs from Riot API.
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
     * Fetches all match IDs for a player paginating in blocks of 100.
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
        // Creates a list to save all match IDs
        List<String> allMatchIds = new ArrayList<>();

        // Variables to control pagination
        int start = 0;
        int batchSize = 100;

        // Loop until we have fetched maxMatches or there are no more matches
        while (allMatchIds.size() < maxMatches) {
            // Calculates left matches
            int remaining = maxMatches - allMatchIds.size();
            int count = batchSize;

            // If remaining is smaller than batch size, adjusts the count for the last fetch
            if (remaining < batchSize) {
                count = remaining;
            }

            // Fetches a batch of match IDs
            List<String> batch = matchClient.getMatchIdsByPuuidSince(platform, puuid, count, start, startTime);

            // If the batch is empty, returns all match IDs
            if (batch.isEmpty()) {
                return allMatchIds;
            }

            // Adds the batch to the list of all match IDs
            allMatchIds.addAll(batch);

            // If there are no more matches to fetch, returns all match IDs
            if (batch.size() < count) {
                return allMatchIds;
            }

            // Increments the start index for the next batch
            start = start + batchSize;
        }

        return allMatchIds;
    }

    /**
     * Fetches the match information of a match.
     *
     * @param platform the platform/region
     * @param matchId  the match ID
     * @return the match DTO
     */
    public MatchDto fetchMatchByMatchId(Platform platform, String matchId) {
        return matchClient.getMatchByMatchId(platform, matchId);
    }

    /**
     * Fetches the recent matches of a player.
     *
     * @param platform the platform/region
     * @param gameName the player's game name
     * @param tagLine  the player's tag line
     * @param count    the number of recent matches to fetch
     * @return the list of match DTOs
     */
    public List<MatchDto> fetchRecentMatches(Platform platform, String gameName, String tagLine, int count) {
        String puuid = fetchPuuid(platform, gameName, tagLine);
        List<String> matchIds = fetchMatchIdsSince(platform, puuid, count, null);

        return matchIds.stream()
                .map(matchId -> fetchMatchByMatchId(platform, matchId))
                .toList();
    }

    /**
     * Fetches the current game information of a player if they are currently in a game.
     *
     * @param platform
     * @param puuid
     * @return
     */
    @Override
    public Optional<CurrentGameInfoDto> fetchCurrentGame(Platform platform, String puuid) {
        return spectatorClient.fetchCurrentGameByPuuid(platform, puuid);
    }
}