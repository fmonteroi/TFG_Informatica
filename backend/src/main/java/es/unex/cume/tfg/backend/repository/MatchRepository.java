package es.unex.cume.tfg.backend.repository;

import es.unex.cume.tfg.backend.model.Match;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

/**
 * Repository for Match persistence and match lookups.
 */
public interface MatchRepository extends JpaRepository<Match, String> {
    /**
     * Finds a match by its Riot match ID.
     *
     * @param matchId the Riot match ID.
     * @return the match if it exists.
     */
    Optional<Match> findByMatchId(String matchId);

    /**
     * Checks whether a match already exists by its Riot match ID.
     *
     * @param matchId the Riot match ID.
     * @return true if the match exists.
     */
    boolean existsByMatchId(String matchId);

    /**
     * Finds the latest stored match from the given queues.
     *
     * @param queueIds queue identifiers
     * @return latest matching match when available
     */
    Optional<Match> findFirstByQueueIdInOrderByGameStartAtDesc(List<Integer> queueIds);

    /**
     * Counts stored ranked matches from the given patch.
     *
     * @param queueIds ranked queue identifiers
     * @param patch game patch to include
     * @return number of matching ranked matches
     */
    @Query("""
    SELECT COUNT(m)
    FROM Match m
    WHERE m.queueId IN :queueIds
    AND m.gameVersion LIKE CONCAT(:patch, '.%')
    """)
    long countRankedMatchesByPatch(@Param("queueIds") List<Integer> queueIds, @Param("patch") String patch);
}
