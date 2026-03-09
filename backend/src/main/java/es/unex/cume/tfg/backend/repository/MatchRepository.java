package es.unex.cume.tfg.backend.repository;

import es.unex.cume.tfg.backend.model.Match;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface MatchRepository extends JpaRepository<Match, String> {
    Optional<Match> findByMatchId(String matchId);

    boolean existsByMatchId(String matchId);

    @Query("""
                SELECT DISTINCT m
                FROM Match m
                JOIN m.participations p
                WHERE p.player.puuid = :puuid
                ORDER BY m.gameStartAt DESC
            """)
    List<Match> findByParticipantPuuid(@Param("puuid") String puuid, Pageable pageable);

}
