package es.unex.cume.tfg.backend.loader;

import es.unex.cume.tfg.backend.model.Platform;
import es.unex.cume.tfg.backend.model.Role;

/**
 * Represents one professional player entry loaded from the roster configuration.
 *
 * @param code internal identifier
 * @param platform Riot platform where the account is registered
 * @param proName professional player name
 * @param gameName Riot account game name
 * @param tagLine Riot account tag line
 * @param teamName current professional team
 * @param league current professional league
 * @param role role played within the team
 */
public record ProfessionalEntry (
    String code,
    Platform platform,
    String proName,
    String gameName,
    String tagLine,
    String teamName,
    String league,
    Role role
) {
}
