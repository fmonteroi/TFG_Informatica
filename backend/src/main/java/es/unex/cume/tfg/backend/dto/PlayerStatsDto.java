package es.unex.cume.tfg.backend.dto;

import es.unex.cume.tfg.backend.model.PlayerStats;

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
    public static PlayerStatsDto fromEntity(PlayerStats stats){
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
