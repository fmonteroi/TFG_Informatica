package es.unex.cume.tfg.backend.controller;

import es.unex.cume.tfg.backend.dto.ProfessionalsRefreshResultDto;
import es.unex.cume.tfg.backend.service.ProfessionalService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST controller that exposes professional player maintenance endpoints.
 */
@RestController
@RequestMapping("/api/professionals")
public class ProfessionalController {

    private final ProfessionalService professionalService;

    public ProfessionalController(ProfessionalService professionalService) {
        this.professionalService = professionalService;
    }

    /**
     * Refreshes all professionals data.
     * Call: POST /api/professionals/refresh
     *
     * @return the refresh result summary.
     */
    @PostMapping("/refresh")
    public ResponseEntity<ProfessionalsRefreshResultDto> refreshProfessionals() {
        return ResponseEntity.ok(professionalService.refreshProfessionals());
    }
}
