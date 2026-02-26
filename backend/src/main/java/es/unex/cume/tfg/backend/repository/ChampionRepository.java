package es.unex.cume.tfg.backend.repository;

import es.unex.cume.tfg.backend.model.Champion;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChampionRepository extends JpaRepository<Champion, Integer> {
}
