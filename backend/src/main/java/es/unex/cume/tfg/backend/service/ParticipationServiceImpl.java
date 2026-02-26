package es.unex.cume.tfg.backend.service;

import es.unex.cume.tfg.backend.model.Champion;
import es.unex.cume.tfg.backend.model.Match;
import es.unex.cume.tfg.backend.model.Participation;
import es.unex.cume.tfg.backend.model.Player;
import es.unex.cume.tfg.backend.repository.ChampionRepository;
import es.unex.cume.tfg.backend.repository.ParticipationRepository;
import es.unex.cume.tfg.backend.repository.PlayerRepository;
import es.unex.cume.tfg.backend.riot.dto.RiotMatchDto;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ParticipationServiceImpl implements ParticipationService {

    private final ParticipationRepository participationRepository;
    private final PlayerRepository playerRepository;
    private final ChampionRepository championRepository;

    public ParticipationServiceImpl(ParticipationRepository participationRepository,
                                    PlayerRepository playerRepository,
                                    ChampionRepository championRepository) {
        this.participationRepository = participationRepository;
        this.playerRepository = playerRepository;
        this.championRepository = championRepository;
    }

    /**
     * Finds all participations for a player by their PUUID.
     *
     * @param puuid the player's PUUID
     * @return the list of participations
     */
    @Override
    public List<Participation> findByPuuid(String puuid) {
        return participationRepository.findByPlayerPuuid(puuid);
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
     * Creates and saves Participation entities from a RiotMatchDto.
     * Links each participation to the saved Match, the Player and the Champion.
     *
     * @param riotMatchDto the Riot match DTO
     * @param match the already-saved Match entity
     * @return the list of saved participations
     */
    @Override
    public List<Participation> saveParticipationsFromDto(RiotMatchDto riotMatchDto, Match match) {
        List<Participation> participations = new ArrayList<>();

        for (RiotMatchDto.Participant p : riotMatchDto.info().participants()) {
            Participation participation = new Participation();

            // Match
            participation.setMatch(match);

            // Player (link if exists in DB)
            Optional<Player> player = playerRepository.findByPuuid(p.puuid());
            if (player.isPresent()) {
                participation.setPlayer(player.get());
            }

            // Champion (link if exists in DB)
            Optional<Champion> champion = championRepository.findById(p.championId());
            if (champion.isPresent()) {
                participation.setChampion(champion.get());
            }

            // Result
            participation.setTeamId(p.teamId());
            participation.setWin(p.win());

            // Stats
            participation.setKills(p.kills());
            participation.setDeaths(p.deaths());
            participation.setAssists(p.assists());

            // General information
            participation.setGameStartAt(Instant.ofEpochMilli(riotMatchDto.info().gameStartTimestamp()));
            participation.setTeamPosition(p.teamPosition());
            participation.setSummoner1Id(p.summoner1Id());
            participation.setSummoner2Id(p.summoner2Id());

            // Items
            participation.setItem0(p.item0());
            participation.setItem1(p.item1());
            participation.setItem3(p.item3());
            participation.setItem4(p.item4());
            participation.setItem5(p.item5());
            participation.setItem6(p.item6());

            participations.add(participation);
        }

        return participationRepository.saveAll(participations);
    }
}

