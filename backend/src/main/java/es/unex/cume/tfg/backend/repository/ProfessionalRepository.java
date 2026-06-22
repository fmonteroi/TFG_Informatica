package es.unex.cume.tfg.backend.repository;

import es.unex.cume.tfg.backend.model.Professional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * Repository for Professional persistence and eager player queries.
 */
public interface ProfessionalRepository extends JpaRepository<Professional, String> {
    /**
     * Finds all professionals with their linked players loaded.
     *
     * @return the professionals with player data.
     */
    @Query("""
        SELECT p
        FROM Professional p
        JOIN FETCH p.player
    """)
    List<Professional> findAllWithPlayer();
}
