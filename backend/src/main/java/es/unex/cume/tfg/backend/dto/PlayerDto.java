package es.unex.cume.tfg.backend.dto;

import es.unex.cume.tfg.backend.model.Platform;
import es.unex.cume.tfg.backend.model.Player;

import java.time.Instant;

/**
 * DTO that exposes player profile data.
 */
public record PlayerDto(
        String puuid,
        String gameName,
        String tagLine,
        Platform platform,
        Integer profileIconId,
        Long summonerLevel,
        Instant lastSyncAt
) {

    /**
     * Creates a DTO from a Player entity.
     *
     * @param player the player entity to convert.
     * @return the player DTO.
     */
    public static PlayerDto fromEntity(Player player) {
        return new PlayerDto(
                player.getPuuid(),
                player.getGameName(),
                player.getTagLine(),
                player.getPlatform(),
                player.getProfileIconId(),
                player.getSummonerLevel(),
                player.getLastSyncAt()
        );
    }
}
