package es.unex.cume.tfg.backend.controller;

import es.unex.cume.tfg.backend.dto.ChampionDto;
import es.unex.cume.tfg.backend.dto.ProBuildDto;
import es.unex.cume.tfg.backend.model.Champion;
import es.unex.cume.tfg.backend.service.BuildService;
import es.unex.cume.tfg.backend.service.ChampionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/champions")
public class ChampionController {

    private final ChampionService championService;
    private final BuildService buildService;

    public ChampionController(ChampionService championService,
                              BuildService buildService) {
        this.championService = championService;
        this.buildService = buildService;
    }

    /**
     * Finds a champion by its ID.
     * Call: GET /api/champions/{championId}
     *
     * @param championId
     * @return
     */
    @GetMapping("/{championId}")
    public ResponseEntity<ChampionDto> findChampion(@PathVariable Integer championId) {
        // Gets the champion
        Champion champion = championService.findChampion(championId);

        // Returns the champion
        return ResponseEntity.ok(ChampionDto.fromEntity(champion));
    }

    /** GET /api/champions - Obtener todos los campeones */

    /**
     * Finds all champions.
     * Call: GET /api/champions
     *
     * @return
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

    /**
     * Gets recent pro builds for a champion.
     * Call: GET /api/champions/{championId}/builds?count=10
     *
     * @param championId
     * @param count
     * @return
     */
    @GetMapping("/{championId}/builds")
    public ResponseEntity<List<ProBuildDto>> getRecentProBuilds(
            @PathVariable Integer championId,
            @RequestParam(defaultValue = "10") int count) {

        // Check if champion exists
        championService.findChampion(championId);

        // Gets recent pro builds for champion
        List<ProBuildDto> builds = buildService.findRecentProBuildsByChampionId(championId, count);

        // Returns builds
        return ResponseEntity.ok(builds);
    }

}

