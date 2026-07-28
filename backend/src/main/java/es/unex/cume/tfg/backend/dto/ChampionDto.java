package es.unex.cume.tfg.backend.dto;

import es.unex.cume.tfg.backend.model.Champion;
import es.unex.cume.tfg.backend.model.Tier;

/**
 * DTO that exposes champion catalog data.
 */
public record ChampionDto(
        Integer championId,
        String championName,
        Tier tier
) {

    /**
     * Creates a DTO from a Champion entity.
     *
     * @param champion the champion entity to convert.
     * @return the champion DTO.
     */
    public static ChampionDto fromEntity(Champion champion) {
        Tier tier = null;

        if (champion.getStats() != null) {
            tier = champion.getStats().getTier();
        }

        return new ChampionDto(
                champion.getChampionId(),
                champion.getChampionName(),
                tier
        );
    }
}
