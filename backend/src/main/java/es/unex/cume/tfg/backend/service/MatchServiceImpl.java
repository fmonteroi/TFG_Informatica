package es.unex.cume.tfg.backend.service;

import es.unex.cume.tfg.backend.dto.MatchDetailsDto;
import es.unex.cume.tfg.backend.exception.MatchNotFoundException;
import es.unex.cume.tfg.backend.model.Match;
import es.unex.cume.tfg.backend.model.Participation;
import es.unex.cume.tfg.backend.model.Platform;
import es.unex.cume.tfg.backend.repository.MatchRepository;
import es.unex.cume.tfg.backend.riot.dto.MatchDto;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Optional;

/**
 * Default implementation of MatchService.
 */
@Service
public class MatchServiceImpl implements MatchService {

    private final MatchRepository matchRepository;
    private final ParticipationService participationService;
    private final RiotFetchService riotFetchService;

    /**
     * Creates the match service.
     *
     * @param matchRepository match repository
     * @param participationService participation service
     * @param riotFetchService Riot data service
     */
    public MatchServiceImpl(MatchRepository matchRepository,
                            ParticipationService participationService,
                            RiotFetchService riotFetchService) {
        this.matchRepository = matchRepository;
        this.participationService = participationService;
        this.riotFetchService = riotFetchService;
    }

    /**
     * Finds a match by its ID.
     *
     * @param matchId the match ID
     * @return the match with its participations
     */
    @Override
    public Match findMatch(String matchId) {
        Optional<Match> optionalMatch = matchRepository.findByMatchId(matchId);

        if(optionalMatch.isEmpty()){
            throw new MatchNotFoundException(matchId);
        }

        return optionalMatch.get();
    }

    /**
     * Finds the details of a match by its ID.
     *
     * @param matchId Riot match identifier
     * @return the match details DTO.
     */
    @Override
    public MatchDetailsDto findMatchDetails(String matchId) {
        Match match = findMatch(matchId);
        List<Participation> participations = participationService.findByMatchId(matchId);
        return MatchDetailsDto.from(match, participations);
    }

    /**
     * Gets recent matches from Riot API by PUUID and saves them in the database.
     * Delegates to loadMatchesSince with null timestamp.
     *
     * @param platform the platform/region
     * @param puuid    the player's PUUID
     * @param count    the number of recent matches to fetch
     * @return the list of newly saved matches
     */
    @Override
    public List<Match> loadMatches(Platform platform, String puuid, int count) {
        return loadMatchesSince(platform, puuid, count, null);
    }

    /**
     * Gets matches from Riot API since a given timestamp and saves them in the database.
     * Used for periodic updates to fetch new matches since the last update.
     *
     * @param platform  the platform/region
     * @param puuid     the player's PUUID
     * @param count     the number of recent matches to fetch
     * @param since the timestamp to start from (null to fetch without time filter)
     * @return the list of newly saved matches
     */
    @Override
    public List<Match> loadMatchesSince(Platform platform, String puuid, int count, Instant since) {
        Long startTime = null;

        if (since != null) {
            startTime = since.getEpochSecond();
        }

        List<String> matchIds = riotFetchService.fetchMatchIdsSince(platform, puuid, count, startTime);
        return saveMatches(platform, matchIds);
    }

    /**
     * Gets all matches from Riot API since a given date, paginating automatically.
     * Used when creating a new player to load their full match history.
     *
     * Note: Unused because development api kay has very low rate limits. Used for more than 100 matches.
     *
     * @param platform   the platform/region
     * @param puuid      the player's PUUID
     * @param maxMatches the maximum number of matches to load
     * @param since      the date to start from (null to fetch without time filter)
     * @return the list of newly saved matches
     */
    @Override
    public List<Match> loadAllMatchesSince(Platform platform, String puuid, int maxMatches, Instant since) {
        Long startTime = null;

        if (since != null) {
            startTime = since.getEpochSecond();
        }

        List<String> matchIds = riotFetchService.fetchAllMatchIdsSince(platform, puuid, maxMatches, startTime);
        return saveMatches(platform, matchIds);
    }

    /**
     * Finds the participations of a match by its match ID.
     *
     * @param matchId the match ID
     * @return the list of participations
     */
    @Override
    public List<Participation> findParticipationsByMatchId(String matchId) {
        return participationService.findByMatchId(matchId);
    }

    /**
     * Converts a MatchDto to a Match entity.
     *
     * @param matchDto the Riot match DTO
     * @return the Match entity
     */
    private Match toEntity(MatchDto matchDto) {
        Match match = new Match();
        match.setMatchId(matchDto.metadata().matchId());
        match.setQueueId(matchDto.info().queueId());
        match.setGameDuration(matchDto.info().gameDuration());
        match.setGameVersion(matchDto.info().gameVersion());
        match.setGameStartAt(Instant.ofEpochMilli(matchDto.info().gameStartTimestamp()));
        return match;
    }

    /**
     * Saves new matches and their participations from Riot API.
     * Skips matches that already exist in the database.
     *
     * @param platform the platform/region
     * @param matchIds the list of match IDs to save
     * @return the list of newly saved matches
     */
    private List<Match> saveMatches(Platform platform, List<String> matchIds) {
        List<Match> savedMatches = new ArrayList<>();
        for (String matchId : new LinkedHashSet<>(matchIds)) {
            // If match already exists, skips it to avoid duplicates
            if (matchRepository.existsByMatchId(matchId)) {
                continue;
            }

            MatchDto matchDto = riotFetchService.fetchMatchByMatchId(platform, matchId);
            Match match = toEntity(matchDto);

            try {
                Match savedMatch = matchRepository.save(match);
                participationService.saveParticipationsFromDto(matchDto, savedMatch, platform);
                savedMatches.add(savedMatch);
            } catch (DataIntegrityViolationException ex) {
                // Another refresh inserted the same match while this one was processing it
            }
        }
        return savedMatches;
    }
}
