package es.unex.cume.tfg.backend.repository;

import es.unex.cume.tfg.backend.model.Champion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ChampionRepository extends JpaRepository<Champion, Integer> {
    Optional<Champion> findByChampionId(Integer championId);
}
