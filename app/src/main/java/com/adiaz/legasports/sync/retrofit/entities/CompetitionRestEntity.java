
package com.adiaz.legasports.sync.retrofit.entities;

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
    @SerializedName("matches")
    @Expose
    private List<MatchRestEntity> matches = null;

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

    public List<MatchRestEntity> getMatches() {
        return matches;
    }

    public void setMatches(List<MatchRestEntity> matches) {
        this.matches = matches;
    }

}
