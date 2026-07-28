package es.unex.cume.tfg.backend.dto;

import es.unex.cume.tfg.backend.model.PlayerStats;

/**
 * DTO that exposes calculated player statistics and their best champion.
 */
public record PlayerStatsDto(
        Long gamesPlayed,
        Long wins,
        Long losses,
        Double winRate,
        Double averageKills,
        Double averageDeaths,
        Double averageAssists,
        Double kda,
        ChampionDto bestChampion
) {
    /**
     * Creates a DTO from player statistics.
     *
     * @param stats player statistics to convert
     * @return player statistics DTO
     */
    public static PlayerStatsDto fromEntity(PlayerStats stats){
        // Maps the best champion when enough data is available
        ChampionDto bestChampion = null;

        if (stats.getBestChampion() != null){
            bestChampion = ChampionDto.fromEntity(stats.getBestChampion());
        }

        return new PlayerStatsDto(
                stats.getGamesPlayed(),
                stats.getWins(),
                stats.getLosses(),
                stats.getWinRate(),
                stats.getAverageKills(),
                stats.getAverageDeaths(),
                stats.getAverageAssists(),
                stats.getKda(),
                bestChampion
        );
    }
}
