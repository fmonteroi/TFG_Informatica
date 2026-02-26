package es.unex.cume.tfg.backend.model;

import jakarta.persistence.*;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Player {

    @Id
    private String puuid;

    private String riotGameName;
    private String riotTagLine;

    @Enumerated(EnumType.STRING)
    private Platform platform;

    private Integer profileIconId;
    private Integer summonerLevel;
    private Instant updatedAt;

    // Relations
    @OneToOne(mappedBy = "player", cascade = CascadeType.ALL, orphanRemoval = true)
    private Professional professional;

    @OneToMany(mappedBy = "player", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Participation> participations = new ArrayList<>();

    public Player(){

    }

    public String getPuuid() {
        return puuid;
    }

    public void setPuuid(String puuid) {
        this.puuid = puuid;
    }

    public String getRiotGameName() {
        return riotGameName;
    }

    public void setRiotGameName(String riotGameName) {
        this.riotGameName = riotGameName;
    }

    public String getRiotTagLine() {
        return riotTagLine;
    }

    public void setRiotTagLine(String riotTagLine) {
        this.riotTagLine = riotTagLine;
    }

    public Platform getPlatformRegion() {
        return platform;
    }

    public void setPlatformRegion(Platform platform) {
        this.platform = platform;
    }

    public Integer getProfileIconId() {
        return profileIconId;
    }

    public void setProfileIconId(Integer profileIconId) {
        this.profileIconId = profileIconId;
    }

    public Integer getSummonerLevel() {
        return summonerLevel;
    }

    public void setSummonerLevel(Integer summonerLevel) {
        this.summonerLevel = summonerLevel;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Professional getProfessional() {
        return professional;
    }

    public void setProfessional(Professional professional) {
        this.professional = professional;
    }

    public List<Participation> getParticipations() {
        return participations;
    }

    public void setParticipations(List<Participation> participations) {
        this.participations = participations;
    }
}
