package es.unex.cume.tfg.backend.controller;

import es.unex.cume.tfg.backend.dto.CurrentGameDto;
import es.unex.cume.tfg.backend.dto.PlayerWithParticipationsDto;
import es.unex.cume.tfg.backend.model.Participation;
import es.unex.cume.tfg.backend.model.Player;
import es.unex.cume.tfg.backend.model.Platform;
import es.unex.cume.tfg.backend.service.CurrentGameService;
import es.unex.cume.tfg.backend.service.ParticipationService;
import es.unex.cume.tfg.backend.service.PlayerService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST controller that exposes player search, refresh and current game endpoints.
 */
@RestController
@RequestMapping("/api/players")
public class PlayerController {

    private final PlayerService playerService;
    private final ParticipationService participationService;
    private final CurrentGameService currentGameService;

    public PlayerController(PlayerService playerService, ParticipationService participationService, CurrentGameService currentGameService) {
        this.playerService = playerService;
        this.participationService = participationService;
        this.currentGameService = currentGameService;
    }

    /**
     * Searches for a player and returns them with their participations.
     * Call: GET /api/players/search?platform=EUW1&gameName=Faker&tagLine=KR1
     *
     * @param platform
     * @param gameName
     * @param tagLine
     * @return the player and their participations.
     */
    @GetMapping("/search")
    public ResponseEntity<PlayerWithParticipationsDto> searchPlayer(@RequestParam Platform platform, @RequestParam String gameName, @RequestParam String tagLine) {
        // Searches player
        Player player = playerService.searchPlayer(platform, gameName, tagLine);

        // Gets participations
        List<Participation> participations = participationService.findByPuuid(player.getPuuid());

        // Returns player with participations
        return ResponseEntity.ok(PlayerWithParticipationsDto.from(player, participations));
    }

    /**
     * Refreshes player data from Riot and returns the updated player with their participations.
     * Call: POST /api/players/refresh?platform=EUW1&puuid=puuid123
     *
     * @param platform
     * @param puuid
     * @return the refreshed player and their participations.
     */
    @PostMapping("/refresh")
    public ResponseEntity<PlayerWithParticipationsDto> refreshPlayer(@RequestParam Platform platform, @RequestParam String puuid) {
        // Refreshes player data
        Player player = playerService.refreshPlayer(platform, puuid);

        List<Participation> participations = participationService.findByPuuid(player.getPuuid());

        return ResponseEntity.ok(PlayerWithParticipationsDto.from(player, participations));
    }

    /**
     * Gets the current game status of a player.
     * Call: GET /api/players/current-game?puuid=puuid123
     *
     * @param puuid the player's puuid
     * @return current game information for the frontend card
     */
    @GetMapping("/current-game")
    public ResponseEntity<CurrentGameDto> getCurrentGame(@RequestParam String puuid) {
        return ResponseEntity.ok(currentGameService.findCurrentGame(puuid));
    }

}
