package es.unex.cume.tfg.backend.dto;

import es.unex.cume.tfg.backend.model.RankedRank;

/**
 * DTO that exposes a player's result in one ranked queue.
 */
public record RankedRankDto(
        String queueType,
        String tier,
        String rank,
        Integer leaguePoints,
        Integer wins,
        Integer losses
) {

    /**
     * Creates a DTO from a ranked result.
     *
     * @param rankedRank ranked result to convert
     * @return ranked result DTO
     */
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
