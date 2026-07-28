package es.unex.cume.tfg.backend.repository;

import es.unex.cume.tfg.backend.model.Professional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

/**
 * Repository for Professional persistence and eager player queries.
 */
public interface ProfessionalRepository extends JpaRepository<Professional, String> {

    /**
     * Finds all professional players ordered by professional name.
     *
     * @return ordered professional players
     */
    List<Professional> findAllByOrderByProNameAsc();

    /**
     * Finds all professionals with their linked players loaded.
     *
     * @return the professionals with player data.
     */
    @Query("""
            SELECT professional
            FROM Professional professional
            JOIN FETCH professional.player player
            ORDER BY
                CASE WHEN player.lastSyncAt IS NULL THEN 0 ELSE 1 END,
                player.lastSyncAt ASC
            """)
    List<Professional> findAllWithPlayerOrderByLastSyncAt();

    /**
     * Finds a professional player with their player account loaded.
     *
     * @param puuid professional player PUUID
     * @return professional player when available
     */
    @Query("""
            SELECT professional
            FROM Professional professional
            JOIN FETCH professional.player
            WHERE professional.puuid = :puuid
            """)
    Optional<Professional> findByPuuidWithPlayer(@Param("puuid") String puuid);
}
