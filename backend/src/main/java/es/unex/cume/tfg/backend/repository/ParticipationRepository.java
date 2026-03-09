package es.unex.cume.tfg.backend.repository;

import es.unex.cume.tfg.backend.model.Participation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ParticipationRepository extends JpaRepository<Participation, Long> {
    List<Participation> findByPlayerPuuidOrderByGameStartAtDesc(String puuid);
    List<Participation> findByMatchMatchId(String matchId);
    List<Participation> findByChampionChampionIdOrderByGameStartAtDesc(Integer championId);
}
