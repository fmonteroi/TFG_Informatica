package es.unex.cume.tfg.backend.service;

import es.unex.cume.tfg.backend.model.Platform;
import es.unex.cume.tfg.backend.model.Player;
import es.unex.cume.tfg.backend.model.RankedRank;

import java.util.List;

public interface RankedRankService {
    List<RankedRank> refreshRanks(Platform platform, Player player);
    List<RankedRank> findByPuuid(String puuid);
}
