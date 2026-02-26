package es.unex.cume.tfg.backend.controller;

import es.unex.cume.tfg.backend.model.Champion;
import es.unex.cume.tfg.backend.model.Participation;
import es.unex.cume.tfg.backend.service.ChampionServiceImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/champions")
public class ChampionController {

    private final ChampionServiceImpl championServiceImpl;

    public ChampionController(ChampionServiceImpl championServiceImpl) {
        this.championServiceImpl = championServiceImpl;
    }

    /** GET /api/champions - Obtener todos los campeones */
    @GetMapping
    public ResponseEntity<List<Champion>> getAllChampions() {
        // TODO: implementar
        return ResponseEntity.ok().build();
    }

    /** GET /api/champions/{championId} - Consultar campeon por ID */
    @GetMapping("/{championId}")
    public ResponseEntity<Champion> getChampion(@PathVariable Integer championId) {
        // TODO: implementar
        return ResponseEntity.ok().build();
    }

    /** GET /api/champions/{championId}/builds?count=10 - Ver builds recientes de un campeon */
    @GetMapping("/{championId}/builds")
    public ResponseEntity<List<Participation>> getRecentBuilds(
            @PathVariable Integer championId,
            @RequestParam(defaultValue = "10") int count) {
        // TODO: implementar
        return ResponseEntity.ok().build();
    }
}

