package es.unex.cume.tfg.backend.controller;

import es.unex.cume.tfg.backend.dto.MatchDetailsDto;
import es.unex.cume.tfg.backend.model.Match;
import es.unex.cume.tfg.backend.model.Participation;
import es.unex.cume.tfg.backend.model.Platform;
import es.unex.cume.tfg.backend.service.MatchService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST controller that exposes match detail endpoints.
 */
@RestController
@RequestMapping("/api/matches")
public class MatchController {

    private final MatchService matchService;

    public MatchController(MatchService matchService) {
        this.matchService = matchService;
    }

    /**
     * Finds the details of a match including all the participations.
     * Call: GET /api/matches/{matchId}
     *
     * @param matchId
     * @return the match details.
     */
    @GetMapping("/{matchId}")
    public ResponseEntity<MatchDetailsDto> findMatchDetails(@PathVariable String matchId) {
        return ResponseEntity.ok(matchService.findMatchDetails(matchId));
    }

}

