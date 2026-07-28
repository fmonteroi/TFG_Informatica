package es.unex.cume.tfg.backend.service;

import es.unex.cume.tfg.backend.dto.ChampionProBuildDto;
import es.unex.cume.tfg.backend.dto.ProBuildDto;
import es.unex.cume.tfg.backend.model.Build;
import es.unex.cume.tfg.backend.model.Role;
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

    private final BuildRepository buildRepository;
    private final RankedDataService rankedDataService;

    /**
     * Creates the build service.
     *
     * @param buildRepository   build repository
     * @param rankedDataService ranked data service
     */
    public BuildServiceImpl(BuildRepository buildRepository, RankedDataService rankedDataService) {
        this.buildRepository = buildRepository;
        this.rankedDataService = rankedDataService;
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
     * Finds recent ranked builds used by professional players for a champion and role.
     *
     * @param championId champion identifier
     * @param role       champion role
     * @param limit      maximum number of builds to return
     * @return recent professional builds for the champion and role
     */
    @Override
    public List<ChampionProBuildDto> findRecentProBuildsByChampionIdAndRole(Integer championId, Role role, int limit) {
        if (limit < 1) {
            limit = 10;
        }

        PageRequest pageRequest = PageRequest.of(0, limit);

        List<Build> builds = buildRepository.findRecentProBuildsByChampionIdAndRole(championId, role, rankedDataService.getQueueIds(), pageRequest);

        return builds.stream()
                .map(ChampionProBuildDto::fromEntity)
                .toList();
    }

    /**
     * Finds a professional player's latest ranked builds.
     *
     * @param puuid professional player PUUID
     * @param limit maximum number of builds
     * @return latest professional builds
     */
    @Override
    public List<ProBuildDto> findRecentBuildsByProfessionalPuuid(String puuid, int limit) {
        if (limit < 1) {
            limit = 10;
        }

        PageRequest pageRequest = PageRequest.of(0, limit);

        List<Build> builds = buildRepository.findRecentBuildsByProfessionalPuuid(puuid, rankedDataService.getQueueIds(), pageRequest);

        return builds.stream()
                .map(ProBuildDto::fromEntity)
                .toList();
    }
}

