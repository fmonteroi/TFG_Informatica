package es.unex.cume.tfg.backend.controller;

import es.unex.cume.tfg.backend.model.Player;
import es.unex.cume.tfg.backend.model.Platform;
import es.unex.cume.tfg.backend.service.PlayerServiceImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/players")
public class PlayerController {

    private final PlayerServiceImpl playerServiceImpl;

    public PlayerController(PlayerServiceImpl playerServiceImpl) {
        this.playerServiceImpl = playerServiceImpl;
    }

    /** GET /api/players/{puuid} - Consultar perfil de jugador por PUUID */
    @GetMapping("/{puuid}")
    public ResponseEntity<Player> getPlayerByPuuid(@PathVariable String puuid) {
        // TODO: implementar
        return ResponseEntity.ok().build();
    }

    /** GET /api/players/search?platform=EUW1&gameName=Faker&tagLine=KR1 - Buscar jugador en Riot y sincronizar */
    @GetMapping("/search")
    public ResponseEntity<Player> searchPlayer(
            @RequestParam Platform platform,
            @RequestParam String gameName,
            @RequestParam String tagLine) {
        // TODO: implementar
        return ResponseEntity.ok().build();
    }
}

