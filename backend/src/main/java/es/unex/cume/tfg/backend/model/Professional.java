package es.unex.cume.tfg.backend.model;

import jakarta.persistence.*;

import java.time.Instant;

@Entity
public class Professional {
    @Id
    private String puuid;

    private String proName;
    private String teamName;
    private String league;

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

    public String getProName() {
        return proName;
    }

    public void setProName(String proName) {
        this.proName = proName;
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

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }
}