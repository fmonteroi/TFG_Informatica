package es.unex.cume.tfg.backend.repository;

import es.unex.cume.tfg.backend.model.RankedRank;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RankedRankRepository extends JpaRepository<RankedRank, Long> {
    List<RankedRank> findByPlayerPuuid(String puuid);
    Long deleteByPlayerPuuid(String puuid);
}
