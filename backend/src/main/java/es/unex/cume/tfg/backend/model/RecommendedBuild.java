package es.unex.cume.tfg.backend.model;

import jakarta.persistence.*;

/**
 * Entity that stores the recommended build for one champion.
 */
@Entity
public class RecommendedBuild {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long recommendedBuildId;

    @Enumerated(EnumType.STRING)
    private Role role;

    private long roleGames;

    private Integer item0;
    private Integer item1;
    private Integer item2;
    private Integer item3;
    private Integer item4;
    private Integer item5;
    private Integer item6;
    private Integer roleBoundItem;

    private Integer summoner1Id;
    private Integer summoner2Id;

    @ManyToOne(fetch = FetchType.LAZY)
    private Champion champion;

    /** Creates an empty recommended build. */
    public RecommendedBuild() {
    }

    /**
     *
     * @return first item identifier
     */
    public Integer getItem0() {
        return item0;
    }

    /**
     *
     * @param item0 first item identifier to set
     */
    public void setItem0(Integer item0) {
        this.item0 = item0;
    }

    /**
     *
     * @return second item identifier
     */
    public Integer getItem1() {
        return item1;
    }

    /**
     *
     * @param item1 second item identifier to set
     */
    public void setItem1(Integer item1) {
        this.item1 = item1;
    }

    /**
     *
     * @return third item identifier
     */
    public Integer getItem2() {
        return item2;
    }

    /**
     *
     * @param item2 third item identifier to set
     */
    public void setItem2(Integer item2) {
        this.item2 = item2;
    }

    /**
     *
     * @return fourth item identifier
     */
    public Integer getItem3() {
        return item3;
    }

    /**
     *
     * @param item3 fourth item identifier to set
     */
    public void setItem3(Integer item3) {
        this.item3 = item3;
    }

    /**
     *
     * @return fifth item identifier
     */
    public Integer getItem4() {
        return item4;
    }

    /**
     *
     * @param item4 fifth item identifier to set
     */
    public void setItem4(Integer item4) {
        this.item4 = item4;
    }

    /**
     *
     * @return sixth item identifier
     */
    public Integer getItem5() {
        return item5;
    }

    /**
     *
     * @param item5 sixth item identifier to set
     */
    public void setItem5(Integer item5) {
        this.item5 = item5;
    }

    /**
     *
     * @return seventh item identifier
     */
    public Integer getItem6() {
        return item6;
    }

    /**
     *
     * @param item6 seventh item identifier to set
     */
    public void setItem6(Integer item6) {
        this.item6 = item6;
    }

    /**
     *
     * @return role-specific item identifier
     */
    public Integer getRoleBoundItem() {
        return roleBoundItem;
    }

    /**
     *
     * @param roleBoundItem role-specific item identifier to set
     */
    public void setRoleBoundItem(Integer roleBoundItem) {
        this.roleBoundItem = roleBoundItem;
    }

    /**
     *
     * @return first summoner spell identifier
     */
    public Integer getSummoner1Id() {
        return summoner1Id;
    }

    /**
     *
     * @param summoner1Id first summoner spell identifier to set
     */
    public void setSummoner1Id(Integer summoner1Id) {
        this.summoner1Id = summoner1Id;
    }

    /**
     *
     * @return second summoner spell identifier
     */
    public Integer getSummoner2Id() {
        return summoner2Id;
    }

    /**
     *
     * @param summoner2Id second summoner spell identifier to set
     */
    public void setSummoner2Id(Integer summoner2Id) {
        this.summoner2Id = summoner2Id;
    }

    /**
     *
     * @return champion that owns the recommendation
     */
    public Champion getChampion() {
        return champion;
    }

    /**
     *
     * @param champion champion that owns the recommendation to set
     */
    public void setChampion(Champion champion) {
        this.champion = champion;
    }

    /**
     *
     * @return recommended build identifier
     */
    public Long getRecommendedBuildId() {
        return recommendedBuildId;
    }

    /**
     *
     * @param recommendedBuildId recommended build identifier to set
     */
    public void setRecommendedBuildId(Long recommendedBuildId) {
        this.recommendedBuildId = recommendedBuildId;
    }

    /**
     *
     * @return role of the recommendation
     */
    public Role getRole() {
        return role;
    }

    /**
     *
     * @param role role of the recommendation to set
     */
    public void setRole(Role role) {
        this.role = role;
    }

    /**
     *
     * @return games played with the champion in this role
     */
    public long getRoleGames() {
        return roleGames;
    }

    /**
     *
     * @param roleGames games played with the champion in this role to set
     */
    public void setRoleGames(long roleGames) {
        this.roleGames = roleGames;
    }
}
