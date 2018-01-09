package com.adiaz.localsports.sync.retrofit.entities.issue;

/**
 * Created by toni on 17/09/2017.
 */

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Issue {

	@SerializedName("clientId")
	@Expose
	private String clientId;
	@SerializedName("competitionId")
	@Expose
	private Long competitionId;
	@SerializedName("matchId")
	@Expose
	private Long matchId;
	@SerializedName("description")
	@Expose
	private String description;

	public String getClientId() {
		return clientId;
	}

	public void setClientId(String clientId) {
		this.clientId = clientId;
	}

	public Long getCompetitionId() {
		return competitionId;
	}

	public void setCompetitionId(Long competitionId) {
		this.competitionId = competitionId;
	}

	public Long getMatchId() {
		return matchId;
	}

	public void setMatchId(Long matchId) {
		this.matchId = matchId;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

}