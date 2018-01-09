package com.adiaz.localsports.entities;

/* Created by toni on 28/03/2017. */

public class Team {

	private String teamName;
	/* Matches of a team in a competition. */
	private Match[] matches;

	public Team(String teamName, Integer weeksNumber) {
		this.teamName = teamName;
		this.matches = new Match[weeksNumber];
	}

	public Team(String teamName, Match[] matches) {
		this.teamName = teamName;
		this.matches = matches;
	}

	public String getTeamName() {
		return teamName;
	}

	public void setTeamName(String teamName) {
		this.teamName = teamName;
	}

	public Match[] getMatches() {
		return matches;
	}

	public void setMatches(Match[] matches) {
		this.matches = matches;
	}

	public void add(Integer week, Match match) {
		this.matches[week] = match;
	}
}
