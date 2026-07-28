package es.unex.cume.tfg.backend.model;

import jakarta.persistence.*;

import java.time.Instant;

/**
 * Entity that represents one player's participation in a match.
 */
@Entity
public class Participation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Result
    private Integer teamId;
    private boolean win;

    // Stats
    private Integer kills;
    private Integer deaths;
    private Integer assists;

    // General information
    private Instant gameStartAt;

    @Enumerated(EnumType.STRING)
    private Role teamPosition;

    // Relations
    @OneToOne(mappedBy = "participation", cascade = CascadeType.ALL, orphanRemoval = true)
    private Build build;

    @ManyToOne(fetch = FetchType.LAZY)
    private Player player;

    @ManyToOne(fetch = FetchType.LAZY)
    private Match match;

    @ManyToOne(fetch = FetchType.LAZY)
    private Champion champion;

    /** Creates an empty participation. */
    public Participation() {

    }

    /**
     *
     * @return participation identifier
     */
    public Long getId() {
        return id;
    }

    /**
     *
     * @param id participation identifier to set
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     *
     * @return team identifier
     */
    public Integer getTeamId() {
        return teamId;
    }

    /**
     *
     * @param teamId team identifier to set
     */
    public void setTeamId(Integer teamId) {
        this.teamId = teamId;
    }

    /**
     *
     * @return whether the player won
     */
    public boolean isWin() {
        return win;
    }

    /**
     *
     * @param win whether the player won
     */
    public void setWin(boolean win) {
        this.win = win;
    }

    /**
     *
     * @return kills
     */
    public Integer getKills() {
        return kills;
    }

    /**
     *
     * @param kills kills to set
     */
    public void setKills(Integer kills) {
        this.kills = kills;
    }

    /**
     *
     * @return deaths
     */
    public Integer getDeaths() {
        return deaths;
    }

    /**
     *
     * @param deaths deaths to set
     */
    public void setDeaths(Integer deaths) {
        this.deaths = deaths;
    }

    /**
     *
     * @return assists
     */
    public Integer getAssists() {
        return assists;
    }

    /**
     *
     * @param assists assists to set
     */
    public void setAssists(Integer assists) {
        this.assists = assists;
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
     * @return team position
     */
    public Role getTeamPosition() {
        return teamPosition;
    }

    /**
     *
     * @param teamPosition team position to set
     */
    public void setTeamPosition(Role teamPosition) {
        this.teamPosition = teamPosition;
    }

    /**
     *
     * @return build used in the participation
     */
    public Build getBuild() {
        return build;
    }

    /**
     *
     * @param build build used in the participation to set
     */
    public void setBuild(Build build) {
        this.build = build;
    }

    /**
     *
     * @return participating player
     */
    public Player getPlayer() {
        return player;
    }

    /**
     *
     * @param player participating player to set
     */
    public void setPlayer(Player player) {
        this.player = player;
    }

    /**
     *
     * @return related match
     */
    public Match getMatch() {
        return match;
    }

    /**
     *
     * @param match related match to set
     */
    public void setMatch(Match match) {
        this.match = match;
    }

    /**
     *
     * @return champion played
     */
    public Champion getChampion() {
        return champion;
    }

    /**
     *
     * @param champion champion played to set
     */
    public void setChampion(Champion champion) {
        this.champion = champion;
    }
}
