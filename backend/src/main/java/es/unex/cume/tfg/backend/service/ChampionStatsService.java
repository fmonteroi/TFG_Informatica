package es.unex.cume.tfg.backend.service;

import es.unex.cume.tfg.backend.model.ChampionStats;

import java.util.List;
import java.util.Optional;

public interface ChampionStatsService {
    List<ChampionStats> calculateAllChampionStats();
    Optional<ChampionStats> findByChampionId(Integer championId);
}
