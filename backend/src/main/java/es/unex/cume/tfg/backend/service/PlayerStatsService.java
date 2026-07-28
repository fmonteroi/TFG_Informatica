package es.unex.cume.tfg.backend.service;

import es.unex.cume.tfg.backend.model.Player;
import es.unex.cume.tfg.backend.model.PlayerStats;

import java.util.Optional;

/**
 * Service that calculates and gets player statistics.
 */
public interface PlayerStatsService {

    /**
     * Recalculates statistics for one player.
     *
     * @param player player to calculate
     * @return saved player statistics
     */
    PlayerStats calculatePlayerStats(Player player);

    /**
     * Finds statistics for one player.
     *
     * @param puuid player PUUID
     * @return player statistics when available
     */
    Optional<PlayerStats> findByPuuid(String puuid);
}
