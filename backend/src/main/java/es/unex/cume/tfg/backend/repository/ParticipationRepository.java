package es.unex.cume.tfg.backend.repository;

import es.unex.cume.tfg.backend.model.Participation;
import org.springframework.data.jpa.repository.JpaRepository;

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
}
