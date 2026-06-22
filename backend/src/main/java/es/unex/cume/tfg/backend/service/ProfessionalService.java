package es.unex.cume.tfg.backend.service;

import es.unex.cume.tfg.backend.dto.ProfessionalsRefreshResultDto;

/**
 * Service that manages professional players and their automatic refresh.
 */
public interface ProfessionalService {

    /**
     * Initializes professional players in the local database.
     */
    void initProfessionals();

    /**
     * Refreshes recent match data for professional players.
     *
     * @return the refresh result summary.
     */
    ProfessionalsRefreshResultDto refreshProfessionals();
}
