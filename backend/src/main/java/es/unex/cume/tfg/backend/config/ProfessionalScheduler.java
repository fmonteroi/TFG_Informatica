package es.unex.cume.tfg.backend.config;

import es.unex.cume.tfg.backend.service.ProfessionalService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class ProfessionalScheduler {

    private final ProfessionalService professionalService;

    public ProfessionalScheduler(ProfessionalService professionalService) {
        this.professionalService = professionalService;
    }

    @Scheduled(initialDelayString = "${app.scheduler.professionals.initial-delay-ms}", fixedDelayString = "${app.scheduler.professionals.fixed-delay-ms}")
    public void refreshProfessionals(){
        System.out.println("Starting professional refresh");
        professionalService.refreshProfessionals();
    }
}
