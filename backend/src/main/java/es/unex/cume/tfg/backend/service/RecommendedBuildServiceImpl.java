package es.unex.cume.tfg.backend.service;

import es.unex.cume.tfg.backend.model.RecommendedBuild;
import es.unex.cume.tfg.backend.model.Role;
import es.unex.cume.tfg.backend.repository.BuildRepository;
import es.unex.cume.tfg.backend.repository.ParticipationRepository;
import es.unex.cume.tfg.backend.repository.RecommendedBuildRepository;
import es.unex.cume.tfg.backend.repository.projection.ChampionRoleGamesAggregate;
import es.unex.cume.tfg.backend.repository.projection.RecommendedBuildAggregate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * Calculates champion build recommendations from ranked match data.
 */
@Service
public class RecommendedBuildServiceImpl implements RecommendedBuildService {

    private static final long MIN_GAMES_PLAYED = 10;
    private static final double PICK_RATE_WEIGHT = 0.4;
    private static final double WIN_RATE_WEIGHT = 0.6;

    private final BuildRepository buildRepository;
    private final ParticipationRepository participationRepository;
    private final RecommendedBuildRepository recommendedBuildRepository;
    private final RankedDataService rankedDataService;

    /**
     * Creates the recommended build service.
     *
     * @param buildRepository            build repository
     * @param participationRepository    participation repository
     * @param recommendedBuildRepository recommendation repository
     * @param rankedDataService          ranked data service
     */
    public RecommendedBuildServiceImpl(BuildRepository buildRepository, ParticipationRepository participationRepository, RecommendedBuildRepository recommendedBuildRepository, RankedDataService rankedDataService) {
        this.buildRepository = buildRepository;
        this.participationRepository = participationRepository;
        this.recommendedBuildRepository = recommendedBuildRepository;
        this.rankedDataService = rankedDataService;
    }

    /**
     * Recalculates the best build for each champion and role on the current patch.
     *
     * @return saved build recommendations
     */
    @Override
    @Transactional
    public List<RecommendedBuild> calculateAllRecommendedBuilds() {

        // Uses the latest ranked match to find the current data patch
        Optional<String> optionalCurrentPatch = rankedDataService.findLatestPatch();

        // If no ranked matches are found, no recommendations can be calculated
        if (optionalCurrentPatch.isEmpty()) {
            return List.of();
        }

        String currentPatch = optionalCurrentPatch.get();
        List<Integer> rankedQueueIds = rankedDataService.getQueueIds();

        // Counts all ranked games played by each champion and role
        List<ChampionRoleGamesAggregate> roleGames = participationRepository.aggregateGamesByChampionAndRole(rankedQueueIds, currentPatch);

        Map<ChampionRoleKey, Long> gamesByChampionAndRole = getGamesByChampionAndRole(roleGames);

        // Groups complete builds and their match results
        List<RecommendedBuildAggregate> buildAggregates = buildRepository.aggregateRankedBuilds(rankedQueueIds, currentPatch);

        // Selects one weighted recommendation for each champion and role
        Map<ChampionRoleKey, RecommendedBuildAggregate> bestBuilds = findBestBuilds(buildAggregates, gamesByChampionAndRole);

        // Reuses current recommendations and keeps old ones for later removal
        Map<ChampionRoleKey, RecommendedBuild> existingBuilds = findExistingBuilds();
        List<RecommendedBuild> currentRecommendations = new ArrayList<>();

        // Updates or creates recommendations for each champion and role
        for (RecommendedBuildAggregate aggregate : bestBuilds.values()) {
            Integer championId = aggregate.champion().getChampionId();

            ChampionRoleKey key = new ChampionRoleKey(championId, aggregate.role());

            // Removes and reuses the existing recommendation
            RecommendedBuild recommendedBuild = existingBuilds.remove(key);

            if (recommendedBuild == null) {
                recommendedBuild = new RecommendedBuild();
            }

            recommendedBuild.setChampion(aggregate.champion());
            recommendedBuild.setRole(aggregate.role());
            recommendedBuild.setRoleGames(gamesByChampionAndRole.get(key));

            copyBuild(aggregate, recommendedBuild);

            currentRecommendations.add(recommendedBuild);
        }

        // Removes recommendations that were not calculated for the current patch
        recommendedBuildRepository.deleteAll(existingBuilds.values());

        return recommendedBuildRepository.saveAll(currentRecommendations);
    }

