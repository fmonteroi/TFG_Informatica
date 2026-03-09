package es.unex.cume.tfg.backend.repository;

import es.unex.cume.tfg.backend.model.Professional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ProfessionalRepository extends JpaRepository<Professional, String> {
    @Query("""
        SELECT p
        FROM Professional p
        JOIN FETCH p.player
    """)
    List<Professional> findAllWithPlayer();
}
