package es.unex.cume.tfg.backend.model;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Entity that represents a League of Legends champion from the local catalog.
 */
@Entity
public class Champion {
    @Id
    private Integer championId;

    private String championName;

    // Relations
    @OneToMany(mappedBy = "champion")
    private List<Participation> participations = new ArrayList<>();

    @OneToOne(mappedBy = "champion", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private ChampionStats stats;

    @OneToMany(mappedBy = "bestChampion")
    private List<PlayerStats> bestForPlayers = new ArrayList<>();

    @OneToMany(mappedBy = "champion", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<RecommendedBuild> recommendedBuilds = new ArrayList<>();

    /** Creates an empty champion. */
    public Champion() {
    }

    /**
     *
     * @return Riot champion identifier
     */
    public Integer getChampionId() {
        return championId;
    }

    /**
     *
     * @param championId Riot champion identifier to set
     */
    public void setChampionId(Integer championId) {
        this.championId = championId;
    }

    /**
     *
     * @return champion name
     */
    public String getChampionName() {
        return championName;
    }

    /**
     *
     * @param championName champion name to set
     */
    public void setChampionName(String championName) {
        this.championName = championName;
    }

    /**
     *
     * @return participations played with the champion
     */
    public List<Participation> getParticipations() {
        return participations;
    }

    /**
     *
     * @param participations participations played with the champion to set
     */
    public void setParticipations(List<Participation> participations) {
        this.participations = participations;
    }

    /**
     *
     * @return calculated champion statistics
     */
    public ChampionStats getStats() {
        return stats;
    }

    /**
     *
     * @param stats calculated champion statistics to set
     */
    public void setStats(ChampionStats stats) {
        this.stats = stats;
    }

    /**
     *
     * @return player statistics where this is the best champion
     */
    public List<PlayerStats> getBestForPlayers() {
        return bestForPlayers;
    }

    /**
     *
     * @param bestForPlayers player statistics where this is the best champion to set
     */
    public void setBestForPlayers(List<PlayerStats> bestForPlayers) {
        this.bestForPlayers = bestForPlayers;
    }

    /**
     *
     * @return recommended builds grouped by role
     */
    public List<RecommendedBuild> getRecommendedBuilds() {
        return recommendedBuilds;
    }

    /**
     *
     * @param recommendedBuilds recommended builds grouped by role to set
     */
    public void setRecommendedBuilds(List<RecommendedBuild> recommendedBuilds) {
        this.recommendedBuilds = recommendedBuilds;
    }
}