    /**
     * Finds all recommendations for one champion.
     *
     * @param championId champion identifier
     * @return recommendations grouped by role
     */
    @Override
    public List<RecommendedBuild> findByChampionId(Integer championId) {
        return recommendedBuildRepository.findByChampionChampionIdOrderByRoleGamesDesc(championId);
    }

    /**
     * Maps the number of games played by each champion and role.
     *
     * @param aggregates grouped champion role games
     * @return games mapped by champion and role
     */
    private Map<ChampionRoleKey, Long> getGamesByChampionAndRole(List<ChampionRoleGamesAggregate> aggregates) {
        Map<ChampionRoleKey, Long> games = new HashMap<>();

        for (ChampionRoleGamesAggregate aggregate : aggregates) {
            Integer championId = aggregate.champion().getChampionId();
            ChampionRoleKey key = new ChampionRoleKey(championId, aggregate.role());

            games.put(key, aggregate.gamesPlayed());
        }

        return games;
    }

    /**
     * Finds the highest scoring build for each champion and role.
     *
     * @param aggregates             grouped build results
     * @param gamesByChampionAndRole games played by each champion and role
     * @return best grouped build by champion and role
     */
    private Map<ChampionRoleKey, RecommendedBuildAggregate> findBestBuilds(List<RecommendedBuildAggregate> aggregates, Map<ChampionRoleKey, Long> gamesByChampionAndRole) {
        Map<ChampionRoleKey, RecommendedBuildAggregate> bestBuilds = new HashMap<>();
        Map<ChampionRoleKey, Double> bestScores = new HashMap<>();

        for (RecommendedBuildAggregate aggregate : aggregates) {
            Integer championId = aggregate.champion().getChampionId();
            ChampionRoleKey key = new ChampionRoleKey(championId, aggregate.role());

            Long totalGames = gamesByChampionAndRole.get(key);

            if (totalGames == null || totalGames < MIN_GAMES_PLAYED) {
                continue;
            }

            double pickRate = calculateRate(aggregate.gamesPlayed(), totalGames);

            double winRate = calculateRate(aggregate.wins(), aggregate.gamesPlayed());

            // Combines pick rate and win rate using configurable weights
            double score = PICK_RATE_WEIGHT * pickRate + WIN_RATE_WEIGHT * winRate;

            Double bestScore = bestScores.get(key);

            if (bestScore == null || score > bestScore) {
                bestScores.put(key, score);
                bestBuilds.put(key, aggregate);
            } else if (score == bestScore) {
                RecommendedBuildAggregate currentBest = bestBuilds.get(key);

                if (aggregate.gamesPlayed() > currentBest.gamesPlayed()) {
                    bestScores.put(key, score);
                    bestBuilds.put(key, aggregate);
                }
            }
        }

        return bestBuilds;
    }

    /**
     * Calculates a ratio.
     *
     * @param value matching amount
     * @param total total amount
     * @return calculated ratio
     */
    private double calculateRate(long value, long total) {
        if (total == 0) {
            return 0.0;
        }

        return (double) value / total;
    }

    /**
     * Maps stored recommendations by champion and role.
     *
     * @return stored recommendations by champion and role
     */
    private Map<ChampionRoleKey, RecommendedBuild> findExistingBuilds() {
        List<RecommendedBuild> existingBuilds = recommendedBuildRepository.findAll();

        Map<ChampionRoleKey, RecommendedBuild> buildsByChampionAndRole = new HashMap<>();

        for (RecommendedBuild build : existingBuilds) {
            Integer championId = build.getChampion().getChampionId();
            ChampionRoleKey key = new ChampionRoleKey(championId, build.getRole());

            buildsByChampionAndRole.put(key, build);
        }

        return buildsByChampionAndRole;
    }

    /**
     * Copies build fields from a grouped result into an entity.
     *
     * @param original grouped build result
     * @param copy     recommendation entity to update
     */
    private void copyBuild(RecommendedBuildAggregate original, RecommendedBuild copy) {
        copy.setItem0(original.item0());
        copy.setItem1(original.item1());
        copy.setItem2(original.item2());
        copy.setItem3(original.item3());
        copy.setItem4(original.item4());
        copy.setItem5(original.item5());
        copy.setItem6(original.item6());
        copy.setRoleBoundItem(original.roleBoundItem());
        copy.setSummoner1Id(original.summoner1Id());
        copy.setSummoner2Id(original.summoner2Id());
    }

    /**
     * Identifies one champion and role combination.
     *
     * @param championId champion identifier
     * @param role       champion role
     */
    private record ChampionRoleKey(
            Integer championId,
            Role role
    ) {
    }

}
