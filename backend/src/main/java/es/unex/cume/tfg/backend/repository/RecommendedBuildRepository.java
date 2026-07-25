package es.unex.cume.tfg.backend.repository;

import es.unex.cume.tfg.backend.model.RecommendedBuild;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RecommendedBuildRepository extends JpaRepository<RecommendedBuild, Integer> {
}
