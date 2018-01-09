
package com.adiaz.localsports.sync.retrofit.entities.competition;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TeamsDeref {

    @SerializedName("id")
    @Expose
    private Long id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("categoryEntity")
    @Expose
    private CategoryEntity_ categoryEntity;
    @SerializedName("townEntity")
    @Expose
    private TownEntity_ townEntity;
    @SerializedName("clubEntity")
    @Expose
    private ClubEntity clubEntity;
    @SerializedName("sportEntity")
    @Expose
    private SportEntity_ sportEntity;

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

    public CategoryEntity_ getCategoryEntity() {
        return categoryEntity;
    }

    public void setCategoryEntity(CategoryEntity_ categoryEntity) {
        this.categoryEntity = categoryEntity;
    }

    public TownEntity_ getTownEntity() {
        return townEntity;
    }

    public void setTownEntity(TownEntity_ townEntity) {
        this.townEntity = townEntity;
    }

    public ClubEntity getClubEntity() {
        return clubEntity;
    }

    public void setClubEntity(ClubEntity clubEntity) {
        this.clubEntity = clubEntity;
    }

    public SportEntity_ getSportEntity() {
        return sportEntity;
    }

    public void setSportEntity(SportEntity_ sportEntity) {
        this.sportEntity = sportEntity;
    }

}
