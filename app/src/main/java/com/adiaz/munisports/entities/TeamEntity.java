package com.adiaz.munisports.entities;

/* Created by toni on 28/03/2017. */

public class TeamEntity {

	private String teamName;
	private TeamMatchEntity[] matches;

	public TeamEntity(String teamName, Integer weeksNumber) {
		this.teamName = teamName;
		this.matches = new TeamMatchEntity[weeksNumber];
	}

	public TeamEntity(String teamName, TeamMatchEntity[] matches) {
		this.teamName = teamName;
		this.matches = matches;
	}

	public String getTeamName() {
		return teamName;
	}

	public void setTeamName(String teamName) {
		this.teamName = teamName;
	}

	public TeamMatchEntity[] getMatches() {
		return matches;
	}

	public void setMatches(TeamMatchEntity[] matches) {
		this.matches = matches;
	}

	public void add(Integer week, TeamMatchEntity teamMatchEntity) {
		this.matches[week] = teamMatchEntity;
	}
}
