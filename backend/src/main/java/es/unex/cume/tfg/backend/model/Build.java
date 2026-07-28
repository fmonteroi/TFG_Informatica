package es.unex.cume.tfg.backend.model;

import jakarta.persistence.*;

/**
 * Entity that stores the item and summoner spell setup used in one participation.
 */
@Entity
public class Build {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

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

    // Relations
    @OneToOne(fetch = FetchType.LAZY)
    private Participation participation;

    /** Creates an empty build. */
    public Build() {
    }

    /**
     *
     * @return build identifier
     */
    public Long getId() {
        return id;
    }

    /**
     *
     * @param id build identifier to set
     */
    public void setId(Long id) {
        this.id = id;
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
     * @return participation that used the build
     */
    public Participation getParticipation() {
        return participation;
    }

    /**
     *
     * @param participation participation that used the build to set
     */
    public void setParticipation(Participation participation) {
        this.participation = participation;
    }
}

