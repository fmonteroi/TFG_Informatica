package es.unex.cume.tfg.backend.repository;

import es.unex.cume.tfg.backend.model.PlayerStats;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PlayerStatsRepository extends JpaRepository<PlayerStats, String> {
}
