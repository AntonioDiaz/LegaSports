
package com.adiaz.localsports.sync.retrofit.entities.competitiondetails;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SportCenterCourt {

    @SerializedName("id")
    @Expose
    private Long id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("sportCenter")
    @Expose
    private SportCenter sportCenter;
    @SerializedName("nameWithCenter")
    @Expose
    private String nameWithCenter;

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

    public SportCenter getSportCenter() {
        return sportCenter;
    }

    public void setSportCenter(SportCenter sportCenter) {
        this.sportCenter = sportCenter;
    }

    public String getNameWithCenter() {
        return nameWithCenter;
    }

    public void setNameWithCenter(String nameWithCenter) {
        this.nameWithCenter = nameWithCenter;
    }

}
