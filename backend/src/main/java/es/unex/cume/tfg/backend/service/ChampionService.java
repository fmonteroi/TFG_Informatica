package es.unex.cume.tfg.backend.service;

import es.unex.cume.tfg.backend.model.Champion;

import java.util.List;

public interface ChampionService {
    Champion findChampion(Integer championId);
    List<Champion> findAllChampions();
    void initChampions();
}
