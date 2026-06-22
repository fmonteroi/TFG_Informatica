package es.unex.cume.tfg.backend.service;

import es.unex.cume.tfg.backend.model.Platform;
import es.unex.cume.tfg.backend.riot.dto.CurrentGameInfoDto;
import es.unex.cume.tfg.backend.riot.dto.MatchDto;
import es.unex.cume.tfg.backend.riot.dto.SummonerDto;

import java.util.List;
import java.util.Optional;

/**
 * Service facade that groups Riot API fetch operations.
 */
public interface RiotFetchService {
    /**
     * Fetches a player's PUUID from their Riot ID.
     *
     * @param platform the Riot platform.
     * @param gameName the Riot game name.
     * @param tagLine the Riot tag line.
     * @return the player PUUID.
     */
    String fetchPuuid(Platform platform, String gameName, String tagLine);

    /**
     * Fetches summoner profile data by PUUID.
     *
     * @param platform the Riot platform.
     * @param puuid the player PUUID.
     * @return the summoner DTO.
     */
    SummonerDto fetchSummoner(Platform platform, String puuid);

    /**
     * Fetches match IDs since an optional start time.
     *
     * @param platform the Riot platform.
     * @param puuid the player PUUID.
     * @param count the number of match IDs to fetch.
     * @param startTime the epoch seconds lower bound, or null.
     * @return the match IDs.
     */
    List<String> fetchMatchIdsSince(Platform platform, String puuid, int count, Long startTime);

    /**
     * Fetches match IDs using Riot pagination.
     *
     * @param platform the Riot platform.
     * @param puuid the player PUUID.
     * @param maxMatches the maximum number of match IDs to fetch.
     * @param startTime the epoch seconds lower bound, or null.
     * @return the match IDs.
     */
    List<String> fetchAllMatchIdsSince(Platform platform, String puuid, int maxMatches, Long startTime);

    /**
     * Fetches a full match payload by match ID.
     *
     * @param platform the Riot platform.
     * @param matchId the Riot match ID.
     * @return the match DTO.
     */
    MatchDto fetchMatchByMatchId(Platform platform, String matchId);

    /**
     * Fetches recent match payloads by Riot ID.
     *
     * @param platform the Riot platform.
     * @param gameName the Riot game name.
     * @param tagLine the Riot tag line.
     * @param count the number of matches to fetch.
     * @return the match DTOs.
     */
    List<MatchDto> fetchRecentMatches(Platform platform, String gameName, String tagLine, int count);

    /**
     * Fetches current game information by PUUID.
     *
     * @param platform the Riot platform.
     * @param puuid the player PUUID.
     * @return the current game if Riot reports one.
     */
    Optional<CurrentGameInfoDto> fetchCurrentGame(Platform platform, String puuid);
}
