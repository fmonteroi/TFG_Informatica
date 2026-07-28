package es.unex.cume.tfg.backend.model;

import jakarta.persistence.*;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

/**
 * Entity that represents a Riot player known by the application.
 */
@Entity
public class Player {

    @Id
    private String puuid;

    private String gameName;
    private String tagLine;

    @Enumerated(EnumType.STRING)
    private Platform platform;

    private Integer profileIconId;
    private Long summonerLevel;
    private Instant lastSyncAt;

    // Relations
    @OneToOne(mappedBy = "player", cascade = CascadeType.ALL, orphanRemoval = true)
    private Professional professional;

    @OneToMany(mappedBy = "player", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Participation> participations = new ArrayList<>();

    @OneToMany(mappedBy = "player", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<RankedRank> rankedRanks = new ArrayList<>();

    @OneToOne(mappedBy = "player", cascade = CascadeType.ALL, orphanRemoval = true)
    private PlayerStats stats;

    /** Creates an empty player. */
    public Player(){

    }

    /**
     *
     * @return stable Riot PUUID
     */
    public String getPuuid() {
        return puuid;
    }

    /**
     *
     * @param puuid stable Riot PUUID to set
     */
    public void setPuuid(String puuid) {
        this.puuid = puuid;
    }

    /**
     *
     * @return Riot game name
     */
    public String getGameName() {
        return gameName;
    }

    /**
     *
     * @param riotGameName Riot game name to set
     */
    public void setGameName(String riotGameName) {
        this.gameName = riotGameName;
    }

    /**
     *
     * @return Riot tag line
     */
    public String getTagLine() {
        return tagLine;
    }

    /**
     *
     * @param tagLine Riot tag line to set
     */
    public void setTagLine(String tagLine) {
        this.tagLine = tagLine;
    }

    /**
     *
     * @return Riot platform
     */
    public Platform getPlatform() {
        return platform;
    }

    /**
     *
     * @param platform Riot platform to set
     */
    public void setPlatform(Platform platform) {
        this.platform = platform;
    }

    /**
     *
     * @return profile icon identifier
     */
    public Integer getProfileIconId() {
        return profileIconId;
    }

    /**
     *
     * @param profileIconId profile icon identifier to set
     */
    public void setProfileIconId(Integer profileIconId) {
        this.profileIconId = profileIconId;
    }

    /**
     *
     * @return summoner level
     */
    public Long getSummonerLevel() {
        return summonerLevel;
    }

    /**
     *
     * @param summonerLevel summoner level to set
     */
    public void setSummonerLevel(Long summonerLevel) {
        this.summonerLevel = summonerLevel;
    }

    /**
     *
     * @return last complete synchronization time
     */
    public Instant getLastSyncAt() {
        return lastSyncAt;
    }

    /**
     *
     * @param lastSyncAt last complete synchronization time to set
     */
    public void setLastSyncAt(Instant lastSyncAt) {
        this.lastSyncAt = lastSyncAt;
    }

    /**
     *
     * @return professional information when available
     */
    public Professional getProfessional() {
        return professional;
    }

    /**
     *
     * @param professional professional information to set
     */
    public void setProfessional(Professional professional) {
        this.professional = professional;
    }

    /**
     *
     * @return stored match participations
     */
    public List<Participation> getParticipations() {
        return participations;
    }

    /**
     *
     * @param participations stored match participations to set
     */
    public void setParticipations(List<Participation> participations) {
        this.participations = participations;
    }

    /**
     *
     * @return current ranked queue results
     */
    public List<RankedRank> getRankedRanks() {
        return rankedRanks;
    }

    /**
     *
     * @param rankedRanks current ranked queue results to set
     */
    public void setRankedRanks(List<RankedRank> rankedRanks) {
        this.rankedRanks = rankedRanks;
    }

    /**
     *
     * @return calculated player statistics
     */
    public PlayerStats getStats() {
        return stats;
    }

    /**
     *
     * @param stats calculated player statistics to set
     */
    public void setStats(PlayerStats stats) {
        this.stats = stats;
    }
}
