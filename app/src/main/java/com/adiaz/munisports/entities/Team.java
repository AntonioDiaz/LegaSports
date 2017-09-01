package com.adiaz.munisports.entities;

/* Created by toni on 28/03/2017. */

public class Team {

	private String teamName;
	private TeamMatch[] matches;

	public Team(String teamName, Integer weeksNumber) {
		this.teamName = teamName;
		this.matches = new TeamMatch[weeksNumber];
	}

	public Team(String teamName, TeamMatch[] matches) {
		this.teamName = teamName;
		this.matches = matches;
	}

	public String getTeamName() {
		return teamName;
	}

	public void setTeamName(String teamName) {
		this.teamName = teamName;
	}

	public TeamMatch[] getMatches() {
		return matches;
	}

	public void setMatches(TeamMatch[] matches) {
		this.matches = matches;
	}

	public void add(Integer week, TeamMatch teamMatch) {
		this.matches[week] = teamMatch;
	}
}
