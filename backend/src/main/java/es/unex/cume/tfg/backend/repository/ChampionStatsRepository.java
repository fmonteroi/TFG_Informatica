package es.unex.cume.tfg.backend.repository;

import es.unex.cume.tfg.backend.model.ChampionStats;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChampionStatsRepository extends JpaRepository<ChampionStats, Integer> {
}
