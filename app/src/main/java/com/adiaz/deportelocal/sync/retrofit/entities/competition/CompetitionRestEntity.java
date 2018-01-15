
package com.adiaz.deportelocal.sync.retrofit.entities.competition;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

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
    private Object matches;
    @SerializedName("classification")
    @Expose
    private Object classification;
    @SerializedName("lastPublished")
    @Expose
    private Long lastPublished;

    @SerializedName("teamsDeref")
    @Expose
    private List<TeamsDeref> teamsDeref = null;

    @SerializedName("teamsAffectedByLastUpdateDeref")
    @Expose
    private List<TeamsDeref> teamsAffectedByLastUpdateDeref = null;

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

    public Object getMatches() {
        return matches;
    }

    public void setMatches(Object matches) {
        this.matches = matches;
    }

    public Object getClassification() {
        return classification;
    }

    public void setClassification(Object classification) {
        this.classification = classification;
    }

    public Long getLastPublished() {
        return lastPublished;
    }

    public void setLastPublished(Long lastPublished) {
        this.lastPublished = lastPublished;
    }

    public List<TeamsDeref> getTeamsDeref() {
        return teamsDeref;
    }

    public void setTeamsDeref(List<TeamsDeref> teamsDeref) {
        this.teamsDeref = teamsDeref;
    }

    public List<TeamsDeref> getTeamsAffectedByLastUpdateDeref() {
        return teamsAffectedByLastUpdateDeref;
    }

    public void setTeamsAffectedByLastUpdateDeref(List<TeamsDeref> teamsAffectedByLastUpdateDeref) {
        this.teamsAffectedByLastUpdateDeref = teamsAffectedByLastUpdateDeref;
    }
}
