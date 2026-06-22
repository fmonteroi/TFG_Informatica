package es.unex.cume.tfg.backend.repository;

import es.unex.cume.tfg.backend.model.Champion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Repository for Champion persistence and catalog lookups.
 */
public interface ChampionRepository extends JpaRepository<Champion, Integer> {
    /**
     * Finds a champion by its Riot champion ID.
     *
     * @param championId the Riot champion ID.
     * @return the champion if it exists.
     */
    Optional<Champion> findByChampionId(Integer championId);
}
