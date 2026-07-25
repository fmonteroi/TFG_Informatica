package es.unex.cume.tfg.backend.service;

import es.unex.cume.tfg.backend.model.RecommendedBuild;

import java.util.List;
import java.util.Optional;

public interface RecommendedBuildService {

    List<RecommendedBuild> calculateAllRecommendedBuilds();

    Optional<RecommendedBuild> findByChampionId(Integer championId);
}
