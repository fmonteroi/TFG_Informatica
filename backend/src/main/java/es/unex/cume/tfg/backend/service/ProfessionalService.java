package es.unex.cume.tfg.backend.service;

import es.unex.cume.tfg.backend.dto.ProfessionalsRefreshResultDto;

public interface ProfessionalService {

    void initProfessionals();

    ProfessionalsRefreshResultDto refreshProfessionals();
}
