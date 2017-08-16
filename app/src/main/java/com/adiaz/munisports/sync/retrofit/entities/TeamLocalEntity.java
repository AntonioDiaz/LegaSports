
package com.adiaz.munisports.sync.retrofit.entities;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TeamLocalEntity {

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
    private TownEntity__ townEntity;
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

    public TownEntity__ getTownEntity() {
        return townEntity;
    }

    public void setTownEntity(TownEntity__ townEntity) {
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
