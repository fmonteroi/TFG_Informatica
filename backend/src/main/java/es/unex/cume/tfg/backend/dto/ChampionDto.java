package es.unex.cume.tfg.backend.dto;

import es.unex.cume.tfg.backend.model.Champion;

public record ChampionDto(
        Integer championId,
        String championName
) {

    public static ChampionDto fromEntity(Champion champion) {
        return new ChampionDto(
                champion.getChampionId(),
                champion.getChampionName()
        );
    }
}
