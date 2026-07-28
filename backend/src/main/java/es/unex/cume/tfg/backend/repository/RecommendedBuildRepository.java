package es.unex.cume.tfg.backend.repository;

import es.unex.cume.tfg.backend.model.RecommendedBuild;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Repository for champion recommended builds.
 */
public interface RecommendedBuildRepository extends JpaRepository<RecommendedBuild, Long> {
    /**
     * Gets all recommendations for one champion ordered by role usage.
     *
     * @param championId champion identifier
     * @return recommendations ordered from most to least played role
     */
    List<RecommendedBuild> findByChampionChampionIdOrderByRoleGamesDesc(Integer championId);
}
