package es.unex.cume.tfg.backend.model;

import jakarta.persistence.*;

@Entity
public class PlayerStats {
    @Id
    private String puuid;

    private Long gamesPlayed;
    private Long wins;
    private Long losses;
    private Double winRate;
    private Double averageKills;
    private Double averageDeaths;
    private Double averageAssists;
    private Double kda;

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    private Player player;

    @ManyToOne(fetch = FetchType.LAZY)
    private Champion bestChampion;

    public PlayerStats(){

    }

    public String getPuuid() {
        return puuid;
    }

    public void setPuuid(String puuid) {
        this.puuid = puuid;
    }

    public Long getGamesPlayed() {
        return gamesPlayed;
    }

    public void setGamesPlayed(Long gamesPlayed) {
        this.gamesPlayed = gamesPlayed;
    }

    public Long getWins() {
        return wins;
    }

    public void setWins(Long wins) {
        this.wins = wins;
    }

    public Long getLosses() {
        return losses;
    }

    public void setLosses(Long losses) {
        this.losses = losses;
    }

    public Double getWinRate() {
        return winRate;
    }

    public void setWinRate(Double winRate) {
        this.winRate = winRate;
    }

    public Double getAverageKills() {
        return averageKills;
    }

    public void setAverageKills(Double averageKills) {
        this.averageKills = averageKills;
    }

    public Double getAverageDeaths() {
        return averageDeaths;
    }

    public void setAverageDeaths(Double averageDeaths) {
        this.averageDeaths = averageDeaths;
    }

    public Double getAverageAssists() {
        return averageAssists;
    }

    public void setAverageAssists(Double averageAssists) {
        this.averageAssists = averageAssists;
    }

    public Double getKda() {
        return kda;
    }

    public void setKda(Double kda) {
        this.kda = kda;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public Champion getBestChampion() {
        return bestChampion;
    }

    public void setBestChampion(Champion bestChampion) {
        this.bestChampion = bestChampion;
    }
}
