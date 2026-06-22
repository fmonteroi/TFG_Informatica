package es.unex.cume.tfg.backend.config;

import es.unex.cume.tfg.backend.service.ChampionService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * Initializes the local champion catalog when the backend starts.
 */
@Component
public class ChampionDataInitializer implements CommandLineRunner {
    private final ChampionService championService;

    public ChampionDataInitializer(ChampionService championService) {
        this.championService = championService;
    }

    /**
     * Runs champion catalog synchronization at application startup.
     *
     * @param args startup arguments.
     */
    @Override
    public void run(String... args) {
        championService.initChampions();
    }
}
