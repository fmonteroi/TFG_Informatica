package es.unex.cume.tfg.backend.service;

import es.unex.cume.tfg.backend.dto.ProBuildDto;
import es.unex.cume.tfg.backend.model.Build;
import es.unex.cume.tfg.backend.repository.BuildRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Default implementation of BuildService.
 */
@Service
public class BuildServiceImpl implements BuildService {

    private static final List<Integer> RANKED_QUEUE_IDS = List.of(420, 440);

    private final BuildRepository buildRepository;

    public BuildServiceImpl(BuildRepository buildRepository) {
        this.buildRepository = buildRepository;
    }

    /**
     * Finds a build by its associated participation ID.
     *
     * @param participationId the participation ID
     * @return the build if found, otherwise an empty Optional
     */
    @Override
    public Optional<Build> findByParticipationId(Long participationId) {
        return buildRepository.findByParticipationId(participationId);
    }

    /**
     * Saves a build in the database.
     *
     * @param build the build to save
     * @return the saved build
     */
    @Override
    public Build saveBuild(Build build) {
        return buildRepository.save(build);
    }

    /**
     * Finds recent builds used by professional players for a given champion.
     * Ordered by match date descending.
     *
     * @param championId the champion ID
     * @param limit      the maximum number of builds to return
     * @return the list of recent pro builds
     */
    @Override
    public List<ProBuildDto> findRecentProBuildsByChampionId(Integer championId, int limit) {
        // Default limit
        if (limit < 1) {
            limit = 10;
        }

        // Creates a PageRequest to limit the number of results
        PageRequest pageRequest = PageRequest.of(0, limit);

        // Fetches rankeds recent pro build for a champion
        List<Build> builds = buildRepository.findRecentProBuildsByChampionId(championId, RANKED_QUEUE_IDS, pageRequest);

        // Converts the builds to DTOs and returns them
        return builds.stream()
                .map(ProBuildDto::fromEntity)
                .toList();
    }
}

