package es.unex.cume.tfg.backend.service;

import es.unex.cume.tfg.backend.model.Platform;
import es.unex.cume.tfg.backend.riot.dto.CurrentGameInfoDto;
import es.unex.cume.tfg.backend.riot.dto.LeagueEntryDto;
import es.unex.cume.tfg.backend.riot.dto.MatchDto;
import es.unex.cume.tfg.backend.riot.dto.SummonerDto;

import java.util.List;
import java.util.Optional;

/**
 * Service that groups Riot API calls.
 */
public interface RiotFetchService {
    /**
     * Gets a player's PUUID from their Riot ID.
     *
     * @param platform the Riot platform.
     * @param gameName the Riot game name.
     * @param tagLine the Riot tag line.
     * @return the player PUUID.
     */
    String fetchPuuid(Platform platform, String gameName, String tagLine);

    /**
     * Gets summoner profile data by PUUID.
     *
     * @param platform the Riot platform.
     * @param puuid the player PUUID.
     * @return the summoner DTO.
     */
    SummonerDto fetchSummoner(Platform platform, String puuid);

    /**
     * Gets match IDs since an optional start time.
     *
     * @param platform the Riot platform.
     * @param puuid the player PUUID.
     * @param count the number of match IDs to fetch.
     * @param startTime the epoch seconds lower bound, or null.
     * @return the match IDs.
     */
    List<String> fetchMatchIdsSince(Platform platform, String puuid, int count, Long startTime);

    /**
     * Gets match IDs using Riot pagination.
     *
     * @param platform the Riot platform.
     * @param puuid the player PUUID.
     * @param maxMatches the maximum number of match IDs to fetch.
     * @param startTime the epoch seconds lower bound, or null.
     * @return the match IDs.
     */
    List<String> fetchAllMatchIdsSince(Platform platform, String puuid, int maxMatches, Long startTime);

    /**
     * Gets full match data by match ID.
     *
     * @param platform the Riot platform.
     * @param matchId the Riot match ID.
     * @return the match DTO.
     */
    MatchDto fetchMatchByMatchId(Platform platform, String matchId);

    /**
     * Gets recent matches by Riot ID.
     *
     * @param platform the Riot platform.
     * @param gameName the Riot game name.
     * @param tagLine the Riot tag line.
     * @param count the number of matches to fetch.
     * @return the match DTOs.
     */
    List<MatchDto> fetchRecentMatches(Platform platform, String gameName, String tagLine, int count);

    /**
     * Gets current game information by PUUID.
     *
     * @param platform the Riot platform.
     * @param puuid the player PUUID.
     * @return the current game if Riot reports one.
     */
    Optional<CurrentGameInfoDto> fetchCurrentGame(Platform platform, String puuid);

    /**
     * Gets ranked queue results by PUUID.
     *
     * @param platform Riot platform
     * @param puuid player PUUID
     * @return ranked queue results
     */
    List<LeagueEntryDto> fetchLeagueEntries(Platform platform, String puuid);
}
