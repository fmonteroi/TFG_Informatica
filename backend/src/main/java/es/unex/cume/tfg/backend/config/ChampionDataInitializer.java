package es.unex.cume.tfg.backend.config;

import es.unex.cume.tfg.backend.service.ChampionService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class ChampionDataInitializer implements CommandLineRunner {
    private final ChampionService championService;

    public ChampionDataInitializer(ChampionService championService) {
        this.championService = championService;
    }

    @Override
    public void run(String... args) {
        championService.initChampions();
    }
}