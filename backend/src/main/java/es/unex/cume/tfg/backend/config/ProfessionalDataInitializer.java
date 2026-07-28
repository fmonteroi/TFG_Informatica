package es.unex.cume.tfg.backend.config;

import es.unex.cume.tfg.backend.service.ProfessionalService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * Synchronizes the configured professional roster when the application starts.
 */
@Component
public class ProfessionalDataInitializer implements CommandLineRunner {

    private final ProfessionalService professionalService;

    /**
     * Creates the professional data initializer.
     *
     * @param professionalService service used to synchronize professionals
     */
    public ProfessionalDataInitializer(ProfessionalService professionalService) {
        this.professionalService = professionalService;
    }

    /**
     * Synchronizes professional records during application startup.
     *
     * @param args application startup arguments
     */
    @Override
    public void run(String... args) {
        // Synchronizes the database with the configured roster
        professionalService.synchronizeProfessionals();
    }
}
