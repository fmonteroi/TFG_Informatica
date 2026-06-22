package es.unex.cume.tfg.backend.service;

import es.unex.cume.tfg.backend.config.ChampionJsonLoader;
import es.unex.cume.tfg.backend.exception.ChampionNotFoundException;
import es.unex.cume.tfg.backend.model.Champion;
import es.unex.cume.tfg.backend.repository.ChampionRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Default implementation of ChampionService.
 */
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
     * Synchronizes champion data from the local JSON catalog.
     */
    @Override
    public void initChampions() {
        List<ChampionJsonLoader.ChampionSeed> championSeeds = championJsonLoader.loadChampionSeeds();
        List<Champion> champions = new ArrayList<>();

        for (ChampionJsonLoader.ChampionSeed championSeed : championSeeds) {
            Optional<Champion> optionalChampion = championRepository.findByChampionId(championSeed.championId());
            Champion champion;

            if (optionalChampion.isPresent()) {
                champion = optionalChampion.get();
            } else {
                champion = new Champion();
                champion.setChampionId(championSeed.championId());
            }

            champion.setChampionName(championSeed.championName());
            champions.add(champion);
        }

        championRepository.saveAll(champions);
    }

}
