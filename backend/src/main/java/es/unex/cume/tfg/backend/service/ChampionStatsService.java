package es.unex.cume.tfg.backend.service;

import es.unex.cume.tfg.backend.model.ChampionStats;

import java.util.List;
import java.util.Optional;

/**
 * Service that calculates and gets champion statistics.
 */
public interface ChampionStatsService {

    /**
     * Recalculates statistics for every champion.
     *
     * @return saved champion statistics
     */
    List<ChampionStats> calculateAllChampionStats();

    /**
     * Finds statistics for one champion.
     *
     * @param championId champion identifier
     * @return champion statistics when available
     */
    Optional<ChampionStats> findByChampionId(Integer championId);
}
