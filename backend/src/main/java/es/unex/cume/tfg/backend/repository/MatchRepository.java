package es.unex.cume.tfg.backend.repository;

import es.unex.cume.tfg.backend.model.Match;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MatchRepository extends JpaRepository<Match, String> {
    Optional<Match> findByMatchId(String matchId);
    boolean existsByMatchId(String matchId);
}
