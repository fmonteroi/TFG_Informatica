package es.unex.cume.tfg.backend.service;

import es.unex.cume.tfg.backend.model.Professional;

import java.util.List;

/**
 * Service that manages professional players and their automatic refresh.
 */
public interface ProfessionalService {

    /**
     * Synchronizes professional players with the configured roster.
     */
    void synchronizeProfessionals();

    /**
     * Refreshes data for every professional player.
     */
    void refreshProfessionals();

    /**
     * Finds every professional player.
     *
     * @return professional players ordered by name
     */
    List<Professional> findAllProfessionals();

    /**
     * Finds one professional player.
     *
     * @param puuid professional player PUUID
     * @return matching professional player
     */
    Professional findProfessional(String puuid);
}
