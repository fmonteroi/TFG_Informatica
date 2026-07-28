package es.unex.cume.tfg.backend.model;

import jakarta.persistence.*;

import java.time.Instant;

/**
 * Entity that stores a player's result in one ranked queue.
 */
@Entity
public class RankedRank {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String queueType;

    // Rank iron/bronze/silver/gold/platinum/diamond/master/grandmaster/challenger
    private String tier;
    // division I/II/III/IV
    private String rank;
    private Integer leaguePoints;
    private Integer wins;
    private Integer losses;

    @ManyToOne(fetch = FetchType.LAZY)
    private Player player;

    /** Creates an empty ranked result. */
    public RankedRank(){

    }

    /**
     *
     * @return ranked result identifier
     */
    public Long getId() {
        return id;
    }

    /**
     *
     * @param id ranked result identifier to set
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     *
     * @return Riot queue type
     */
    public String getQueueType() {
        return queueType;
    }

    /**
     *
     * @param queueType Riot queue type to set
     */
    public void setQueueType(String queueType) {
        this.queueType = queueType;
    }

    /**
     *
     * @return ranked tier
     */
    public String getTier() {
        return tier;
    }

    /**
     *
     * @param tier ranked tier to set
     */
    public void setTier(String tier) {
        this.tier = tier;
    }

    /**
     *
     * @return division within the tier
     */
    public String getRank() {
        return rank;
    }

    /**
     *
     * @param rank division within the tier to set
     */
    public void setRank(String rank) {
        this.rank = rank;
    }

    /**
     *
     * @return current league points
     */
    public Integer getLeaguePoints() {
        return leaguePoints;
    }

    /**
     *
     * @param leaguePoints current league points to set
     */
    public void setLeaguePoints(Integer leaguePoints) {
        this.leaguePoints = leaguePoints;
    }

    /**
     *
     * @return ranked wins
     */
    public Integer getWins() {
        return wins;
    }

    /**
     *
     * @param wins ranked wins to set
     */
    public void setWins(Integer wins) {
        this.wins = wins;
    }

    /**
     *
     * @return ranked losses
     */
    public Integer getLosses() {
        return losses;
    }

    /**
     *
     * @param losses ranked losses to set
     */
    public void setLosses(Integer losses) {
        this.losses = losses;
    }

    /**
     *
     * @return related player
     */
    public Player getPlayer() {
        return player;
    }

    /**
     *
     * @param player related player to set
     */
    public void setPlayer(Player player) {
        this.player = player;
    }
}
