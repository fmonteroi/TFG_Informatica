package es.unex.cume.tfg.backend.service;

import es.unex.cume.tfg.backend.model.Champion;
import es.unex.cume.tfg.backend.model.ChampionStats;
import es.unex.cume.tfg.backend.model.Tier;
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

/**
 * Calculates and stores statistics for every champion.
 */
@Service
public class ChampionStatsServiceImpl implements ChampionStatsService {

    private static final double PICK_RATE_WEIGHT = 0.4;
    private static final double WIN_RATE_WEIGHT = 0.6;

    private final ChampionRepository championRepository;
    private final ChampionStatsRepository championStatsRepository;
    private final RankedDataService rankedDataService;

    /**
     * Creates the champion statistics service.
     *
     * @param championRepository      champion repository
     * @param championStatsRepository champion statistics repository
     * @param rankedDataService       ranked data service
     */
    public ChampionStatsServiceImpl(ChampionRepository championRepository, ChampionStatsRepository championStatsRepository, RankedDataService rankedDataService) {
        this.championRepository = championRepository;
        this.championStatsRepository = championStatsRepository;
        this.rankedDataService = rankedDataService;
    }

    /**
     * Recalculates statistics and tiers for every champion.
     *
     * @return saved champion statistics
     */
    @Override
    @Transactional
    public List<ChampionStats> calculateAllChampionStats() {

        // Finds the latest ranked patch stored in the database
        Optional<String> optionalCurrentPatch = rankedDataService.findLatestPatch();

        if (optionalCurrentPatch.isEmpty()) {
            return List.of();
        }

        String currentPatch = optionalCurrentPatch.get();
        List<Integer> rankedQueueIds = rankedDataService.getQueueIds();

        // Gets champion values from ranked matches in the current patch
        List<ChampionStatsAggregate> aggregates = championRepository.aggregateChampionStats(rankedQueueIds, currentPatch);

        // Finds how many games the most played champion has
        long maxChampionGames = findMaxChampionGames(aggregates);

        // Reuses existing entities because they share their ID with champions
        Map<Integer, ChampionStats> existingStats = findExistingStats();

        List<ChampionStats> calculatedStats = new ArrayList<>();
        List<ChampionScore> championScores = new ArrayList<>();

        for (ChampionStatsAggregate aggregate : aggregates) {
            Champion champion = aggregate.champion();

            ChampionStats stats =
                    existingStats.get(champion.getChampionId());

            if (stats == null) {
                stats = new ChampionStats();
            }

            // Converts nullable query results to primitive values
            long games = valueOrZero(aggregate.gamesPlayed());
            long wins = valueOrZero(aggregate.wins());
            long kills = valueOrZero(aggregate.kills());
            long deaths = valueOrZero(aggregate.deaths());
            long assists = valueOrZero(aggregate.assists());

            // Calculates the champion statistics
            stats.setChampion(champion);
            stats.setGamesPlayed(games);
            stats.setWins(wins);
            stats.setLosses(games - wins);
            stats.setWinRate(calculateRate(wins, games));
            stats.setAverageKills(calculateAverage(kills, games));
            stats.setAverageDeaths(calculateAverage(deaths, games));
            stats.setAverageAssists(calculateAverage(assists, games));
            stats.setKda(calculateKda(kills, deaths, assists));

            // Only classifies champions with data in the current patch
            if (games == 0) {
                stats.setTier(Tier.C);
            } else {
                double score = calculateChampionScore(stats, maxChampionGames);
                championScores.add(new ChampionScore(stats, score));
            }

            champion.setStats(stats);
            calculatedStats.add(stats);
        }

        // Assigns tiers after every valid champion score is available
        assignTiers(championScores);

        return championStatsRepository.saveAll(calculatedStats);
    }

    /**
     * Finds statistics for one champion.
     *
     * @param championId champion identifier
     * @return champion statistics when available
     */
    @Override
    public Optional<ChampionStats> findByChampionId(Integer championId) {
        return championStatsRepository.findById(championId);
    }

    /**
     * Maps stored statistics by champion identifier.
     *
     * @return stored statistics by champion identifier
     */
    private Map<Integer, ChampionStats> findExistingStats() {
        List<ChampionStats> stats = championStatsRepository.findAll();

        Map<Integer, ChampionStats> statsByChampionId = new HashMap<>();

        for (ChampionStats championStats : stats) {
            statsByChampionId.put(championStats.getChampionId(), championStats);
        }

        return statsByChampionId;
    }

