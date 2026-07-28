package es.unex.cume.tfg.backend.model;

import jakarta.persistence.*;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

/**
 * Entity that represents a Riot match imported into the local database.
 */
@Entity
public class Match {
    @Id
    private String matchId;

    private Integer queueId;
    private Instant gameStartAt;
    private Long gameDuration;
    private String gameVersion;

    // Relations
    @OneToMany(mappedBy = "match")
    private List<Participation> participations = new ArrayList<>();

    /** Creates an empty match. */
    public Match() {
    }

    /**
     *
     * @return Riot match identifier
     */
    public String getMatchId() {
        return matchId;
    }

    /**
     *
     * @param matchId Riot match identifier to set
     */
    public void setMatchId(String matchId) {
        this.matchId = matchId;
    }

    /**
     *
     * @return Riot queue identifier
     */
    public Integer getQueueId() {
        return queueId;
    }

    /**
     *
     * @param queueId Riot queue identifier to set
     */
    public void setQueueId(Integer queueId) {
        this.queueId = queueId;
    }

    /**
     *
     * @return match start time
     */
    public Instant getGameStartAt() {
        return gameStartAt;
    }

    /**
     *
     * @param gameStartAt match start time to set
     */
    public void setGameStartAt(Instant gameStartAt) {
        this.gameStartAt = gameStartAt;
    }

    /**
     *
     * @return match duration in seconds
     */
    public Long getGameDuration() {
        return gameDuration;
    }

    /**
     *
     * @param gameDuration match duration in seconds to set
     */
    public void setGameDuration(Long gameDuration) {
        this.gameDuration = gameDuration;
    }

    /**
     *
     * @return game version
     */
    public String getGameVersion() {
        return gameVersion;
    }

    /**
     *
     * @param gameVersion game version to set
     */
    public void setGameVersion(String gameVersion) {
        this.gameVersion = gameVersion;
    }

    /**
     *
     * @return match participations
     */
    public List<Participation> getParticipations() {
        return participations;
    }

    /**
     *
     * @param participations match participations to set
     */
    public void setParticipations(List<Participation> participations) {
        this.participations = participations;
    }
}
