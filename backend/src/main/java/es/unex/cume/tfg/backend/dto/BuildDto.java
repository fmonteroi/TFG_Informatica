package es.unex.cume.tfg.backend.dto;

import es.unex.cume.tfg.backend.model.Build;

/**
 * DTO that exposes item and summoner spell choices for a participation.
 */
public record BuildDto(
        Integer item0,
        Integer item1,
        Integer item2,
        Integer item3,
        Integer item4,
        Integer item5,
        Integer item6,
        Integer roleBoundItem,
        Integer summoner1Id,
        Integer summoner2Id
) {
    /**
     * Creates a DTO from a Build entity.
     *
     * @param build the build entity to convert.
     * @return the build DTO.
     */
    public static BuildDto fromEntity(Build build) {
        return new BuildDto(
                build.getItem0(),
                build.getItem1(),
                build.getItem2(),
                build.getItem3(),
                build.getItem4(),
                build.getItem5(),
                build.getItem6(),
                build.getRoleBoundItem(),
                build.getSummoner1Id(),
                build.getSummoner2Id()
        );
    }
}
