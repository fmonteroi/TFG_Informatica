package es.unex.cume.tfg.backend.repository;

import es.unex.cume.tfg.backend.model.ChampionStats;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository for calculated champion statistics.
 */
public interface ChampionStatsRepository extends JpaRepository<ChampionStats, Integer> {
}
