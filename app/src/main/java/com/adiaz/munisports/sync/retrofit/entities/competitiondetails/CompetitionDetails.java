
package com.adiaz.munisports.sync.retrofit.entities.competitiondetails;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CompetitionDetails {

    @SerializedName("matches")
    @Expose
    private List<Match> matches = null;
    @SerializedName("classification")
    @Expose
    private List<Classification> classification = null;

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

}
