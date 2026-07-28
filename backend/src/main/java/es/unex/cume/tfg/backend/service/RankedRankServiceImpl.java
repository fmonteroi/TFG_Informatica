package es.unex.cume.tfg.backend.service;

import es.unex.cume.tfg.backend.model.Platform;
import es.unex.cume.tfg.backend.model.Player;
import es.unex.cume.tfg.backend.model.RankedRank;
import es.unex.cume.tfg.backend.repository.RankedRankRepository;
import es.unex.cume.tfg.backend.riot.dto.LeagueEntryDto;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Refreshes and stores player ranked results.
 */
@Service
public class RankedRankServiceImpl implements RankedRankService {

    private final RankedRankRepository rankedRankRepository;
    private final RiotFetchService riotFetchService;

    /**
     * Creates the ranked result service.
     *
     * @param rankedRankRepository ranked result repository
     * @param riotFetchService Riot data service
     */
    public RankedRankServiceImpl(RankedRankRepository rankedRankRepository, RiotFetchService riotFetchService) {
        this.rankedRankRepository = rankedRankRepository;
        this.riotFetchService = riotFetchService;
    }

    /**
     * Replaces stored ranked results with current Riot data.
     *
     * @param platform Riot platform
     * @param player player to refresh
     * @return saved ranked results
     */
    @Override
    @Transactional
    public List<RankedRank> refreshRanks(Platform platform, Player player) {
        // Gets the current results before replacing the old snapshot
        List<LeagueEntryDto> entries = riotFetchService.fetchLeagueEntries(platform, player.getPuuid());

        rankedRankRepository.deleteByPlayerPuuid(player.getPuuid());

        List<RankedRank> ranks = entries.stream()
                .map(entry -> toEntity(entry, player))
                .toList();

        return rankedRankRepository.saveAll(ranks);
    }

    /**
     * Finds a player's ranked results.
     *
     * @param puuid player PUUID
     * @return ranked results
     */
    @Override
    public List<RankedRank> findByPuuid(String puuid) {
        return rankedRankRepository.findByPlayerPuuid(puuid);
    }

    /**
     * Creates a ranked result from Riot data.
     *
     * @param entry Riot ranked result
     * @param player related player
     * @return ranked result entity
     */
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
