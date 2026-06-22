package es.unex.cume.tfg.backend.service;

import es.unex.cume.tfg.backend.dto.CurrentGameDto;

/**
 * Service that retrieves current game information for players.
 */
public interface CurrentGameService {
    /**
     * Finds the current game status for a player.
     *
     * @param puuid the player PUUID.
     * @return the current game status.
     */
    CurrentGameDto findCurrentGame(String puuid);
}
