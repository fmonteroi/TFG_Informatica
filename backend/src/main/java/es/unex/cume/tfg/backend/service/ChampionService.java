package es.unex.cume.tfg.backend.service;

import es.unex.cume.tfg.backend.model.Champion;

import java.util.List;

/**
 * Service that manages champion catalog data.
 */
public interface ChampionService {
    /**
     * Finds a champion by ID.
     *
     * @param championId the champion ID.
     * @return the champion.
     */
    Champion findChampion(Integer championId);

    /**
     * Finds all champions.
     *
     * @return the champion catalog.
     */
    List<Champion> findAllChampions();

    /**
     * Synchronizes champion data from the local JSON catalog.
     */
    void initChampions();
}
