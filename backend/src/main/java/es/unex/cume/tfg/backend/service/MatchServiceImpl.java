package es.unex.cume.tfg.backend.service;

import es.unex.cume.tfg.backend.dto.MatchDetailsDto;
import es.unex.cume.tfg.backend.exception.MatchNotFoundException;
import es.unex.cume.tfg.backend.model.Match;
import es.unex.cume.tfg.backend.model.Participation;
import es.unex.cume.tfg.backend.model.Platform;
import es.unex.cume.tfg.backend.repository.MatchRepository;
import es.unex.cume.tfg.backend.riot.dto.MatchDto;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class MatchServiceImpl implements MatchService {

    private final MatchRepository matchRepository;
    private final ParticipationService participationService;
    private final RiotFetchService riotFetchService;

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
     * @param matchId
     * @return
     */
    @Override
    public MatchDetailsDto findMatchDetails(String matchId) {
        Match match = findMatch(matchId);
        List<Participation> participations = participationService.findByMatchId(matchId);
        return MatchDetailsDto.from(match, participations);
    }

    /**
     * Finds the match history of a player given their PUUID.
     * Navigates through Participation to find the matches.
     *
     * @param puuid the player's PUUID
     * @param count the number of matches to return
     * @return the list of matches
     */
    @Override
    public List<Match> findMatchHistory(String puuid, int count) {
        return matchRepository.findByParticipantPuuid(puuid, PageRequest.of(0, count));
    }

    /**
     * Fetches recent matches from Riot API by PUUID and saves them in the database.
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
     * Fetches matches from Riot API since a given timestamp and saves them in the database.
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
     * Fetches all matches from Riot API since a given date, paginating automatically.
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
        for (String matchId : matchIds) {
            if (!matchRepository.existsByMatchId(matchId)) {
                MatchDto matchDto = riotFetchService.fetchMatchByMatchId(platform, matchId);
                Match match = toEntity(matchDto);
                Match savedMatch = matchRepository.save(match);

                participationService.saveParticipationsFromDto(matchDto, savedMatch, platform);

                savedMatches.add(savedMatch);
            }
        }
        return savedMatches;
    }
}