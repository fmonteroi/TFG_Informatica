package es.unex.cume.tfg.backend.repository;

import es.unex.cume.tfg.backend.model.Build;
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
     * Finds recent professional builds for a champion in the given queues.
     *
     * @param championId the champion ID.
     * @param queueIds the queue IDs to include.
     * @param pageable pagination and limit information.
     * @return the recent professional builds.
     */
    @Query("""
            SELECT b
            FROM Build b
            JOIN FETCH b.participation p
            JOIN FETCH p.match m
            JOIN FETCH p.champion c
            JOIN FETCH p.player pl
            JOIN FETCH pl.professional pr
            WHERE c.championId = :championId AND m.queueId IN :queueIds
            ORDER BY m.gameStartAt DESC
            """)
    List<Build> findRecentProBuildsByChampionId(@Param("championId") Integer championId, @Param("queueIds") List<Integer> queueIds, Pageable pageable);

    @Query("""
            SELECT new es.unex.cume.tfg.backend.repository.projection.RecommendedBuildAggregate(
                c, b.item0, b.item1, b.item2, b.item3, b.item4, b.item5, b.item6, b.roleBoundItem, b.summoner1Id, b.summoner2Id,
                COUNT(b),
                SUM(CASE WHEN p.win = true THEN 1 ELSE 0 END)
            )
            FROM Build b
            JOIN b.participation p
            JOIN p.champion c
            JOIN p.match m
            WHERE m.queueId IN :queueIds
            AND m.gameVersion LIKE CONCAT(:patch, '.%')
            GROUP BY c,b.item0, b.item1, b.item2, b.item3, b.item4, b.item5, b.item6, b.roleBoundItem, b.summoner1Id, b.summoner2Id
            ORDER BY c.championId ASC
            """)
    List<RecommendedBuildAggregate> aggregateRankedBuilds(@Param("queueIds") List<Integer> queueIds, @Param("patch") String patch);

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

