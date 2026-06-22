package es.unex.cume.tfg.backend.repository;

import es.unex.cume.tfg.backend.model.Match;
import org.springframework.data.jpa.repository.JpaRepository;

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
}
