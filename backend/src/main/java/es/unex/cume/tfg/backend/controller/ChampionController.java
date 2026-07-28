package es.unex.cume.tfg.backend.controller;

import es.unex.cume.tfg.backend.dto.*;
import es.unex.cume.tfg.backend.model.Champion;
import es.unex.cume.tfg.backend.model.ChampionStats;
import es.unex.cume.tfg.backend.model.RecommendedBuild;
import es.unex.cume.tfg.backend.model.Tier;
import es.unex.cume.tfg.backend.service.BuildService;
import es.unex.cume.tfg.backend.service.ChampionService;
import es.unex.cume.tfg.backend.service.ChampionStatsService;
import es.unex.cume.tfg.backend.service.RecommendedBuildService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
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

    /**
     * Creates the champion controller.
     *
     * @param championService         service used to get champions
     * @param buildService            service used to get professional builds
     * @param championStatsService    service used to get champion statistics
     * @param recommendedBuildService service used to get recommended builds
     */
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
        List<ChampionDto> champions = championService.findAllChampions()
                .stream()
                .map(ChampionDto::fromEntity)
                .toList();

        return ResponseEntity.ok(champions);
    }

    /**
     * Gets a champion with statistics and build information grouped by role.
     *
     * @param championId champion identifier
     * @param buildCount maximum number of professional builds per role
     * @return complete champion details
     */
    @GetMapping("/{championId}")
    public ResponseEntity<ChampionDetailsDto> findChampionDetails(@PathVariable Integer championId, @RequestParam(defaultValue = "10") int buildCount) {
        Champion champion = championService.findChampion(championId);

        ChampionStatsDto statsDto = null;
        Tier tier = null;
        Optional<ChampionStats> optionalStats = championStatsService.findByChampionId(championId);

        if (optionalStats.isPresent()) {
            ChampionStats stats = optionalStats.get();
            statsDto = ChampionStatsDto.fromEntity(stats);
            tier = stats.getTier();
        }

        // Gets recommendations ordered by the most played role
        List<RecommendedBuild> recommendedBuilds = recommendedBuildService.findByChampionId(championId);

        List<ChampionRoleBuildsDto> roleBuilds = new ArrayList<>();

        // Groups each recommendation with recent professional builds for its role
        for (RecommendedBuild recommendedBuild : recommendedBuilds) {
            List<ChampionProBuildDto> recentBuilds = buildService.findRecentProBuildsByChampionIdAndRole(championId, recommendedBuild.getRole(), buildCount);
            ChampionRoleBuildsDto roleBuildsDto = new ChampionRoleBuildsDto(recommendedBuild.getRole(), RecommendedBuildDto.fromEntity(recommendedBuild), recentBuilds);

            roleBuilds.add(roleBuildsDto);
        }

        ChampionDetailsDto details = new ChampionDetailsDto(champion.getChampionId(), champion.getChampionName(), tier, statsDto, roleBuilds);

        return ResponseEntity.ok(details);
    }


}

