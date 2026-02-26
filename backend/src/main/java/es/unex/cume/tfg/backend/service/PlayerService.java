package es.unex.cume.tfg.backend.service;

import es.unex.cume.tfg.backend.model.Platform;
import es.unex.cume.tfg.backend.model.Player;

import java.util.Optional;

public interface PlayerService {

    Optional<Player> findByPuuid(String puuid);
    boolean existsByPuuid(String puuid);
    Player savePlayer(Platform platform, String gameName, String tagLine);
}
