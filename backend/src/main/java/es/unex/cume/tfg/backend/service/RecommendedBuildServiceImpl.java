package es.unex.cume.tfg.backend.service;

import es.unex.cume.tfg.backend.model.Match;
import es.unex.cume.tfg.backend.model.RecommendedBuild;
import es.unex.cume.tfg.backend.repository.BuildRepository;
import es.unex.cume.tfg.backend.repository.MatchRepository;
import es.unex.cume.tfg.backend.repository.RecommendedBuildRepository;
import es.unex.cume.tfg.backend.repository.projection.RecommendedBuildAggregate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class RecommendedBuildServiceImpl implements RecommendedBuildService {

    private static final List<Integer> RANKED_QUEUE_IDS = List.of(420, 440); // Ranked Solo/Duo and Ranked Flex
    private static final double PICK_RATE_WEIGHT = 0.4;
    private static final double WIN_RATE_WEIGHT = 0.6;

    private final BuildRepository buildRepository;
    private final RecommendedBuildRepository recommendedBuildRepository;
    private final MatchRepository matchRepository;

    public RecommendedBuildServiceImpl(BuildRepository buildRepository, RecommendedBuildRepository recommendedBuildRepository, MatchRepository matchRepository) {
        this.buildRepository = buildRepository;
        this.recommendedBuildRepository = recommendedBuildRepository;
        this.matchRepository = matchRepository;
    }

    @Override
    @Transactional
    public List<RecommendedBuild> calculateAllRecommendedBuilds() {

        Optional<Match> optionalLatestMatch = matchRepository.findFirstByQueueIdInOrderByGameStartAtDesc(RANKED_QUEUE_IDS);
        if (optionalLatestMatch.isEmpty()) {
            return List.of();
        }
        Match latestMatch = optionalLatestMatch.get();
        String currentPatch = extractPatch(latestMatch.getGameVersion());

        if (currentPatch == null) {
            return List.of();
        }

        List<RecommendedBuildAggregate> aggregates = buildRepository.aggregateRankedBuilds(RANKED_QUEUE_IDS, currentPatch);

        Map<Integer, Long> totalGamesByChampion = calculateTotalGamesByChampion(aggregates);

        Map<Integer, RecommendedBuildAggregate> bestByChampion = findBestBuilds(aggregates, totalGamesByChampion);

        Map<Integer, RecommendedBuild> existingBuilds = findExistingBuilds();

        List<RecommendedBuild> recommendedBuilds = new ArrayList<>();

        for (RecommendedBuildAggregate aggregate : bestByChampion.values()) {

            Integer championId = aggregate.champion().getChampionId();

            RecommendedBuild recommendedBuild = existingBuilds.get(championId);

            if (recommendedBuild == null) {
                recommendedBuild = new RecommendedBuild();
                recommendedBuild.setChampion(aggregate.champion());
            }
            copyBuild(aggregate, recommendedBuild);
            aggregate.champion().setRecommendedBuild(recommendedBuild);

            recommendedBuilds.add(recommendedBuild);
        }

        return recommendedBuildRepository.saveAll(recommendedBuilds);
    }

    @Override
    public Optional<RecommendedBuild> findByChampionId(Integer championId) {
        return recommendedBuildRepository.findById(championId);
    }

    private Map<Integer, Long> calculateTotalGamesByChampion(List<RecommendedBuildAggregate> aggregates) {
        Map<Integer, Long> totals = new HashMap<>();

        for (RecommendedBuildAggregate aggregate : aggregates) {
            Integer championId = aggregate.champion().getChampionId();

            Long currentTotal = totals.get(championId);

            if (currentTotal == null) {
                currentTotal = 0L;
            }

            totals.put(championId, currentTotal + aggregate.gamesPlayed());
        }

        return totals;
    }

    private Map<Integer, RecommendedBuildAggregate> findBestBuilds(List<RecommendedBuildAggregate> aggregates, Map<Integer, Long> totalGamesByChampion) {
        Map<Integer, RecommendedBuildAggregate> bestBuilds = new HashMap<>();

        Map<Integer, Double> bestScores = new HashMap<>();

        for (RecommendedBuildAggregate aggregate : aggregates) {
            Integer championId = aggregate.champion().getChampionId();

            long totalGames = totalGamesByChampion.get(championId);
            double pickRate = calculateRate(aggregate.gamesPlayed(), totalGames);
            double winRate = calculateRate(aggregate.wins(), aggregate.gamesPlayed());

            double score = PICK_RATE_WEIGHT * pickRate + WIN_RATE_WEIGHT * winRate;

            Double bestScore = bestScores.get(championId);

            if (bestScore == null || score > bestScore) {
                bestScores.put(championId, score);
                bestBuilds.put(championId, aggregate);
            } else if (score == bestScore) {
                RecommendedBuildAggregate currentBest = bestBuilds.get(championId);

                if (aggregate.gamesPlayed() > currentBest.gamesPlayed()) {
                    bestBuilds.put(championId, aggregate);
                    bestScores.put(championId, score);
                }
            }
        }

        return bestBuilds;
    }

    private double calculateRate(long value, long total) {
        if (total == 0) {
            return 0.0;
        }

        return (double) value / total;
    }

    private Map<Integer, RecommendedBuild> findExistingBuilds() {
        List<RecommendedBuild> existingBuilds = recommendedBuildRepository.findAll();

        Map<Integer, RecommendedBuild> buildsByChampion = new HashMap<>();

        for (RecommendedBuild build : existingBuilds) {
            buildsByChampion.put(build.getChampionId(), build);
        }

        return buildsByChampion;
    }

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

    private String extractPatch(String gameVersion) {
        if (gameVersion == null || gameVersion.isBlank()) {
            return null;
        }

        String[] parts = gameVersion.split("\\.");

        if (parts.length < 2) {
            return null;
        }

        return parts[0] + "." + parts[1];
    }

}
