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

    @OneToOne(mappedBy = "champion", cascade = CascadeType.ALL, orphanRemoval = true)
    private RecommendedBuild recommendedBuild;

    public Champion() {
    }

    public Integer getChampionId() {
        return championId;
    }

    public void setChampionId(Integer championId) {
        this.championId = championId;
    }

    public String getChampionName() {
        return championName;
    }

    public void setChampionName(String championName) {
        this.championName = championName;
    }

    public List<Participation> getParticipations() {
        return participations;
    }

    public void setParticipations(List<Participation> participations) {
        this.participations = participations;
    }

    public ChampionStats getStats() {
        return stats;
    }

    public void setStats(ChampionStats stats) {
        this.stats = stats;
    }

    public List<PlayerStats> getBestForPlayers() {
        return bestForPlayers;
    }

    public void setBestForPlayers(List<PlayerStats> bestForPlayers) {
        this.bestForPlayers = bestForPlayers;
    }

    public RecommendedBuild getRecommendedBuild() {
        return recommendedBuild;
    }

    public void setRecommendedBuild(RecommendedBuild recommendedBuild) {
        this.recommendedBuild = recommendedBuild;
    }
}
