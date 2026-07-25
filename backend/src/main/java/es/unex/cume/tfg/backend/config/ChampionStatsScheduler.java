package es.unex.cume.tfg.backend.config;

import es.unex.cume.tfg.backend.service.ChampionStatsService;
import es.unex.cume.tfg.backend.service.RecommendedBuildService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class ChampionStatsScheduler {

    private final ChampionStatsService championStatsService;
    private final RecommendedBuildService recommendedBuildService;

    public ChampionStatsScheduler(ChampionStatsService championStatsService, RecommendedBuildService recommendedBuildService) {
        this.championStatsService = championStatsService;
        this.recommendedBuildService = recommendedBuildService;
    }

    @Scheduled(initialDelayString = "${app.scheduler.champion-stats.initial-delay-ms}", fixedDelayString = "${app.scheduler.champion-stats.fixed-delay-ms}")
    public void calculateChampionStats() {
        System.out.println("Starting champion data calculation");

        championStatsService.calculateAllChampionStats();
        recommendedBuildService.calculateAllRecommendedBuilds();

        System.out.println("Champion data calculation completed");
    }
}
