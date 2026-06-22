package es.unex.cume.tfg.backend.service;

import es.unex.cume.tfg.backend.dto.MatchDetailsDto;
import es.unex.cume.tfg.backend.model.Match;
import es.unex.cume.tfg.backend.model.Participation;
import es.unex.cume.tfg.backend.model.Platform;

import java.time.Instant;
import java.util.List;

/**
 * Service that manages match import and match detail queries.
 */
public interface MatchService {
    /**
     * Finds a match by ID.
     *
     * @param matchId the Riot match ID.
     * @return the match.
     */
    Match findMatch(String matchId);

    /**
     * Finds match details including participations.
     *
     * @param matchId the Riot match ID.
     * @return the match details.
     */
    MatchDetailsDto findMatchDetails(String matchId);

    /**
     * Loads recent matches for a player.
     *
     * @param platform the Riot platform.
     * @param puuid the player PUUID.
     * @param count the number of matches to load.
     * @return the newly saved matches.
     */
    List<Match> loadMatches(Platform platform, String puuid, int count);

    /**
     * Loads matches for a player since a given timestamp.
     *
     * @param platform the Riot platform.
     * @param puuid the player PUUID.
     * @param count the number of matches to load.
     * @param since the lower timestamp bound.
     * @return the newly saved matches.
     */
    List<Match> loadMatchesSince(Platform platform, String puuid, int count, Instant since);

    /**
     * Loads matches for a player using Riot pagination.
     *
     * @param platform the Riot platform.
     * @param puuid the player PUUID.
     * @param maxMatches the maximum number of matches to load.
     * @param since the lower timestamp bound.
     * @return the newly saved matches.
     */
    List<Match> loadAllMatchesSince(Platform platform, String puuid, int maxMatches, Instant since);

    /**
     * Finds all participations for a match.
     *
     * @param matchId the Riot match ID.
     * @return the match participations.
     */
    List<Participation> findParticipationsByMatchId(String matchId);
}
