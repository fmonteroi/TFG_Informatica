package es.unex.cume.tfg.backend.config;

import es.unex.cume.tfg.backend.service.ChampionStatsService;
import es.unex.cume.tfg.backend.service.RecommendedBuildService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * Schedules champion statistics and recommended build calculations.
 */
@Component
public class ChampionStatsScheduler {

    private final ChampionStatsService championStatsService;
    private final RecommendedBuildService recommendedBuildService;

    /**
     * Creates the champion statistics scheduler.
     *
     * @param championStatsService service used to calculate champion statistics
     * @param recommendedBuildService service used to calculate recommended builds
     */
    public ChampionStatsScheduler(ChampionStatsService championStatsService, RecommendedBuildService recommendedBuildService) {
        this.championStatsService = championStatsService;
        this.recommendedBuildService = recommendedBuildService;
    }

    /**
     * Recalculates champion statistics and recommended builds.
     */
    @Scheduled(initialDelayString = "${app.scheduler.champion-stats.initial-delay-ms}", fixedDelayString = "${app.scheduler.champion-stats.fixed-delay-ms}")
    public void calculateChampionStats() {
        // Recalculates statistics and recommendations from stored data
        System.out.println("Starting champion data calculation");

        championStatsService.calculateAllChampionStats();
        recommendedBuildService.calculateAllRecommendedBuilds();

        System.out.println("Champion data calculation completed");
    }
}
