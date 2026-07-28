package es.unex.cume.tfg.backend.repository;

import es.unex.cume.tfg.backend.model.Participation;
import es.unex.cume.tfg.backend.repository.projection.ChampionRoleGamesAggregate;
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

    /**
     * Groups a player's results for general statistics.
     *
     * @param puuid player PUUID
     * @return grouped player statistics
     */
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

    /**
     * Groups a player's results by champion.
     *
     * @param puuid player PUUID
     * @return grouped statistics for each played champion
     */
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

    /**
     * Counts current-patch ranked games by champion and role.
     *
     * @param queueIds ranked queue identifiers
     * @param patch game patch to include
     * @return games played by each champion and role
     */
    @Query("""
        SELECT new es.unex.cume.tfg.backend.repository.projection.ChampionRoleGamesAggregate(p.champion, p.teamPosition,COUNT(p))
        FROM Participation p
        JOIN p.match m
        WHERE m.queueId IN :queueIds
        AND m.gameVersion LIKE CONCAT(:patch, '.%')
        AND p.teamPosition IS NOT NULL
        GROUP BY p.champion, p.teamPosition
        """)
    List<ChampionRoleGamesAggregate> aggregateGamesByChampionAndRole(@Param("queueIds") List<Integer> queueIds, @Param("patch") String patch);

}
