package es.unex.cume.tfg.backend.service;

import es.unex.cume.tfg.backend.model.Professional;

import java.util.List;

/**
 * Service that manages professional players and their automatic refresh.
 */
public interface ProfessionalService {

    /**
     * Initializes professional players in the local database.
     */
    void initProfessionals();

    void refreshProfessionals();

    List<Professional> findAllProfessionals();

    Professional findProfessional(String puuid);
}
