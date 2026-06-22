package es.unex.cume.tfg.backend.dto;

import es.unex.cume.tfg.backend.model.Champion;

/**
 * DTO that exposes champion catalog data.
 */
public record ChampionDto(
        Integer championId,
        String championName
) {

    /**
     * Creates a DTO from a Champion entity.
     *
     * @param champion the champion entity to convert.
     * @return the champion DTO.
     */
    public static ChampionDto fromEntity(Champion champion) {
        return new ChampionDto(
                champion.getChampionId(),
                champion.getChampionName()
        );
    }
}
