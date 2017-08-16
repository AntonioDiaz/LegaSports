
package com.adiaz.munisports.sync.retrofit.entities;

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
    private CategoryEntity___ categoryEntity;
    @SerializedName("townEntity")
    @Expose
    private TownEntity____ townEntity;
    @SerializedName("clubEntity")
    @Expose
    private ClubEntity__ clubEntity;
    @SerializedName("sportEntity")
    @Expose
    private SportEntity___ sportEntity;

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

    public CategoryEntity___ getCategoryEntity() {
        return categoryEntity;
    }

    public void setCategoryEntity(CategoryEntity___ categoryEntity) {
        this.categoryEntity = categoryEntity;
    }

    public TownEntity____ getTownEntity() {
        return townEntity;
    }

    public void setTownEntity(TownEntity____ townEntity) {
        this.townEntity = townEntity;
    }

    public ClubEntity__ getClubEntity() {
        return clubEntity;
    }

    public void setClubEntity(ClubEntity__ clubEntity) {
        this.clubEntity = clubEntity;
    }

    public SportEntity___ getSportEntity() {
        return sportEntity;
    }

    public void setSportEntity(SportEntity___ sportEntity) {
        this.sportEntity = sportEntity;
    }

}
