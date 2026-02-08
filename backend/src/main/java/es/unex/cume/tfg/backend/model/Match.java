package es.unex.cume.tfg.backend.model;

import jakarta.persistence.*;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

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

    public Match() {
    }

    public String getMatchId() {
        return matchId;
    }

    public void setMatchId(String matchId) {
        this.matchId = matchId;
    }

    public Integer getQueueId() {
        return queueId;
    }

    public void setQueueId(Integer queueId) {
        this.queueId = queueId;
    }

    public Instant getGameStartAt() {
        return gameStartAt;
    }

    public void setGameStartAt(Instant gameStartAt) {
        this.gameStartAt = gameStartAt;
    }

    public Long getGameDuration() {
        return gameDuration;
    }

    public void setGameDuration(Long gameDuration) {
        this.gameDuration = gameDuration;
    }

    public String getGameVersion() {
        return gameVersion;
    }

    public void setGameVersion(String gameVersion) {
        this.gameVersion = gameVersion;
    }

    public List<Participation> getParticipations() {
        return participations;
    }

    public void setParticipations(List<Participation> participations) {
        this.participations = participations;
    }
}
