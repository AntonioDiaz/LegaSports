
package com.adiaz.legasports.sync.retrofit;

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
    @SerializedName("sportStr")
    @Expose
    private String sportStr;
    @SerializedName("categoryStr")
    @Expose
    private String categoryStr;
    @SerializedName("matchesSize")
    @Expose
    private Long matchesSize;
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

    public String getSportStr() {
        return sportStr;
    }

    public void setSportStr(String sportStr) {
        this.sportStr = sportStr;
    }

    public String getCategoryStr() {
        return categoryStr;
    }

    public void setCategoryStr(String categoryStr) {
        this.categoryStr = categoryStr;
    }

    public Long getMatchesSize() {
        return matchesSize;
    }

    public void setMatchesSize(Long matchesSize) {
        this.matchesSize = matchesSize;
    }

    public List<MatchRestEntity> getMatches() {
        return matches;
    }

    public void setMatches(List<MatchRestEntity> matches) {
        this.matches = matches;
    }

}
