package es.unex.cume.tfg.backend.repository.projection;

import es.unex.cume.tfg.backend.model.Champion;
import es.unex.cume.tfg.backend.model.Role;

/**
 * Groups the number of games played by one champion and role.
 */
public record ChampionRoleGamesAggregate(
        Champion champion,
        Role role,
        Long gamesPlayed
){
}
