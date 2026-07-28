package es.unex.cume.tfg.backend.model;

import jakarta.persistence.*;

/**
 * Entity that marks a player as a professional competitor.
 */
@Entity
public class Professional {
    @Id
    private String puuid;

    private String code;

    private String proName;
    private String teamName;
    private String league;
    @Enumerated(EnumType.STRING)
    private Role role;

    // Relations
    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    private Player player;

    /** Creates an empty professional player. */
    public Professional() {

    }

    /**
     *
     * @return player PUUID
     */
    public String getPuuid() {
        return puuid;
    }

    /**
     *
     * @param puuid player PUUID to set
     */
    public void setPuuid(String puuid) {
        this.puuid = puuid;
    }

    /**
     *
     * @return professional player name
     */
    public String getProName() {
        return proName;
    }

    /**
     *
     * @param proName professional player name to set
     */
    public void setProName(String proName) {
        this.proName = proName;
    }

    /**
     *
     * @return current team name
     */
    public String getTeamName() {
        return teamName;
    }

    /**
     *
     * @param teamName current team name to set
     */
    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }

    /**
     *
     * @return current league
     */
    public String getLeague() {
        return league;
    }

    /**
     *
     * @param league current league to set
     */
    public void setLeague(String league) {
        this.league = league;
    }

    /**
     *
     * @return related player account
     */
    public Player getPlayer() {
        return player;
    }

    /**
     *
     * @param player related player account to set
     */
    public void setPlayer(Player player) {
        this.player = player;
    }

    /**
     *
     * @return stable roster code
     */
    public String getCode() {
        return code;
    }

    /**
     *
     * @param code stable roster code to set
     */
    public void setCode(String code) {
        this.code = code;
    }

    /**
     *
     * @return team role
     */
    public Role getRole() {
        return role;
    }

    /**
     *
     * @param role team role to set
     */
    public void setRole(Role role) {
        this.role = role;
    }
}
