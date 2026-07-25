package es.unex.cume.tfg.backend.repository;

import es.unex.cume.tfg.backend.model.Champion;
import es.unex.cume.tfg.backend.repository.projection.ChampionStatsAggregate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
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

    @Query("""
        SELECT new es.unex.cume.tfg.backend.repository.projection.ChampionStatsAggregate(
            c,
            COUNT(p),
            SUM(CASE WHEN p.win = true THEN 1 ELSE 0 END),
            SUM(p.kills),
            SUM(p.deaths),
            SUM(p.assists)
        )
        FROM Champion c
        LEFT JOIN c.participations p
        GROUP BY c
        ORDER BY c.championId ASC
        """)
    List<ChampionStatsAggregate> aggregateChampionStats();
}
