package es.unex.cume.tfg.backend.service;

import es.unex.cume.tfg.backend.model.Platform;
import es.unex.cume.tfg.backend.model.Player;
import es.unex.cume.tfg.backend.model.RankedRank;

import java.util.List;

/**
 * Service that refreshes and gets player ranked results.
 */
public interface RankedRankService {

    /**
     * Replaces a player's ranked results with current Riot data.
     *
     * @param platform Riot platform
     * @param player player to refresh
     * @return saved ranked results
     */
    List<RankedRank> refreshRanks(Platform platform, Player player);

    /**
     * Finds a player's ranked results.
     *
     * @param puuid player PUUID
     * @return ranked results
     */
    List<RankedRank> findByPuuid(String puuid);
}
