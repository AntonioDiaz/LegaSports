
package com.adiaz.legasports.sync.retrofit.entities;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TeamVisitorEntity {

    @SerializedName("id")
    @Expose
    private Long id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("categoryEntity")
    @Expose
    private CategoryEntity__ categoryEntity;
    @SerializedName("townEntity")
    @Expose
    private TownEntity___ townEntity;
    @SerializedName("clubEntity")
    @Expose
    private ClubEntity_ clubEntity;
    @SerializedName("sportEntity")
    @Expose
    private SportEntity__ sportEntity;

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

    public CategoryEntity__ getCategoryEntity() {
        return categoryEntity;
    }

    public void setCategoryEntity(CategoryEntity__ categoryEntity) {
        this.categoryEntity = categoryEntity;
    }

    public TownEntity___ getTownEntity() {
        return townEntity;
    }

    public void setTownEntity(TownEntity___ townEntity) {
        this.townEntity = townEntity;
    }

    public ClubEntity_ getClubEntity() {
        return clubEntity;
    }

    public void setClubEntity(ClubEntity_ clubEntity) {
        this.clubEntity = clubEntity;
    }

    public SportEntity__ getSportEntity() {
        return sportEntity;
    }

    public void setSportEntity(SportEntity__ sportEntity) {
        this.sportEntity = sportEntity;
    }

}
