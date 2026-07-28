package es.unex.cume.tfg.backend.service;

import es.unex.cume.tfg.backend.dto.ChampionProBuildDto;
import es.unex.cume.tfg.backend.dto.ProBuildDto;
import es.unex.cume.tfg.backend.model.Build;
import es.unex.cume.tfg.backend.model.Role;

import java.util.List;
import java.util.Optional;

/**
 * Service that manages build persistence and professional build queries.
 */
public interface BuildService {

    /**
     * Finds a build by participation ID.
     *
     * @param participationId the participation ID.
     * @return the build if it exists.
     */
    Optional<Build> findByParticipationId(Long participationId);

    /**
     * Saves a build.
     *
     * @param build the build to save.
     * @return the saved build.
     */
    Build saveBuild(Build build);

    /**
     * Finds recent professional builds for a champion and role.
     *
     * @param championId champion identifier
     * @param role champion role
     * @param limit maximum number of builds
     * @return recent professional builds
     */
    List<ChampionProBuildDto> findRecentProBuildsByChampionIdAndRole(Integer championId, Role role, int limit);

    /**
     * Finds a professional player's latest ranked builds.
     *
     * @param puuid professional player PUUID
     * @param limit maximum number of builds
     * @return latest professional builds
     */
    List<ProBuildDto> findRecentBuildsByProfessionalPuuid(String puuid, int limit);
}

