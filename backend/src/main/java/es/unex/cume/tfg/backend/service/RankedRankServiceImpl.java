package es.unex.cume.tfg.backend.service;

import es.unex.cume.tfg.backend.model.Platform;
import es.unex.cume.tfg.backend.model.Player;
import es.unex.cume.tfg.backend.model.RankedRank;
import es.unex.cume.tfg.backend.repository.RankedRankRepository;
import es.unex.cume.tfg.backend.riot.dto.LeagueEntryDto;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class RankedRankServiceImpl implements RankedRankService {

    private final RankedRankRepository rankedRankRepository;
    private final RiotFetchService riotFetchService;

    public RankedRankServiceImpl(RankedRankRepository rankedRankRepository, RiotFetchService riotFetchService) {
        this.rankedRankRepository = rankedRankRepository;
        this.riotFetchService = riotFetchService;
    }

    @Override
    @Transactional
    public List<RankedRank> refreshRanks(Platform platform, Player player) {
        List<LeagueEntryDto> entries = riotFetchService.fetchLeagueEntries(platform, player.getPuuid());

        rankedRankRepository.deleteByPlayerPuuid(player.getPuuid());

        List<RankedRank> ranks = entries.stream()
                .map(entry -> toEntity(entry, player))
                .toList();

        return rankedRankRepository.saveAll(ranks);
    }

    @Override
    public List<RankedRank> findByPuuid(String puuid) {
        return rankedRankRepository.findByPlayerPuuid(puuid);
    }

    private RankedRank toEntity(LeagueEntryDto entry, Player player) {
        RankedRank rank = new RankedRank();
        rank.setQueueType(entry.queueType());
        rank.setTier(entry.tier());
        rank.setRank(entry.rank());
        rank.setLeaguePoints(entry.leaguePoints());
        rank.setWins(entry.wins());
        rank.setLosses(entry.losses());
        rank.setPlayer(player);
        return rank;
    }
}