package es.unex.cume.tfg.backend.controller;

import es.unex.cume.tfg.backend.dto.*;
import es.unex.cume.tfg.backend.model.Champion;
import es.unex.cume.tfg.backend.model.ChampionStats;
import es.unex.cume.tfg.backend.model.RecommendedBuild;
import es.unex.cume.tfg.backend.service.BuildService;
import es.unex.cume.tfg.backend.service.ChampionService;
import es.unex.cume.tfg.backend.service.ChampionStatsService;
import es.unex.cume.tfg.backend.service.RecommendedBuildService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

/**
 * REST controller that exposes champion catalog and build endpoints.
 */
@RestController
@RequestMapping("/api/champions")
public class ChampionController {

    private final ChampionService championService;
    private final BuildService buildService;
    private final ChampionStatsService championStatsService;
    private final RecommendedBuildService recommendedBuildService;

    public ChampionController(ChampionService championService, BuildService buildService, ChampionStatsService championStatsService, RecommendedBuildService recommendedBuildService) {
        this.championService = championService;
        this.buildService = buildService;
        this.championStatsService = championStatsService;
        this.recommendedBuildService = recommendedBuildService;
    }


    /**
     * Finds all champions.
     * Call: GET /api/champions
     *
     * @return the champion catalog.
     */
    @GetMapping
    public ResponseEntity<List<ChampionDto>> findAllChampions() {
        // Gets all champions
        List<ChampionDto> champions = championService.findAllChampions()
                .stream()
                .map(ChampionDto::fromEntity)
                .toList();

        // Returns all champions
        return ResponseEntity.ok(champions);
    }

    @GetMapping("/{championId}")
    public ResponseEntity<ChampionDetailsDto> findChampionDetails(@PathVariable Integer championId, @RequestParam(defaultValue = "10") int buildCount) {
        Champion champion = championService.findChampion(championId);

        ChampionStatsDto statsDto = null;
        Optional<ChampionStats> optionalStats = championStatsService.findByChampionId(championId);

        if (optionalStats.isPresent()) {
            statsDto = ChampionStatsDto.fromEntity(optionalStats.get());
        }

        RecommendedBuildDto recommendedBuildDto = null;
        Optional<RecommendedBuild> optionalBuild = recommendedBuildService.findByChampionId(championId);

        if (optionalBuild.isPresent()) {
            recommendedBuildDto = RecommendedBuildDto.fromEntity(optionalBuild.get());
        }

        List<ChampionProBuildDto> recentBuilds = buildService.findRecentProBuildsByChampionId(championId, buildCount);

        ChampionDetailsDto details = new ChampionDetailsDto(champion.getChampionId(), champion.getChampionName(), statsDto, recommendedBuildDto, recentBuilds);

        return ResponseEntity.ok(details);
    }


}

