package es.unex.cume.tfg.backend.repository;

import es.unex.cume.tfg.backend.model.Platform;
import es.unex.cume.tfg.backend.model.Player;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Repository for Player persistence and PUUID lookups.
 */
public interface PlayerRepository extends JpaRepository<Player, String> {

    /**
     * Finds a player by their Riot PUUID.
     *
     * @param puuid the Riot PUUID.
     * @return the player if it exists.
     */
    Optional<Player> findByPuuid(String puuid);

    /**
     * Checks whether a player exists by their Riot PUUID.
     *
     * @param puuid the Riot PUUID.
     * @return true if the player exists.
     */
    boolean existsByPuuid(String puuid);

    Optional<Player> findByPlatformAndGameNameIgnoreCaseAndTagLineIgnoreCase(Platform platform, String gameName, String tagLine);
}
