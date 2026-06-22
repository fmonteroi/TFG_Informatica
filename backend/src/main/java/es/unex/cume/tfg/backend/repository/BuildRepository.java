package es.unex.cume.tfg.backend.repository;

import es.unex.cume.tfg.backend.model.Build;
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
    List<Build> findRecentProBuildsByChampionId(@Param("championId") Integer championId,
                                                @Param("queueIds") List<Integer> queueIds,
                                                Pageable pageable);
}

