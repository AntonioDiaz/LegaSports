
package com.adiaz.legasports.sync.retrofit.entities;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MatchRestEntity {

    @SerializedName("id")
    @Expose
    private Long id;
    @SerializedName("teamLocal")
    @Expose
    private String teamLocal;
    @SerializedName("teamVisitor")
    @Expose
    private String teamVisitor;
    @SerializedName("date")
    @Expose
    private Long date;
    @SerializedName("place")
    @Expose
    private String place;
    @SerializedName("scoreLocal")
    @Expose
    private Long scoreLocal;
    @SerializedName("scoreVisitor")
    @Expose
    private Long scoreVisitor;
    @SerializedName("week")
    @Expose
    private Long week;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTeamLocal() {
        return teamLocal;
    }

    public void setTeamLocal(String teamLocal) {
        this.teamLocal = teamLocal;
    }

    public String getTeamVisitor() {
        return teamVisitor;
    }

    public void setTeamVisitor(String teamVisitor) {
        this.teamVisitor = teamVisitor;
    }

    public Long getDate() {
        return date;
    }

    public void setDate(Long date) {
        this.date = date;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public Long getScoreLocal() {
        return scoreLocal;
    }

    public void setScoreLocal(Long scoreLocal) {
        this.scoreLocal = scoreLocal;
    }

    public Long getScoreVisitor() {
        return scoreVisitor;
    }

    public void setScoreVisitor(Long scoreVisitor) {
        this.scoreVisitor = scoreVisitor;
    }

    public Long getWeek() {
        return week;
    }

    public void setWeek(Long week) {
        this.week = week;
    }

}
