package es.unex.cume.tfg.backend.service;

import es.unex.cume.tfg.backend.model.Champion;
import es.unex.cume.tfg.backend.model.Player;
import es.unex.cume.tfg.backend.model.PlayerStats;
import es.unex.cume.tfg.backend.repository.ParticipationRepository;
import es.unex.cume.tfg.backend.repository.PlayerStatsRepository;
import es.unex.cume.tfg.backend.repository.projection.PlayerChampionStatsAggregate;
import es.unex.cume.tfg.backend.repository.projection.PlayerStatsAggregate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Calculates and stores player statistics from match participations.
 */
@Service
public class PlayerStatsServiceImpl implements PlayerStatsService {

    private static final double PICK_RATE_WEIGHT = 0.4;
    private static final double WIN_RATE_WEIGHT = 0.6;

    private final PlayerStatsRepository playerStatsRepository;
    private final ParticipationRepository participationRepository;

    /**
     * Creates the player statistics service.
     *
     * @param playerStatsRepository player statistics repository
     * @param participationRepository participation repository
     */
    public PlayerStatsServiceImpl(PlayerStatsRepository playerStatsRepository, ParticipationRepository participationRepository) {
        this.playerStatsRepository = playerStatsRepository;
        this.participationRepository = participationRepository;
    }

    /**
     * Recalculates general statistics and the best champion for one player.
     *
     * @param player player to calculate
     * @return saved player statistics
     */
    @Override
    @Transactional
    public PlayerStats calculatePlayerStats(Player player) {
        // Gets the grouped totals without loading every participation
        PlayerStatsAggregate aggregate = participationRepository.aggregatePlayerStats(player.getPuuid());

        long games = valueOrZero(aggregate.gamesPlayed());
        long wins = valueOrZero(aggregate.wins());
        long kills = valueOrZero(aggregate.kills());
        long deaths = valueOrZero(aggregate.deaths());
        long assists = valueOrZero(aggregate.assists());

        // Reuses the shared-PUUID entity when statistics already exist
        Optional<PlayerStats> optionalStats = playerStatsRepository.findById(player.getPuuid());

        PlayerStats stats;
        if (optionalStats.isPresent()) {
            stats = optionalStats.get();
        } else {
            stats = new PlayerStats();
        }

        stats.setPlayer(player);
        stats.setGamesPlayed(games);
        stats.setWins(wins);
        stats.setLosses(games - wins);
        stats.setWinRate(calculateRate(wins, games));
        stats.setAverageKills(calculateAverage(kills, games));
        stats.setAverageDeaths(calculateAverage(deaths, games));
        stats.setAverageAssists(calculateAverage(assists, games));
        stats.setKda(calculateKda(kills, deaths, assists));

        // Gets per-champion totals for the weighted best champion score
        List<PlayerChampionStatsAggregate> championAggregates = participationRepository.aggregatePlayerStatsByChampion(player.getPuuid());

        stats.setBestChampion(findBestChampion(championAggregates, games));

        player.setStats(stats);

        return playerStatsRepository.save(stats);
    }

    /**
     * Finds statistics for one player.
     *
     * @param puuid player PUUID
     * @return player statistics when available
     */
    @Override
    public Optional<PlayerStats> findByPuuid(String puuid) {
        return playerStatsRepository.findById(puuid);
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
     * Calculates KDA while supporting games without deaths.
     *
     * @param kills total kills
     * @param deaths total deaths
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
     * Finds the champion with the best weighted score.
     *
     * @param aggregates grouped values for each champion
     * @param totalGames player's total games
     * @return best champion or null when no games exist
     */
    private Champion findBestChampion(List<PlayerChampionStatsAggregate> aggregates, long totalGames) {
        if (totalGames == 0 || aggregates.isEmpty()) {
            return null;
        }

        Champion bestChampion = null;
        double bestScore = Double.NEGATIVE_INFINITY;

        for (PlayerChampionStatsAggregate aggregate : aggregates) {
            double score = calculateChampionScore(aggregate, totalGames);

            // Replaces the current champion when the score is better
            if (score > bestScore) {
                bestChampion = aggregate.champion();
                bestScore = score;
            }
        }

        return bestChampion;
    }

    /**
     * Calculates a weighted pick rate and win rate score.
     *
     * @param aggregate grouped champion values
     * @param totalGames player's total games
     * @return champion score
     */
    private double calculateChampionScore(PlayerChampionStatsAggregate aggregate, long totalGames) {
        double pickRate = calculateRate(aggregate.gamesPlayed(), totalGames);
        double winRate = calculateRate(aggregate.wins(), aggregate.gamesPlayed());

        // Weighted score: 40% pick rate, 60% win rate
        return PICK_RATE_WEIGHT * pickRate + WIN_RATE_WEIGHT * winRate;
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
}
