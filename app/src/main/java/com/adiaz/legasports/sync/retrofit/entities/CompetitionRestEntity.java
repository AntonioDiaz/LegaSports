
package com.adiaz.legasports.sync.retrofit.entities;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CompetitionRestEntity {

    @SerializedName("id")
    @Expose
    private Long id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("sportEntity")
    @Expose
    private SportEntity sportEntity;
    @SerializedName("categoryEntity")
    @Expose
    private CategoryEntity categoryEntity;
    @SerializedName("townEntity")
    @Expose
    private TownEntity townEntity;
    @SerializedName("matches")
    @Expose
    private List<Match> matches = null;
    @SerializedName("classification")
    @Expose
    private List<Classification> classification = null;
    @SerializedName("teamsDeref")
    @Expose
    private List<TeamsDeref> teamsDeref = null;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public SportEntity getSportEntity() {
        return sportEntity;
    }

    public void setSportEntity(SportEntity sportEntity) {
        this.sportEntity = sportEntity;
    }

    public CategoryEntity getCategoryEntity() {
        return categoryEntity;
    }

    public void setCategoryEntity(CategoryEntity categoryEntity) {
        this.categoryEntity = categoryEntity;
    }

    public TownEntity getTownEntity() {
        return townEntity;
    }

    public void setTownEntity(TownEntity townEntity) {
        this.townEntity = townEntity;
    }

    public List<Match> getMatches() {
        return matches;
    }

    public void setMatches(List<Match> matches) {
        this.matches = matches;
    }

    public List<Classification> getClassification() {
        return classification;
    }

    public void setClassification(List<Classification> classification) {
        this.classification = classification;
    }

    public List<TeamsDeref> getTeamsDeref() {
        return teamsDeref;
    }

    public void setTeamsDeref(List<TeamsDeref> teamsDeref) {
        this.teamsDeref = teamsDeref;
    }

}
