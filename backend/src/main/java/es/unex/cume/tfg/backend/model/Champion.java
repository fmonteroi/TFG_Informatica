package es.unex.cume.tfg.backend.model;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
public class Champion {
    @Id
    private Integer championId;

    private String championName;

    // Relations
    @OneToMany(mappedBy = "champion")
    private List<Participation> participations = new ArrayList<>();

    public Champion() {
    }

    public Integer getChampionId() {
        return championId;
    }

    public void setChampionId(Integer championId) {
        this.championId = championId;
    }

    public String getChampionName() {
        return championName;
    }

    public void setChampionName(String championName) {
        this.championName = championName;
    }

    public List<Participation> getParticipations() {
        return participations;
    }

    public void setParticipations(List<Participation> participations) {
        this.participations = participations;
    }
}
