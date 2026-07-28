package es.unex.cume.tfg.backend.dto;

import es.unex.cume.tfg.backend.model.ChampionStats;

/**
 * DTO that exposes calculated champion statistics.
 */
public record ChampionStatsDto(
        Integer championId,
        Long gamesPlayed,
        Long wins,
        Long losses,
        Double winRate,
        Double averageKills,
        Double averageDeaths,
        Double averageAssists,
        Double kda
) {
    /**
     * Creates a DTO from champion statistics.
     *
     * @param stats champion statistics to convert
     * @return champion statistics DTO
     */
    public static ChampionStatsDto fromEntity(ChampionStats stats){
        return new ChampionStatsDto(
                stats.getChampionId(),
                stats.getGamesPlayed(),
                stats.getWins(),
                stats.getLosses(),
                stats.getWinRate(),
                stats.getAverageKills(),
                stats.getAverageDeaths(),
                stats.getAverageAssists(),
                stats.getKda()
        );
    }
}
