package es.unex.cume.tfg.backend.controller;

import es.unex.cume.tfg.backend.model.Match;
import es.unex.cume.tfg.backend.model.Participation;
import es.unex.cume.tfg.backend.model.Platform;
import es.unex.cume.tfg.backend.service.MatchServiceImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/matches")
public class MatchController {

    private final MatchServiceImpl matchServiceImpl;

    public MatchController(MatchServiceImpl matchServiceImpl) {
        this.matchServiceImpl = matchServiceImpl;
    }

    /** GET /api/matches/history/{puuid} - Ver historial de partidas de un jugador */
    @GetMapping("/history/{puuid}")
    public ResponseEntity<List<Match>> getMatchHistory(@PathVariable String puuid) {
        // TODO: implementar
        return ResponseEntity.ok().build();
    }

    /** GET /api/matches/{matchId} - Ver detalles de una partida */
    @GetMapping("/{matchId}")
    public ResponseEntity<Match> getMatchDetails(@PathVariable String matchId) {
        // TODO: implementar
        return ResponseEntity.ok().build();
    }

    /** GET /api/matches/{matchId}/participations - Ver participaciones de una partida */
    @GetMapping("/{matchId}/participations")
    public ResponseEntity<List<Participation>> getParticipations(@PathVariable String matchId) {
        // TODO: implementar
        return ResponseEntity.ok().build();
    }

    /** POST /api/matches/sync?platform=EUW1&puuid=xxx&count=5 - Sincronizar partidas recientes desde Riot */
    @PostMapping("/sync")
    public ResponseEntity<List<Match>> syncRecentMatches(
            @RequestParam Platform platform,
            @RequestParam String puuid,
            @RequestParam(defaultValue = "5") int count) {
        // TODO: implementar
        return ResponseEntity.ok().build();
    }
}

