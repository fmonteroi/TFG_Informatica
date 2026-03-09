package es.unex.cume.tfg.backend.controller;

import es.unex.cume.tfg.backend.dto.MessageResponseDto;
import es.unex.cume.tfg.backend.service.ProfessionalService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
     * @return
     */
    @PostMapping("/refresh")
    public ResponseEntity<MessageResponseDto> refreshProfessionals() {
        professionalService.refreshProfessionals();
        return ResponseEntity.ok(new MessageResponseDto("Professionals refreshed successfully"));
    }
}
