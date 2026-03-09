package es.unex.cume.tfg.backend.config;

import es.unex.cume.tfg.backend.service.ProfessionalService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class ProfessionalDataInitializer implements CommandLineRunner {

    private final ProfessionalService professionalService;

    public ProfessionalDataInitializer(ProfessionalService professionalService) {
        this.professionalService = professionalService;
    }

    @Override
    public void run(String... args) {
        professionalService.initProfessionals();
    }
}

