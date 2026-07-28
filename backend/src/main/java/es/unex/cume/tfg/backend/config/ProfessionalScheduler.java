package es.unex.cume.tfg.backend.config;

import es.unex.cume.tfg.backend.service.ProfessionalService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * Schedules periodic professional player data refreshes.
 */
@Component
public class ProfessionalScheduler {

    private final ProfessionalService professionalService;

    /**
     * Creates the professional refresh scheduler.
     *
     * @param professionalService service used to refresh professionals
     */
    public ProfessionalScheduler(ProfessionalService professionalService) {
        this.professionalService = professionalService;
    }

    /**
     * Refreshes all configured professional players at the configured interval.
     */
    @Scheduled(initialDelayString = "${app.scheduler.professionals.initial-delay-ms}", fixedDelayString = "${app.scheduler.professionals.fixed-delay-ms}")
    public void refreshProfessionals(){
        System.out.println("Starting professional refresh");

        // Refreshes professional player data from Riot
        professionalService.refreshProfessionals();
    }
}
