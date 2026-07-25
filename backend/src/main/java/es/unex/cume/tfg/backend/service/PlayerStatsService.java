package es.unex.cume.tfg.backend.service;

import es.unex.cume.tfg.backend.model.Player;
import es.unex.cume.tfg.backend.model.PlayerStats;

import java.util.Optional;

public interface PlayerStatsService {
    PlayerStats calculatePlayerStats(Player player);
    Optional<PlayerStats> findByPuuid(String puuid);
}
