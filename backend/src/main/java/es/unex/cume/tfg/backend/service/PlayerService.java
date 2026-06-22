package es.unex.cume.tfg.backend.service;

import es.unex.cume.tfg.backend.model.Platform;
import es.unex.cume.tfg.backend.model.Player;

import java.util.Optional;

/**
 * Service that manages player lookup, refresh and professional synchronization.
 */
public interface PlayerService {

    /**
     * Finds a player by PUUID.
     *
     * @param puuid the player PUUID.
     * @return the player if it exists.
     */
    Optional<Player> findByPuuid(String puuid);

    /**
     * Checks whether a player exists by PUUID.
     *
     * @param puuid the player PUUID.
     * @return true if the player exists.
     */
    boolean existsByPuuid(String puuid);

    /**
     * Searches a Riot player and creates local data when needed.
     *
     * @param platform the Riot platform.
     * @param gameName the Riot game name.
     * @param tagLine the Riot tag line.
     * @return the local player.
     */
    Player searchPlayer(Platform platform, String gameName, String tagLine);

    /**
     * Refreshes profile and recent match data for an existing player.
     *
     * @param platform the Riot platform.
     * @param puuid the player PUUID.
     * @return the refreshed player.
     */
    Player refreshPlayer(Platform platform, String puuid);

    /**
     * Synchronizes basic local player data for a professional player.
     *
     * @param platform the Riot platform.
     * @param gameName the Riot game name.
     * @param tagLine the Riot tag line.
     * @param puuid the player PUUID.
     * @return the synchronized player.
     */
    Player syncPlayerForProfessional(Platform platform, String gameName, String tagLine, String puuid);

}
