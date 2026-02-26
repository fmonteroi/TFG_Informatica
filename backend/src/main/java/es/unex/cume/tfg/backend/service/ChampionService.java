package es.unex.cume.tfg.backend.service;

import es.unex.cume.tfg.backend.model.Champion;
import es.unex.cume.tfg.backend.model.Participation;

import java.util.List;
import java.util.Optional;

public interface ChampionService {
    Optional<Champion> findByChampionId(Integer championId);
    List<Champion> findAll();
}
