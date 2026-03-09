package es.unex.cume.tfg.backend.service;

import es.unex.cume.tfg.backend.config.ChampionJsonLoader;
import es.unex.cume.tfg.backend.exception.ChampionNotFoundException;
import es.unex.cume.tfg.backend.model.Champion;
import es.unex.cume.tfg.backend.repository.ChampionRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ChampionServiceImpl implements ChampionService {

    private final ChampionRepository championRepository;
    private final ChampionJsonLoader championJsonLoader;

    public ChampionServiceImpl(ChampionRepository championRepository, ChampionJsonLoader championJsonLoader) {
        this.championRepository = championRepository;
        this.championJsonLoader = championJsonLoader;
    }

    /**
     * Finds a champion by its ID.
     *
     * @param championId the champion ID
     * @return the champion if found, otherwise throws ChampionNotFoundException
     */
    @Override
    public Champion findChampion(Integer championId) {
        Optional<Champion> optionalChampion = championRepository.findByChampionId(championId);

        if (optionalChampion.isEmpty()) {
            throw new ChampionNotFoundException(championId);
        }

        return optionalChampion.get();
    }

    /**
     * Finds all champions in the database.
     *
     * @return the list of all champions
     */
    @Override
    public List<Champion> findAllChampions() {
        return championRepository.findAll();
    }

    /**
     * Initializes the champion data in db if it's empty.
     */
    @Override
    public void initChampions() {
        // If there are already champions in the database, it returns
        if (championRepository.count() > 0) {
            return;
        }

        // Otherwise, it loads champion seeds from the JSON file and saves them in the database
        List<ChampionJsonLoader.ChampionSeed> championSeeds = championJsonLoader.loadChampionSeeds();
        List<Champion> champions = new ArrayList<>();

        // For each champion seed, it creates a champion and adds it to the list of champions to save
        for (ChampionJsonLoader.ChampionSeed championSeed : championSeeds) {
            champions.add(createChampion(championSeed.championId(), championSeed.championName()));
        }

        // Saves all champions in the database
        championRepository.saveAll(champions);
    }

    /**
     * Creates a champion.
     *
     * @param championId
     * @param championName
     * @return
     */
    private Champion createChampion(Integer championId, String championName) {
        Champion champion = new Champion();
        champion.setChampionId(championId);
        champion.setChampionName(championName);
        return champion;
    }

}