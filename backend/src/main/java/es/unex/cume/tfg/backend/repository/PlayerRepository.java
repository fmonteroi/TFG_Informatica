package es.unex.cume.tfg.backend.repository;

import es.unex.cume.tfg.backend.model.Player;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PlayerRepository extends CrudRepository<Player, String> {
}
