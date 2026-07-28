package es.unex.cume.tfg.backend.repository;

import es.unex.cume.tfg.backend.model.Champion;
import es.unex.cume.tfg.backend.repository.projection.ChampionStatsAggregate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

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

    /**
     * Groups current patch ranked participations by champion.
     *
     * @param queueIds ranked queue identifiers
     * @param patch game patch to include
     * @return grouped values used to calculate champion statistics
     */
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
        LEFT JOIN c.participations p ON 
                p.match.queueId IN :queueIds
                AND p.match.gameVersion LIKE CONCAT(:patch, '.%')
        GROUP BY c
        ORDER BY c.championId ASC
        """)
    List<ChampionStatsAggregate> aggregateChampionStats(@Param("queueIds") List<Integer> queueIds, @Param("patch") String patch);

    /**
     * Finds every champion with its calculated statistics.
     *
     * @return champions with their statistics loaded
     */
    @Query("""
        SELECT champion
        FROM Champion champion
        LEFT JOIN FETCH champion.stats
        ORDER BY champion.championName
        """)
    List<Champion> findAllWithStats();
}
