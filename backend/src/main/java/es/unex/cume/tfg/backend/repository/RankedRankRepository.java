package es.unex.cume.tfg.backend.repository;

import es.unex.cume.tfg.backend.model.RankedRank;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Repository for player ranked results.
 */
public interface RankedRankRepository extends JpaRepository<RankedRank, Long> {

    /**
     * Finds every ranked result for a player.
     *
     * @param puuid player PUUID
     * @return player ranked results
     */
    List<RankedRank> findByPlayerPuuid(String puuid);

    /**
     * Deletes every ranked result for a player.
     *
     * @param puuid player PUUID
     * @return number of deleted results
     */
    Long deleteByPlayerPuuid(String puuid);
}
