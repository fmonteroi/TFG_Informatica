package es.unex.cume.tfg.backend.service;

import es.unex.cume.tfg.backend.model.Champion;
import es.unex.cume.tfg.backend.model.ChampionStats;
import es.unex.cume.tfg.backend.repository.ChampionRepository;
import es.unex.cume.tfg.backend.repository.ChampionStatsRepository;
import es.unex.cume.tfg.backend.repository.projection.ChampionStatsAggregate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class ChampionStatsServiceImpl implements ChampionStatsService {

    private final ChampionRepository championRepository;
    private final ChampionStatsRepository championStatsRepository;

    public ChampionStatsServiceImpl(ChampionRepository championRepository, ChampionStatsRepository championStatsRepository) {
        this.championRepository = championRepository;
        this.championStatsRepository = championStatsRepository;
    }

    @Override
    @Transactional
    public List<ChampionStats> calculateAllChampionStats() {
        List<ChampionStatsAggregate> aggregates = championRepository.aggregateChampionStats();

        // Find existing stats to update them instead of creating new ones
        // As ChampionStats uses MapsId with Champion, we need to find existing stats to avoid creating new ones for the same Id
        Map<Integer, ChampionStats> existingStats = findExistingStats();

        List<ChampionStats> calculatedStats = new ArrayList<>();

        for (ChampionStatsAggregate aggregate : aggregates) {
            Champion champion = aggregate.champion();

            ChampionStats stats = existingStats.get(champion.getChampionId());

            if (stats == null) {
                stats = new ChampionStats();
            }

            long games = valueOrZero(aggregate.gamesPlayed());
            long wins = valueOrZero(aggregate.wins());
            long kills = valueOrZero(aggregate.kills());
            long deaths = valueOrZero(aggregate.deaths());
            long assists = valueOrZero(aggregate.assists());

            stats.setChampion(champion);
            stats.setGamesPlayed(games);
            stats.setWins(wins);
            stats.setLosses(games - wins);
            stats.setWinRate(calculateRate(wins, games));
            stats.setAverageKills(calculateAverage(kills, games));
            stats.setAverageDeaths(calculateAverage(deaths, games));
            stats.setAverageAssists(calculateAverage(assists, games));
            stats.setKda(calculateKda(kills, deaths, assists));

            champion.setStats(stats);
            calculatedStats.add(stats);
        }

        return championStatsRepository.saveAll(calculatedStats);
    }

    @Override
    public Optional<ChampionStats> findByChampionId(Integer championId) {
        return championStatsRepository.findById(championId);
    }

    private Map<Integer, ChampionStats> findExistingStats() {
        List<ChampionStats> stats = championStatsRepository.findAll();

        Map<Integer, ChampionStats> statsByChampionId = new HashMap<>();

        for (ChampionStats championStats : stats) {
            statsByChampionId.put(championStats.getChampionId(), championStats);
        }

        return statsByChampionId;
    }

    private long valueOrZero(Long value) {
        if (value == null) {
            return 0L;
        }

        return value;
    }

    private double calculateRate(long value, long total) {
        if (total == 0) {
            return 0.0;
        }

        return value * 100.0 / total;
    }

    private double calculateAverage(long value, long total) {
        if (total == 0) {
            return 0.0;
        }

        return (double) value / total;
    }

    private double calculateKda(long kills, long deaths, long assists) {
        if (deaths == 0) {
            return kills + assists;
        }

        return (double) (kills + assists) / deaths;
    }
}