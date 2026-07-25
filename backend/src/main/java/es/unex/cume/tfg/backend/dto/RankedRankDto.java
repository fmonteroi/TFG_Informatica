package es.unex.cume.tfg.backend.dto;

import es.unex.cume.tfg.backend.model.RankedRank;

public record RankedRankDto(
        String queueType,
        String tier,
        String rank,
        Integer leaguePoints,
        Integer wins,
        Integer losses
) {

    public static RankedRankDto fromEntity(RankedRank rankedRank){
        return new RankedRankDto(
                rankedRank.getQueueType(),
                rankedRank.getTier(),
                rankedRank.getRank(),
                rankedRank.getLeaguePoints(),
                rankedRank.getWins(),
                rankedRank.getLosses()
        );
    }
}
