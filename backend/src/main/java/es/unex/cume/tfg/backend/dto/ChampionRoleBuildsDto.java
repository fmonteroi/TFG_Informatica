package es.unex.cume.tfg.backend.dto;

import es.unex.cume.tfg.backend.model.Role;

import java.util.List;

/**
 * DTO that groups recommendation and professional builds for one role.
 *
 * @param role champion role
 * @param recommendedBuild recommended build for the role
 * @param recentProBuilds recent professional builds for the role
 */
public record ChampionRoleBuildsDto(
        Role role,
        RecommendedBuildDto recommendedBuild,
        List<ChampionProBuildDto> recentProBuilds
) {
}
