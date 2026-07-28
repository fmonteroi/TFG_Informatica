package es.unex.cume.tfg.backend.repository;

import es.unex.cume.tfg.backend.model.Build;
import es.unex.cume.tfg.backend.model.Role;
import es.unex.cume.tfg.backend.repository.projection.RecommendedBuildAggregate;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

/**
 * Repository for Build persistence and pro build queries.
 */
public interface BuildRepository extends JpaRepository<Build, Long> {

    /**
     * Finds the build associated with a participation.
     *
     * @param participationId the participation ID.
     * @return the build if it exists.
     */
    Optional<Build> findByParticipationId(Long participationId);

    /**
     * Finds recent professional builds for a champion and role.
     *
     * @param championId champion identifier
     * @param role champion role
     * @param queueIds ranked queue identifiers
     * @param pageable pagination and limit information
     * @return recent professional builds
     */
    @Query("""
            SELECT b
            FROM Build b
            JOIN FETCH b.participation p
            JOIN FETCH p.match m
            JOIN FETCH p.champion c
            JOIN FETCH p.player pl
            JOIN FETCH pl.professional pr
            WHERE c.championId = :championId AND m.queueId IN :queueIds AND p.teamPosition = :role
            ORDER BY m.gameStartAt DESC
            """)
    List<Build> findRecentProBuildsByChampionIdAndRole(@Param("championId") Integer championId, @Param("role") Role role, @Param("queueIds") List<Integer> queueIds, Pageable pageable);

    /**
     * Groups current patch ranked builds by champion, role, items and summoner spells.
     *
     * @param queueIds ranked queue identifiers
     * @param patch game patch to include
     * @return grouped build results by champion and role
     */
    @Query("""
            SELECT new es.unex.cume.tfg.backend.repository.projection.RecommendedBuildAggregate(
                c, p.teamPosition, b.item0, b.item1, b.item2, b.item3, b.item4, b.item5, b.item6,
                CASE
                    WHEN p.teamPosition = es.unex.cume.tfg.backend.model.Role.BOTTOM
                    THEN b.roleBoundItem
                    ELSE NULL
                END,
                b.summoner1Id, b.summoner2Id,
                COUNT(b),
                SUM(CASE WHEN p.win = true THEN 1 ELSE 0 END)
            )
            FROM Build b
            JOIN b.participation p
            JOIN p.champion c
            JOIN p.match m
            WHERE m.queueId IN :queueIds
            AND m.gameVersion LIKE CONCAT(:patch, '.%')
            AND p.teamPosition IS NOT NULL
            AND b.item0 IS NOT NULL AND b.item0 <> 0
            AND b.item1 IS NOT NULL AND b.item1 <> 0
            AND b.item2 IS NOT NULL AND b.item2 <> 0
            AND b.item3 IS NOT NULL AND b.item3 <> 0
            AND b.item4 IS NOT NULL AND b.item4 <> 0
            AND b.item5 IS NOT NULL AND b.item5 <> 0
            GROUP BY c, p.teamPosition, b.item0, b.item1, b.item2, b.item3, b.item4, b.item5, b.item6,
            CASE
                WHEN p.teamPosition = es.unex.cume.tfg.backend.model.Role.BOTTOM
                THEN b.roleBoundItem
                ELSE NULL
            END,
            b.summoner1Id, b.summoner2Id
            ORDER BY c.championId ASC, p.teamPosition ASC
            """)
    List<RecommendedBuildAggregate> aggregateRankedBuilds(@Param("queueIds") List<Integer> queueIds, @Param("patch") String patch);

    /**
     * Finds a professional player's latest builds in the given queues.
     *
     * @param puuid professional player PUUID
     * @param queueIds ranked queue identifiers
     * @param pageable result limit and page settings
     * @return latest professional builds
     */
    @Query("""
            SELECT b
            FROM Build b
            JOIN FETCH b.participation p
            JOIN FETCH p.match m
            JOIN FETCH p.champion c
            JOIN p.player player
            JOIN player.professional professional
            WHERE professional.puuid = :puuid
            AND m.queueId IN :queueIds
            ORDER BY m.gameStartAt DESC
            """)
    List<Build> findRecentBuildsByProfessionalPuuid(@Param("puuid") String puuid, @Param("queueIds") List<Integer> queueIds, Pageable pageable);





}

