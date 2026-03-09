package es.unex.cume.tfg.backend.dto;

import es.unex.cume.tfg.backend.model.Build;

public record BuildDto(
        Integer item0,
        Integer item1,
        Integer item2,
        Integer item3,
        Integer item4,
        Integer item5,
        Integer item6,
        Integer summoner1Id,
        Integer summoner2Id
) {
    public static BuildDto fromEntity(Build build) {
        return new BuildDto(
                build.getItem0(),
                build.getItem1(),
                build.getItem2(),
                build.getItem3(),
                build.getItem4(),
                build.getItem5(),
                build.getItem6(),
                build.getSummoner1Id(),
                build.getSummoner2Id()
        );
    }
}
