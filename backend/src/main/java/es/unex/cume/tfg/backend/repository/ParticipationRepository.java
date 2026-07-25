package es.unex.cume.tfg.backend.repository;

import es.unex.cume.tfg.backend.model.Participation;
import es.unex.cume.tfg.backend.repository.projection.PlayerChampionStatsAggregate;
import es.unex.cume.tfg.backend.repository.projection.PlayerStatsAggregate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Repository for Participation persistence and history queries.
 */
public interface ParticipationRepository extends JpaRepository<Participation, Long> {
    /**
     * Finds a player's participations ordered from newest to oldest.
     *
     * @param puuid the player PUUID.
     * @return the player's participations.
     */
    List<Participation> findByPlayerPuuidOrderByGameStartAtDesc(String puuid);

    /**
     * Finds all participations for a match.
     *
     * @param matchId the Riot match ID.
     * @return the match participations.
     */
    List<Participation> findByMatchMatchId(String matchId);

    @Query("""
        SELECT new es.unex.cume.tfg.backend.repository.projection.PlayerStatsAggregate(
            COUNT(p),
            SUM(CASE WHEN p.win = true THEN 1 ELSE 0 END),
            SUM(p.kills),
            SUM(p.deaths),
            SUM(p.assists)
        )
        FROM Participation p
        WHERE p.player.puuid = :puuid
        """)
    PlayerStatsAggregate aggregatePlayerStats(@Param("puuid") String puuid);

    @Query("""
        SELECT new es.unex.cume.tfg.backend.repository.projection.PlayerChampionStatsAggregate(
            p.champion,
            COUNT(p),
            SUM(CASE WHEN p.win = true THEN 1 ELSE 0 END)
        )
        FROM Participation p
        WHERE p.player.puuid = :puuid
        GROUP BY p.champion
        ORDER BY COUNT(p) DESC, MIN(p.champion.championId) ASC
        """)
    List<PlayerChampionStatsAggregate> aggregatePlayerStatsByChampion(@Param("puuid") String puuid);

}
