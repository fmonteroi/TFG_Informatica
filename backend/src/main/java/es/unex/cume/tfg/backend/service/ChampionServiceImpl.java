package es.unex.cume.tfg.backend.service;

import es.unex.cume.tfg.backend.model.Champion;
import es.unex.cume.tfg.backend.model.Participation;
import es.unex.cume.tfg.backend.repository.ChampionRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ChampionServiceImpl implements ChampionService {

    private final ChampionRepository championRepository;

    public ChampionServiceImpl(ChampionRepository championRepository) {
        this.championRepository = championRepository;
    }

    /**
     * Finds a champion by its ID.
     *
     * @param championId the champion ID
     * @return the champion if found, otherwise an empty Optional
     */
    @Override
    public Optional<Champion> findByChampionId(Integer championId) {
        return championRepository.findById(championId);
    }

    /**
     * Finds all champions in the database.
     *
     * @return the list of all champions
     */
    @Override
    public List<Champion> findAll() {
        return championRepository.findAll();
    }

}
