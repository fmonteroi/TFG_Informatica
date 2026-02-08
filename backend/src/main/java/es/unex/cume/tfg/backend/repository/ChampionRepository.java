package es.unex.cume.tfg.backend.repository;

import es.unex.cume.tfg.backend.model.Champion;
import org.springframework.data.repository.CrudRepository;

public interface ChampionRepository extends CrudRepository<Champion, Integer> {
}
