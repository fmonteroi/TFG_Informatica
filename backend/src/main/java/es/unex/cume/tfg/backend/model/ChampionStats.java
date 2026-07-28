package es.unex.cume.tfg.backend.model;

import jakarta.persistence.*;

/**
 * Entity that stores calculated statistics for one champion.
 */
@Entity
public class ChampionStats {
    @Id
    private Integer championId;

    private Long gamesPlayed;
    private Long wins;
    private Long losses;
    private Double winRate;
    private Double averageKills;
    private Double averageDeaths;
    private Double averageAssists;
    private Double kda;
    @Enumerated(EnumType.STRING)
    private Tier tier;

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    private Champion champion;

    /** Creates empty champion statistics. */
    public ChampionStats() {

    }

    /**
     *
     * @return champion identifier
     */
    public Integer getChampionId() {
        return championId;
    }

    /**
     *
     * @param championId champion identifier to set
     */
    public void setChampionId(Integer championId) {
        this.championId = championId;
    }

    /**
     *
     * @return total games played
     */
    public Long getGamesPlayed() {
        return gamesPlayed;
    }

    /**
     *
     * @param gamesPlayed total games played to set
     */
    public void setGamesPlayed(Long gamesPlayed) {
        this.gamesPlayed = gamesPlayed;
    }

    /**
     *
     * @return total wins
     */
    public Long getWins() {
        return wins;
    }

    /**
     *
     * @param wins total wins to set
     */
    public void setWins(Long wins) {
        this.wins = wins;
    }

    /**
     *
     * @return total losses
     */
    public Long getLosses() {
        return losses;
    }

    /**
     *
     * @param losses total losses to set
     */
    public void setLosses(Long losses) {
        this.losses = losses;
    }

    /**
     *
     * @return win rate percentage
     */
    public Double getWinRate() {
        return winRate;
    }

    /**
     *
     * @param winRate win rate percentage to set
     */
    public void setWinRate(Double winRate) {
        this.winRate = winRate;
    }

    /**
     *
     * @return average kills
     */
    public Double getAverageKills() {
        return averageKills;
    }

    /**
     *
     * @param averageKills average kills to set
     */
    public void setAverageKills(Double averageKills) {
        this.averageKills = averageKills;
    }

    /**
     *
     * @return average deaths
     */
    public Double getAverageDeaths() {
        return averageDeaths;
    }

    /**
     *
     * @param averageDeaths average deaths to set
     */
    public void setAverageDeaths(Double averageDeaths) {
        this.averageDeaths = averageDeaths;
    }

    /**
     *
     * @return average assists
     */
    public Double getAverageAssists() {
        return averageAssists;
    }

    /**
     *
     * @param averageAssists average assists to set
     */
    public void setAverageAssists(Double averageAssists) {
        this.averageAssists = averageAssists;
    }

    /**
     *
     * @return average KDA
     */
    public Double getKda() {
        return kda;
    }

    /**
     *
     * @param kda average KDA to set
     */
    public void setKda(Double kda) {
        this.kda = kda;
    }

    /**
     *
     * @return champion described by these statistics
     */
    public Champion getChampion() {
        return champion;
    }

    /**
     *
     * @param champion champion described by these statistics to set
     */
    public void setChampion(Champion champion) {
        this.champion = champion;
    }

    /**
     *
     * @return calculated champion tier
     */
    public Tier getTier() {
        return tier;
    }

    /**
     *
     * @param tier calculated champion tier to set
     */
    public void setTier(Tier tier) {
        this.tier = tier;
    }
}
