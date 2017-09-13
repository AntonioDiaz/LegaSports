package com.adiaz.munisports.entities;

/**
 * Created by toni on 28/04/2017.
 */

public class TeamFavorite {
	private String name;
	private Long idCompetitionServer;
	private String competitionName;
	private String sportTag;
	private String categoryTag;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Long getIdCompetitionServer() {
		return idCompetitionServer;
	}

	public void setIdCompetitionServer(Long idCompetitionServer) {
		this.idCompetitionServer = idCompetitionServer;
	}

	public String getSportTag() {
		return sportTag;
	}

	public void setSportTag(String sportTag) {
		this.sportTag = sportTag;
	}

	public String getCategoryTag() {
		return categoryTag;
	}

	public void setCategoryTag(String categoryTag) {
		this.categoryTag = categoryTag;
	}

	public String getCompetitionName() {
		return competitionName;
	}

	public void setCompetitionName(String competitionName) {
		this.competitionName = competitionName;
	}
}
