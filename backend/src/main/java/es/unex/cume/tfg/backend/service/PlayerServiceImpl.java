package es.unex.cume.tfg.backend.service;

import es.unex.cume.tfg.backend.model.Player;
import es.unex.cume.tfg.backend.model.Platform;
import es.unex.cume.tfg.backend.repository.PlayerRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PlayerServiceImpl implements PlayerService{

    private final PlayerRepository playerRepository;
    private final RiotFetchService riotFetchService;

    public PlayerServiceImpl(PlayerRepository playerRepository, RiotFetchService riotFetchService) {
        this.playerRepository = playerRepository;
        this.riotFetchService = riotFetchService;
    }

    /**
     * Finds a player by their PUUID.
     *
     * @param puuid
     * @return
     */
    public Optional<Player> findByPuuid(String puuid) {
        return playerRepository.findByPuuid(puuid);
    }

    /**
     * Finds if a player already exists in the database by their PUUID.
     *
     * @param puuid
     * @return
     */
    public boolean existsByPuuid(String puuid) {
        return playerRepository.existsByPuuid(puuid);
    }

    /**
     * Looks for a player by their Riot game name and tag line. If it exists, returns it.
     * If not, creates a new one.
     *
     * @param platform
     * @param gameName
     * @param tagLine
     * @return
     */
    public Player savePlayer(Platform platform, String gameName, String tagLine) {
        // 1 - Get PUUID from Riot's API
        String puuid = riotFetchService.fetchPuuid(platform, gameName, tagLine);

        // 2 - If already exists, return the existing player
        Optional<Player> existingPlayer = playerRepository.findByPuuid(puuid);
        if(existingPlayer.isPresent()){
            return existingPlayer.get();
        }

        // 3 - If not, create a new player, save it and return it
        Player newPlayer = new Player();
        newPlayer.setPuuid(puuid);
        newPlayer.setPlatformRegion(platform);
        newPlayer.setRiotGameName(gameName);
        newPlayer.setRiotTagLine(tagLine);

        return playerRepository.save(newPlayer);
    }


}

