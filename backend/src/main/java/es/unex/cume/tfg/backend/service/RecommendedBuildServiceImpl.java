package es.unex.cume.tfg.backend.service;

import es.unex.cume.tfg.backend.loader.ItemJsonLoader;
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
    private final ItemJsonLoader itemJsonLoader;

    /**
     * Creates the recommended build service.
     *
     * @param buildRepository            build repository
     * @param participationRepository    participation repository
     * @param recommendedBuildRepository recommendation repository
     * @param rankedDataService          ranked data service
     * @param itemJsonLoader             item catalog loader
     */
    public RecommendedBuildServiceImpl(BuildRepository buildRepository, ParticipationRepository participationRepository,
                                       RecommendedBuildRepository recommendedBuildRepository, RankedDataService rankedDataService, ItemJsonLoader itemJsonLoader) {
        this.buildRepository = buildRepository;
        this.participationRepository = participationRepository;
        this.recommendedBuildRepository = recommendedBuildRepository;
        this.rankedDataService = rankedDataService;
        this.itemJsonLoader = itemJsonLoader;
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

        // Groups builds and their match results
        List<RecommendedBuildAggregate> buildAggregates = buildRepository.aggregateRankedBuilds(rankedQueueIds, currentPatch);

        // Removes builds containing components or incomplete items
        Set<Integer> completedItemIds = itemJsonLoader.loadCompletedItemIds();
        List<RecommendedBuildAggregate> completedBuildAggregates = filterCompletedBuilds(buildAggregates, completedItemIds);

        // Gets the most played build for each champion and role
        Map<ChampionRoleKey, Long> maxBuildGamesByChampionAndRole = getMaxBuildGamesByChampionAndRole(completedBuildAggregates);

        // Selects one weighted recommendation for each champion and role
        Map<ChampionRoleKey, RecommendedBuildAggregate> bestBuilds = findBestBuilds(completedBuildAggregates, gamesByChampionAndRole, maxBuildGamesByChampionAndRole);

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
     * Maps the most played build for each champion and role.
     *
     * @param aggregates grouped build results
     * @return maximum build games by champion and role
     */
    private Map<ChampionRoleKey, Long> getMaxBuildGamesByChampionAndRole(List<RecommendedBuildAggregate> aggregates) {
        Map<ChampionRoleKey, Long> maxBuildGames = new HashMap<>();

        for (RecommendedBuildAggregate aggregate : aggregates) {
            Integer championId = aggregate.champion().getChampionId();

            ChampionRoleKey key = new ChampionRoleKey(championId, aggregate.role());

            Long currentMax = maxBuildGames.get(key);

            if (currentMax == null || aggregate.gamesPlayed() > currentMax) {
                maxBuildGames.put(key, aggregate.gamesPlayed());
            }
        }

        return maxBuildGames;
    }

    /**
     * Finds the highest scoring build for each champion and role.
     *
     * @param aggregates                     grouped build results
     * @param gamesByChampionAndRole         games played by each champion and role
     * @param maxBuildGamesByChampionAndRole most played build games by champion and role
     * @return best grouped build by champion and role
     */
    private Map<ChampionRoleKey, RecommendedBuildAggregate> findBestBuilds(List<RecommendedBuildAggregate> aggregates,
                                                                           Map<ChampionRoleKey, Long> gamesByChampionAndRole, Map<ChampionRoleKey, Long> maxBuildGamesByChampionAndRole) {

        Map<ChampionRoleKey, RecommendedBuildAggregate> bestBuilds = new HashMap<>();
        Map<ChampionRoleKey, Double> bestScores = new HashMap<>();

        for (RecommendedBuildAggregate aggregate : aggregates) {
            Integer championId = aggregate.champion().getChampionId();
            ChampionRoleKey key = new ChampionRoleKey(championId, aggregate.role());

            Long totalGames = gamesByChampionAndRole.get(key);
            Long maxBuildGames = maxBuildGamesByChampionAndRole.get(key);

            if (totalGames == null || totalGames < MIN_GAMES_PLAYED || maxBuildGames == null) {
                continue;
            }

            double normalizedPickRate = calculateRatio(aggregate.gamesPlayed(), maxBuildGames);
            double winRate = calculateRatio(aggregate.wins(), aggregate.gamesPlayed());

            // Combines normalized pick rate and win rate
            double score = PICK_RATE_WEIGHT * normalizedPickRate + WIN_RATE_WEIGHT * winRate;

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
     * Keeps builds containing only completed main items.
     *
     * @param aggregates       grouped build results
     * @param completedItemIds completed item identifiers
     * @return builds containing only completed items
     */
    private List<RecommendedBuildAggregate> filterCompletedBuilds(List<RecommendedBuildAggregate> aggregates, Set<Integer> completedItemIds) {
        List<RecommendedBuildAggregate> completedBuilds = new ArrayList<>();

        for (RecommendedBuildAggregate aggregate : aggregates) {
            if (hasOnlyCompletedItems(aggregate, completedItemIds)) {
                completedBuilds.add(aggregate);
            }
        }

        return completedBuilds;
    }

    /**
     * Checks whether all six main inventory items are completed.
     *
     * @param aggregate        grouped build result
     * @param completedItemIds completed item identifiers
     * @return true when every main item is completed
     */
    private boolean hasOnlyCompletedItems(RecommendedBuildAggregate aggregate, Set<Integer> completedItemIds) {
        if (!completedItemIds.contains(aggregate.item0())) {
            return false;
        }

        if (!completedItemIds.contains(aggregate.item1())) {
            return false;
        }

        if (!completedItemIds.contains(aggregate.item2())) {
            return false;
        }

        if (!completedItemIds.contains(aggregate.item3())) {
            return false;
        }

        if (!completedItemIds.contains(aggregate.item4())) {
            return false;
        }

        return completedItemIds.contains(aggregate.item5());
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
