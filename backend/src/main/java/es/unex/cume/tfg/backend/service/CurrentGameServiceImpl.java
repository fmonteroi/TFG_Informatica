package es.unex.cume.tfg.backend.service;

import es.unex.cume.tfg.backend.dto.CurrentGameDto;
import es.unex.cume.tfg.backend.exception.ChampionCatalogException;
import es.unex.cume.tfg.backend.exception.ChampionNotFoundException;
import es.unex.cume.tfg.backend.exception.PlayerNotFoundException;
import es.unex.cume.tfg.backend.model.Champion;
import es.unex.cume.tfg.backend.model.Player;
import es.unex.cume.tfg.backend.model.Platform;
import es.unex.cume.tfg.backend.riot.dto.CurrentGameInfoDto;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Default implementation of CurrentGameService.
 */
@Service
public class CurrentGameServiceImpl implements CurrentGameService {

    private final RiotFetchService riotFetchService;
    private final ChampionService championService;
    private final PlayerService playerService;

    /**
     * Creates the current game service.
     *
     * @param riotFetchService Riot data service
     * @param championService champion service
     * @param playerService player service
     */
    public CurrentGameServiceImpl(RiotFetchService riotFetchService,
                                  ChampionService championService,
                                  PlayerService playerService) {
        this.riotFetchService = riotFetchService;
        this.championService = championService;
        this.playerService = playerService;
    }

    /**
     * Finds the current game info for a player.
     *
     * @param puuid player PUUID
     * @return the current game status DTO.
     */
    @Override
    public CurrentGameDto findCurrentGame(String puuid) {
        // Finds player by PUUID
        Optional<Player> optionalPlayer = playerService.findByPuuid(puuid);

        // If player is not found, throws exception
        if (optionalPlayer.isEmpty()) {
            throw new PlayerNotFoundException(puuid);
        }

        // Gets current game data from Riot
        Player player = optionalPlayer.get();
        Platform platform = player.getPlatform();
        Optional<CurrentGameInfoDto> optionalCurrentGame = riotFetchService.fetchCurrentGame(platform, puuid);

        // If player is not in game, returns false in the DTO
        if (optionalCurrentGame.isEmpty()) {
            return CurrentGameDto.notInGame();
        }

        // Otherwise, finds participant info and champion info to return the current game info in the DTO
        CurrentGameInfoDto currentGame = optionalCurrentGame.get();
        CurrentGameInfoDto.CurrentGameParticipant participant = findParticipantByPuuid(currentGame, puuid);

        // If participant cannot be identified, treats it as hiddenGame state
        if (participant == null) {
            return CurrentGameDto.hiddenGame();
        }

        // Finds champion info and resolves queue name
        Champion champion;
        try {
            champion = championService.findChampion(participant.championId());
        } catch (ChampionNotFoundException ex) {
            throw new ChampionCatalogException(participant.championId());
        }

        String queueName = resolveQueueName(currentGame.gameQueueConfigId().intValue());

        // Returns current game info in the DTO
        return CurrentGameDto.inGame(
                currentGame.gameQueueConfigId().intValue(),
                queueName,
                currentGame.gameLength(),
                champion.getChampionId(),
                champion.getChampionName()
        );
    }

    /**
     * Finds the participant with the given PUUID in the current game info.
     *
     * @param currentGame current game data
     * @param puuid player PUUID
     * @return the matching participant, or null.
     */
    private CurrentGameInfoDto.CurrentGameParticipant findParticipantByPuuid(CurrentGameInfoDto currentGame, String puuid) {
        for (CurrentGameInfoDto.CurrentGameParticipant participant : currentGame.participants()) {
            if (participant.puuid() != null && participant.puuid().equals(puuid)) {
                return participant;
            }
        }

        return null;
    }


    /**
     * Resolves the queue name from the queue ID.
     *
     * @param queueId Riot queue identifier
     * @return the readable queue name.
     */
    private String resolveQueueName(Integer queueId) {
        if (queueId == null) {
            return "Unknown";
        }

        if (queueId == 420) {
            return "Ranked Solo/Duo";
        }

        if (queueId == 440) {
            return "Ranked Flex";
        }

        if (queueId == 450) {
            return "ARAM";
        }

        if (queueId == 400) {
            return "Normal Draft";
        }

        if (queueId == 430) {
            return "Normal Blind";
        }

        return "Special Mode";
    }
}
