package es.unex.cume.tfg.backend.service;

import es.unex.cume.tfg.backend.model.Match;
import es.unex.cume.tfg.backend.repository.MatchRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Default implementation of RankedDataService.
 */
@Service
public class RankedDataServiceImpl implements RankedDataService {

    private static final List<Integer> RANKED_QUEUE_IDS = List.of(420, 440);

    private final MatchRepository matchRepository;

    /**
     * Creates the ranked data service.
     *
     * @param matchRepository match repository
     */
    public RankedDataServiceImpl(MatchRepository matchRepository) {
        this.matchRepository = matchRepository;
    }

    /**
     * Gets the Solo/Duo and Flex ranked queue identifiers.
     *
     * @return ranked queue identifiers
     */
    @Override
    public List<Integer> getQueueIds() {
        return RANKED_QUEUE_IDS;
    }

    /**
     * Finds the patch of the latest stored ranked match.
     *
     * @return latest stored ranked patch when available
     */
    @Override
    public Optional<String> findLatestPatch() {
        Optional<Match> optionalMatch = matchRepository.findFirstByQueueIdInOrderByGameStartAtDesc(RANKED_QUEUE_IDS);

        if (optionalMatch.isEmpty()) {
            return Optional.empty();
        }

        String patch = extractPatch(optionalMatch.get().getGameVersion());
        return Optional.ofNullable(patch);
    }

    /**
     * Gets the major and minor patch from a full game version.
     *
     * @param gameVersion full Riot game version
     * @return major and minor patch or null when invalid
     */
    private String extractPatch(String gameVersion) {
        if (gameVersion == null || gameVersion.isBlank()) {
            return null;
        }

        String[] parts = gameVersion.split("\\.");

        if (parts.length < 2) {
            return null;
        }

        return parts[0] + "." + parts[1];
    }
}
