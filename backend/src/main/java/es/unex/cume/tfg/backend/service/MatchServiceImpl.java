package es.unex.cume.tfg.backend.service;

import es.unex.cume.tfg.backend.model.Match;
import es.unex.cume.tfg.backend.model.Participation;
import es.unex.cume.tfg.backend.model.Platform;
import es.unex.cume.tfg.backend.repository.MatchRepository;
import es.unex.cume.tfg.backend.riot.dto.RiotMatchDto;
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
     * @return the match if found, otherwise an empty Optional
     */
    @Override
    public Optional<Match> findByMatchId(String matchId) {
        return matchRepository.findByMatchId(matchId);
    }

    /**
     * Finds the match history of a player given their PUUID.
     * Navigates through Participation to find the matches.
     *
     * @param puuid the player's PUUID
     * @return the list of matches
     */
    @Override
    public List<Match> findMatchHistoryByPuuid(String puuid) {
        List<Participation> participations = participationService.findByPuuid(puuid);
        List<Match> matches = new ArrayList<>();
        for (Participation participation : participations) {
            matches.add(participation.getMatch());
        }
        return matches;
    }

    /**
     * Finds the details of a match including its participations.
     *
     * @param matchId the match ID
     * @return the match with its participations
     */
    @Override
    public Match findMatchDetails(String matchId) {
        Optional<Match> optional = matchRepository.findByMatchId(matchId);
        if (optional.isPresent()) {
            return optional.get();
        } else {
            throw new RuntimeException("Match not found: " + matchId);
        }
    }

    /**
     * Fetches recent matches from Riot API and saves them in the database.
     * Also saves the participations of each match.
     *
     * @param platform the platform/region
     * @param gameName the player's Riot game name
     * @param tagLine  the player's tag line
     * @param count    the number of recent matches to fetch
     * @return the list of newly saved matches
     */
    @Override
    public List<Match> loadRecentMatches(Platform platform, String gameName, String tagLine, int count) {
        List<RiotMatchDto> riotMatches = riotFetchService.fetchRecentMatches(platform, gameName, tagLine, count);

        List<Match> savedMatches = new ArrayList<>();
        for (RiotMatchDto riotMatchDto : riotMatches) {
            String matchId = riotMatchDto.metadata().matchId();
            boolean alreadyExists = matchRepository.existsByMatchId(matchId);
            if (!alreadyExists) {
                Match match = toEntity(riotMatchDto);
                Match savedMatch = matchRepository.save(match);

                participationService.saveParticipationsFromDto(riotMatchDto, savedMatch);

                savedMatches.add(savedMatch);
            }
        }
        return savedMatches;
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
     * Converts a RiotMatchDto to a Match entity.
     *
     * @param riotMatchDto the Riot match DTO
     * @return the Match entity
     */
    private Match toEntity(RiotMatchDto riotMatchDto) {
        Match match = new Match();
        match.setMatchId(riotMatchDto.metadata().matchId());
        match.setQueueId(riotMatchDto.info().queueId());
        match.setGameDuration(riotMatchDto.info().gameDuration());
        match.setGameVersion(riotMatchDto.info().gameVersion());
        match.setGameStartAt(Instant.ofEpochMilli(riotMatchDto.info().gameStartTimestamp()));
        return match;
    }
}
