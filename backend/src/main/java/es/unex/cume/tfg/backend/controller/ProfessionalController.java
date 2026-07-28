package es.unex.cume.tfg.backend.controller;

import es.unex.cume.tfg.backend.dto.ProBuildDto;
import es.unex.cume.tfg.backend.dto.ProfessionalDetailsDto;
import es.unex.cume.tfg.backend.dto.ProfessionalDto;
import es.unex.cume.tfg.backend.model.Player;
import es.unex.cume.tfg.backend.model.Professional;
import es.unex.cume.tfg.backend.service.BuildService;
import es.unex.cume.tfg.backend.service.ProfessionalService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Provides endpoints for professional player information and recent builds.
 */
@RestController
@RequestMapping("/api/professionals")
public class ProfessionalController {

    private final ProfessionalService professionalService;
    private final BuildService buildService;

    /**
     * Creates the professional controller.
     *
     * @param professionalService service used to get professional players
     * @param buildService service used to get professional builds
     */
    public ProfessionalController(ProfessionalService professionalService, BuildService buildService) {
        this.professionalService = professionalService;
        this.buildService = buildService;
    }

    /**
     * Gets every registered professional player.
     *
     * @return registered professional players
     */
    @GetMapping
    public ResponseEntity<List<ProfessionalDto>> findAllProfessionals() {
        List<ProfessionalDto> professionals = professionalService.findAllProfessionals()
                        .stream()
                        .map(ProfessionalDto::fromEntity)
                        .toList();

        return ResponseEntity.ok(professionals);
    }

    /**
     * Gets a professional player and their latest ranked builds.
     *
     * @param puuid professional player PUUID
     * @param buildCount maximum number of builds to include
     * @return professional details with recent builds
     */
    @GetMapping("/{puuid}")
    public ResponseEntity<ProfessionalDetailsDto> findProfessionalDetails(@PathVariable String puuid, @RequestParam(defaultValue = "10") int buildCount) {
        Professional professional = professionalService.findProfessional(puuid);

        Player player = professional.getPlayer();
        List<ProBuildDto> recentBuilds = buildService.findRecentBuildsByProfessionalPuuid(puuid, buildCount);

        ProfessionalDetailsDto details = new ProfessionalDetailsDto(
                professional.getPuuid(),
                professional.getProName(),
                professional.getTeamName(),
                professional.getLeague(),
                professional.getRole(),
                player.getGameName(),
                player.getTagLine(),
                player.getPlatform(),
                player.getProfileIconId(),
                recentBuilds
        );

        return ResponseEntity.ok(details);
    }
}
