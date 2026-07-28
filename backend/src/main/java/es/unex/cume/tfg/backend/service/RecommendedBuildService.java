package es.unex.cume.tfg.backend.service;

import es.unex.cume.tfg.backend.model.RecommendedBuild;

import java.util.List;


/**
 * Service that calculates and gets champion build recommendations.
 */
public interface RecommendedBuildService {

    /**
     * Recalculates recommendations from current patch ranked data.
     *
     * @return saved build recommendations
     */
    List<RecommendedBuild> calculateAllRecommendedBuilds();

    /**
     * Finds all recommendations for one champion.
     *
     * @param championId champion identifier
     * @return recommendations grouped by role
     */
    List<RecommendedBuild> findByChampionId(Integer championId);
}
