package es.unex.cume.tfg.backend.repository;

import es.unex.cume.tfg.backend.model.Player;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.Instant;
import java.util.Optional;

public interface PlayerRepository extends JpaRepository<Player, String> {

    Optional<Player> findByPuuid(String puuid);

    boolean existsByPuuid(String puuid);
}
