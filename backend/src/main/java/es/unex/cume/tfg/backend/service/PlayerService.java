package es.unex.cume.tfg.backend.service;

import es.unex.cume.tfg.backend.model.Platform;
import es.unex.cume.tfg.backend.model.Player;

import java.util.Optional;

public interface PlayerService {

    Optional<Player> findByPuuid(String puuid);
    boolean existsByPuuid(String puuid);
    Player searchPlayer(Platform platform, String gameName, String tagLine);
    Player refreshPlayer(Platform platform, String puuid);
    Player syncPlayerForProfessional(Platform platform, String gameName, String tagLine, String puuid);
}