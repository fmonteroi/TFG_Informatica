package es.unex.cume.tfg.backend.service;

import es.unex.cume.tfg.backend.exception.ChampionCatalogException;
import es.unex.cume.tfg.backend.model.Build;
import es.unex.cume.tfg.backend.model.Champion;
import es.unex.cume.tfg.backend.model.Match;
import es.unex.cume.tfg.backend.model.Participation;
import es.unex.cume.tfg.backend.model.Platform;
import es.unex.cume.tfg.backend.model.Player;
import es.unex.cume.tfg.backend.model.Role;
import es.unex.cume.tfg.backend.repository.ChampionRepository;
import es.unex.cume.tfg.backend.repository.ParticipationRepository;
import es.unex.cume.tfg.backend.riot.dto.MatchDto;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * Default implementation of ParticipationService.
 */
@Service
public class ParticipationServiceImpl implements ParticipationService {

    private final ParticipationRepository participationRepository;
    private final ChampionRepository championRepository;
    private final PlayerSyncService playerSyncService;

    /**
     * Creates the participation service.
     *
     * @param participationRepository participation repository
     * @param championRepository champion repository
     * @param playerSyncService basic player synchronization service
     */
    public ParticipationServiceImpl(ParticipationRepository participationRepository,
                                    ChampionRepository championRepository,
                                    PlayerSyncService playerSyncService) {
        this.participationRepository = participationRepository;
        this.championRepository = championRepository;
        this.playerSyncService = playerSyncService;
    }

    /**
     * Finds all participations for a player by their PUUID.
     *
     * @param puuid the player's PUUID
     * @return the list of participations
     */
    @Override
    public List<Participation> findByPuuid(String puuid) {
        return participationRepository.findByPlayerPuuidOrderByGameStartAtDesc(puuid);
    }

    /**
     * Finds all participations for a match by its match ID.
     *
     * @param matchId the match ID
     * @return the list of participations
     */
    @Override
    public List<Participation> findByMatchId(String matchId) {
        return participationRepository.findByMatchMatchId(matchId);
    }

    /**
     * Creates and saves Participation entities from a MatchDto.
     * Links each participation to the saved Match, the Player and the Champion.
     *
     * @param matchDto the Riot match DTO
     * @param match the already-saved Match entity
     * @param platform the platform where the match was played
     */
    @Override
    public void saveParticipationsFromDto(MatchDto matchDto, Match match, Platform platform) {
        List<Participation> participations = new ArrayList<>();

        for (MatchDto.Participant p : matchDto.info().participants()) {
            Participation participation = new Participation();

            // Match
            participation.setMatch(match);

            // Player
            Player player = playerSyncService.syncBasicPlayer(p, platform);
            participation.setPlayer(player);

            // Champion
            Optional<Champion> optionalChampion = championRepository.findById(p.championId());
            if (optionalChampion.isEmpty()) {
                throw new ChampionCatalogException(p.championId());
            }

            participation.setChampion(optionalChampion.get());

            // Result
            participation.setTeamId(p.teamId());
            participation.setWin(p.win());

            // Stats
            participation.setKills(p.kills());
            participation.setDeaths(p.deaths());
            participation.setAssists(p.assists());

            // General information
            participation.setGameStartAt(Instant.ofEpochMilli(matchDto.info().gameStartTimestamp()));
            participation.setTeamPosition(toRole(p.teamPosition()));

            // Build
            Build build = dtoToBuild(p, participation);
            participation.setBuild(build);

            participations.add(participation);
        }

        participationRepository.saveAll(participations);
    }

    /**
     * Converts a Riot position into the role used by the application.
     *
     * @param teamPosition position returned by Riot
     * @return matching application role, or null when Riot provides no position
     */
    private Role toRole(String teamPosition) {
        if (teamPosition == null || teamPosition.isBlank()) {
            return null;
        }

        if ("UTILITY".equals(teamPosition)) {
            return Role.SUPPORT;
        }

        return Role.valueOf(teamPosition);
    }

    /**
     * Creates a build from Riot participant data.
     *
     * @param p Riot participant data
     * @param participation related participation
     * @return created build
     */
    private Build dtoToBuild(MatchDto.Participant p, Participation participation) {
        // Sorts main items so equal builds share the same representation
        List<Integer> items = getSortedItems(p);

        Build build = new Build();

        build.setItem0(items.get(0));
        build.setItem1(items.get(1));
        build.setItem2(items.get(2));
        build.setItem3(items.get(3));
        build.setItem4(items.get(4));
        build.setItem5(items.get(5));
        build.setItem6(p.item6());
        build.setRoleBoundItem(p.roleBoundItem());
        build.setSummoner1Id(p.summoner1Id());
        build.setSummoner2Id(p.summoner2Id());
        build.setParticipation(participation);
        return build;
    }

    /**
     * Gets the main build items in a consistent order.
     *
     * @param participant Riot participant data
     * @return sorted item identifiers
     */
    private List<Integer> getSortedItems(MatchDto.Participant participant) {
        List<Integer> items = new ArrayList<>();

        addItem(items, participant.item0());
        addItem(items, participant.item1());
        addItem(items, participant.item2());
        addItem(items, participant.item3());
        addItem(items, participant.item4());
        addItem(items, participant.item5());

        Collections.sort(items);

        // Fills the list with 0s until it has 6 elements
        while (items.size() < 6) {
            items.add(0);
        }

        return items;
    }

    /**
     * Adds a valid item to the build.
     *
     * @param items build items
     * @param item item identifier
     */
    private void addItem(List<Integer> items, Integer item) {
        if (item != null && item != 0) {
            items.add(item);
        }
    }
}
