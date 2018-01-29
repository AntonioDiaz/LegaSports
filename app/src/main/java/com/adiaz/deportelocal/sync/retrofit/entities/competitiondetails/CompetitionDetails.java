
package com.adiaz.deportelocal.sync.retrofit.entities.competitiondetails;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CompetitionDetails {

    @SerializedName("lastPublished")
    @Expose
    private Long lastPublished;
    @SerializedName("matches")
    @Expose
    private List<Match> matches = null;
    @SerializedName("classification")
    @Expose
    private List<Classification> classification = null;

    @SerializedName("weeksNames")
    @Expose
    private List<String> weeksNames = null;

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

    public Long getLastPublished() {
        return lastPublished;
    }

    public void setLastPublished(Long lastPublished) {
        this.lastPublished = lastPublished;
    }

    public List<String> getWeeksNames() {
        return weeksNames;
    }

    public void setWeeksNames(List<String> weeksNames) {
        this.weeksNames = weeksNames;
    }
}
