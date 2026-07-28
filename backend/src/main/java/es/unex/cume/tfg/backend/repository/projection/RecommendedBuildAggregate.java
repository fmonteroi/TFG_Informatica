package es.unex.cume.tfg.backend.repository.projection;

import es.unex.cume.tfg.backend.model.Champion;
import es.unex.cume.tfg.backend.model.Role;

/**
 * Groups equal builds and their match results for recommendation scoring.
 */
public record RecommendedBuildAggregate(
        Champion champion,
        Role role,
        Integer item0,
        Integer item1,
        Integer item2,
        Integer item3,
        Integer item4,
        Integer item5,
        Integer item6,
        Integer roleBoundItem,
        Integer summoner1Id,
        Integer summoner2Id,
        Long gamesPlayed,
        Long wins
) {
}
