package es.unex.cume.tfg.backend.model;

import jakarta.persistence.*;

import java.time.Instant;

@Entity
public class Professional {
    @Id
    private String puuid;

    private String teamName;
    private String league;
    private Instant lastBuildUpdateAt;

    // Relations
    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    private Player player;

    public Professional() {

    }

    public String getPuuid() {
        return puuid;
    }

    public void setPuuid(String puuid) {
        this.puuid = puuid;
    }

    public String getTeamName() {
        return teamName;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }

    public String getLeague() {
        return league;
    }

    public void setLeague(String league) {
        this.league = league;
    }

    public Instant getLastBuildUpdateAt() {
        return lastBuildUpdateAt;
    }

    public void setLastBuildUpdateAt(Instant lastBuildUpdateAt) {
        this.lastBuildUpdateAt = lastBuildUpdateAt;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }
}