    /**
     * Converts a nullable database total to a primitive value.
     *
     * @param value nullable database total
     * @return value or zero
     */
    private long valueOrZero(Long value) {
        if (value == null) {
            return 0L;
        }

        return value;
    }

    /**
     * Calculates a percentage.
     *
     * @param value matching amount
     * @param total total amount
     * @return calculated percentage
     */
    private double calculateRate(long value, long total) {
        if (total == 0) {
            return 0.0;
        }

        return value * 100.0 / total;
    }

    /**
     * Calculates an average.
     *
     * @param value accumulated value
     * @param total number of games
     * @return calculated average
     */
    private double calculateAverage(long value, long total) {
        if (total == 0) {
            return 0.0;
        }

        return (double) value / total;
    }

    /**
     * Calculates KDA while supporting games without deaths.
     *
     * @param kills   total kills
     * @param deaths  total deaths
     * @param assists total assists
     * @return calculated KDA
     */
    private double calculateKda(long kills, long deaths, long assists) {
        if (deaths == 0) {
            return kills + assists;
        }

        return (double) (kills + assists) / deaths;
    }

    /**
     * Calculates the weighted score used for champion classification.
     *
     * @param stats            calculated champion statistics
     * @param maxChampionGames games played with the most played champion
     * @return weighted champion score
     */
    private double calculateChampionScore(ChampionStats stats, long maxChampionGames) {
        double normalizedPickRate = calculateRatio(stats.getGamesPlayed(), maxChampionGames);
        double winRate = stats.getWinRate() / 100.0;

        return PICK_RATE_WEIGHT * normalizedPickRate + WIN_RATE_WEIGHT * winRate;
    }

    /**
     * Orders champions by score and assigns their tier.
     *
     * @param championScores calculated champion scores
     */
    private void assignTiers(List<ChampionScore> championScores) {
        List<ChampionScore> rankedChampions = new ArrayList<>();

        for (ChampionScore championScore : championScores) {
            ChampionStats stats = championScore.stats();

            // Removes any tier left from a previous calculation
            stats.setTier(Tier.C);

            rankedChampions.add(championScore);
        }

        // Orders champions from highest to lowest score
        rankedChampions.sort((first, second) -> Double.compare(second.score(), first.score()));

        for (int position = 0; position < rankedChampions.size(); position++) {
            Tier tier = calculateTier(position, rankedChampions.size());
            rankedChampions.get(position).stats().setTier(tier);
        }
    }

    /**
     * Gets the tier matching a champion's ranking position.
     *
     * @param position       champion position starting at zero
     * @param totalChampions total classified champions
     * @return matching tier
     */
    private Tier calculateTier(int position, int totalChampions) {
        double percentile = (position + 1) * 100.0 / totalChampions;

        if (percentile <= 10.0) {
            return Tier.S;
        } else if (percentile <= 30.0) {
            return Tier.A;
        } else if (percentile <= 50.0) {
            return Tier.B;
        } else if (percentile <= 70.0) {
            return Tier.C;
        } else if (percentile <= 90.0) {
            return Tier.D;
        }

        return Tier.E;
    }

    /**
     * Finds the highest number of games played with one champion.
     *
     * @param aggregates grouped values for each champion
     * @return highest champion game count
     */
    private long findMaxChampionGames(List<ChampionStatsAggregate> aggregates) {
        long maxChampionGames = 0;

        for (ChampionStatsAggregate aggregate : aggregates) {
            long gamesPlayed = valueOrZero(aggregate.gamesPlayed());

            if (gamesPlayed > maxChampionGames) {
                maxChampionGames = gamesPlayed;
            }
        }

        return maxChampionGames;
    }

    /**
     * Calculates a ratio between zero and one.
     *
     * @param value matching amount
     * @param total total amount
     * @return calculated ratio
     */
    private double calculateRatio(long value, long total) {
        if (total == 0) {
            return 0.0;
        }

        return (double) value / total;
    }

    /**
     * Holds a champion and its temporary classification score.
     *
     * @param stats champion statistics
     * @param score calculated score
     */
    private record ChampionScore(
            ChampionStats stats,
            double score
    ) {
    }


}
