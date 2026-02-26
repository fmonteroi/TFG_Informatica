package es.unex.cume.tfg.backend.repository;

import es.unex.cume.tfg.backend.model.Professional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProfessionalRepository extends JpaRepository<Professional, String> {
}
